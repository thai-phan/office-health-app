package com.sewon.officehealth.service.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import timber.log.Timber
import java.io.IOException
import java.util.Arrays
import java.util.UUID

/**
 * wrap BLE communication into socket like class
 * - connect, disconnect and write as methods,
 * - read + status is returned by SerialListener
 */
@SuppressLint("MissingPermission") // various BluetoothGatt, BluetoothDevice methods

class SocketBleGatt(val context: Context, var device: BluetoothDevice) : BluetoothGattCallback() {
  companion object {

    private val BLUETOOTH_LE_CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    private val BLUETOOTH_LE_CC254X_SERVICE =
      UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb")
    private val BLUETOOTH_LE_CC254X_CHAR_RW =
      UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
    private val BLUETOOTH_LE_NRF_SERVICE = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")

    // read on microbit, write on adafruit
    private val BLUETOOTH_LE_NRF_CHAR_RW2 =
      UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e")
    private val BLUETOOTH_LE_NRF_CHAR_RW3 = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")
    private val BLUETOOTH_LE_MICROCHIP_SERVICE =
      UUID.fromString("49535343-FE7D-4AE5-8FA9-9FAFD205E455")
    private val BLUETOOTH_LE_MICROCHIP_CHAR_RW =
      UUID.fromString("49535343-1E4D-4BD9-BA61-23C647249616")
    private val BLUETOOTH_LE_MICROCHIP_CHAR_W =
      UUID.fromString("49535343-8841-43F4-A8D4-ECBE34729BB3")

    // https://play.google.com/store/apps/details?id=com.telit.tiosample
    // https://www.telit.com/wp-content/uploads/2017/09/TIO_Implementation_Guide_r6.pdf
    private val BLUETOOTH_LE_TIO_SERVICE = UUID.fromString("0000FEFB-0000-1000-8000-00805F9B34FB")

    // WNR
    private val BLUETOOTH_LE_TIO_CHAR_TX =
      UUID.fromString("00000001-0000-1000-8000-008025000000")

    // N
    private val BLUETOOTH_LE_TIO_CHAR_RX =
      UUID.fromString("00000002-0000-1000-8000-008025000000")

    // W
    private val BLUETOOTH_LE_TIO_CHAR_TX_CREDITS =
      UUID.fromString("00000003-0000-1000-8000-008025000000")

    // I
    private val BLUETOOTH_LE_TIO_CHAR_RX_CREDITS =
      UUID.fromString("00000004-0000-1000-8000-008025000000")

    // BLE standard does not limit, some BLE 4.2 devices support 251, various source say that Android has max 512
    private const val MAX_MTU = 512
    private const val DEFAULT_MTU = 23
  }

  val TAG: String = this.javaClass.name

  /**
   * delegate device specific behaviour to inner class
   */
  open class DeviceDelegate {
    open fun connectCharacteristics(s: BluetoothGattService): Boolean {
      return true
    }

    // following methods only overwritten for Telit devices
    open fun onDescriptorWrite(g: BluetoothGatt, d: BluetoothGattDescriptor, status: Int) {
      /*nop*/
    }

    open fun onCharacteristicChanged(g: BluetoothGatt, c: BluetoothGattCharacteristic) {
      /*nop*/
    }

    open fun onCharacteristicWrite(
      g: BluetoothGatt,
      c: BluetoothGattCharacteristic,
      status: Int
    ) {
      /*nop*/
    }

    open fun canWrite(): Boolean {
      return true
    }

    open fun disconnect() {
      /*nop*/
    }
  }

  private val writeBuffer: ArrayList<ByteArray?> = ArrayList()
  private val pairingIntentFilter: IntentFilter = IntentFilter()
  private val pairingBroadcastReceiver: BroadcastReceiver
  private val disconnectBroadcastReceiver: BroadcastReceiver
  private var listener: ISerialListener? = null
  private var delegate: DeviceDelegate? = null
  private var gatt: BluetoothGatt? = null
  private var readCharacteristic: BluetoothGattCharacteristic? = null
  private var writeCharacteristic: BluetoothGattCharacteristic? = null
  private var writePending = false
  private var canceled = false
  private var connected = false
  private var payloadSize = DEFAULT_MTU - 3

  init {
//    if (context instanceof Activity)
//      throw new InvalidParameterException("expected non UI context");
    pairingIntentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
    pairingIntentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
    pairingBroadcastReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        onPairingBroadcastReceive(intent)
      }
    }
    disconnectBroadcastReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        if (listener != null) listener!!.onSerialIoError(IOException("background disconnect"))
        disconnect() // disconnect now, else would be queued until UI re-attached
      }
    }
  }

  val name: String
    get() = if (device.name != null) device.name else device.address

  fun disconnect() {
    Timber.tag(TAG).d("disconnect")
    listener = null // ignore remaining data and errors
    canceled = true
    synchronized(writeBuffer) {
      writePending = false
      writeBuffer.clear()
    }
    readCharacteristic = null
    writeCharacteristic = null
    if (delegate != null) delegate!!.disconnect()
    if (gatt != null) {
      Timber.tag(TAG).d("gatt.disconnect")
      gatt!!.disconnect()
      Timber.tag(TAG).d("gatt.close")
      try {
        gatt!!.close()
      } catch (ignored: Exception) {
        Timber.tag(TAG).d(ignored)
      }
      gatt = null
      connected = false
    }
    try {
      context.unregisterReceiver(pairingBroadcastReceiver)
    } catch (ignored: Exception) {
    }
    try {
      context.unregisterReceiver(disconnectBroadcastReceiver)
    } catch (ignored: Exception) {
    }
  }

  /**
   * connect-success and most connect-errors are returned asynchronously to listener
   */
  @Throws(IOException::class)
  fun connect(listener: ISerialListener) {
    if (connected || gatt != null) throw IOException("already connected")
    canceled = false
    this.listener = listener
//    context.registerReceiver(
//      disconnectBroadcastReceiver,
//      IntentFilter(Constants.INTENT_ACTION_DISCONNECT)
//    )
    Timber.tag(TAG).d("connect %s", device)
//    context.registerReceiver(pairingBroadcastReceiver, pairingIntentFilter)
    Timber.tag(TAG).d("connectGatt, LE")
    gatt = device.connectGatt(context, false, this, BluetoothDevice.TRANSPORT_LE)
    if (gatt == null) throw IOException("connectGatt failed")
    // continues asynchronously in onPairingBroadcastReceive() and onConnectionStateChange()
  }

  private fun onPairingBroadcastReceive(intent: Intent) {
    // for ARM Mbed, Microbit, ... use pairing from Android bluetooth settings
    // for HM10-clone, ... pairing is initiated here
//    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

    val device = intent.parcelable<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)


    if (device == null || device != this.device) return
    when (intent.action) {
      BluetoothDevice.ACTION_PAIRING_REQUEST -> {
        val pairingVariant = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, -1)
        Timber.tag(TAG).d("pairing request $pairingVariant")
        onSerialConnectError(IOException("context.getString(R.string.pairing_request)"))
      }

      BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
        val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
        val previousBondState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, -1)
        Timber.tag(TAG).d("bond state $previousBondState->$bondState")
      }

      else -> Timber.tag(TAG).d("unknown broadcast %s", intent.action)
    }
  }

  override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
    // status directly taken from gat_api.h, e.g. 133=0x85=GATT_ERROR ~= timeout
    if (newState == BluetoothProfile.STATE_CONNECTED) {
      Timber.tag(TAG).d("connect status $status, discoverServices")
      if (!gatt.discoverServices()) onSerialConnectError(IOException("discoverServices failed"))
    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
      if (connected) onSerialIoError(IOException("gatt status $status")) else onSerialConnectError(
        IOException(
          "gatt status $status"
        )
      )
    } else {
      Timber.tag(TAG).d("unknown connect state $newState $status")
    }
    // continues asynchronously in onServicesDiscovered()
  }

  override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
    Timber.tag(TAG).d("servicesDiscovered, status %s", status)
    if (canceled) return
    connectCharacteristics1(gatt)
  }

  private fun connectCharacteristics1(gatt: BluetoothGatt) {
    var sync = true
    writePending = false
    for (gattService in gatt.services) {
      if (gattService.uuid == BLUETOOTH_LE_CC254X_SERVICE) delegate = Cc245XDelegate()
      if (gattService.uuid == BLUETOOTH_LE_MICROCHIP_SERVICE) delegate = MicrochipDelegate()
      if (gattService.uuid == BLUETOOTH_LE_NRF_SERVICE) delegate = NrfDelegate()
      if (gattService.uuid == BLUETOOTH_LE_TIO_SERVICE) delegate = TelitDelegate()
      if (delegate != null) {
        sync = delegate!!.connectCharacteristics(gattService)
        break
      }
    }
    if (canceled) return
    if (delegate == null || readCharacteristic == null || writeCharacteristic == null) {
      for (gattService in gatt.services) {
        Timber.tag(TAG).d("service %s", gattService.uuid)
        for (characteristic in gattService.characteristics) Timber.tag(TAG)
          .d("characteristic %s", characteristic.uuid)
      }
      onSerialConnectError(IOException("no serial profile found"))
      return
    }
    if (sync) connectCharacteristics2(gatt)
  }

  private fun connectCharacteristics2(gatt: BluetoothGatt) {
    Timber.tag(TAG).d("request max MTU")
    if (!gatt.requestMtu(MAX_MTU)) onSerialConnectError(IOException("request MTU failed"))
    // continues asynchronously in onMtuChanged
  }

  override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
    Timber.tag(TAG).d("mtu size $mtu, status=$status")
    if (status == BluetoothGatt.GATT_SUCCESS) {
      payloadSize = mtu - 3
      Timber.tag(TAG).d("payload size %s", payloadSize)
    }
    connectCharacteristics3(gatt)
  }

  private fun connectCharacteristics3(gatt: BluetoothGatt) {
    val writeProperties = writeCharacteristic!!.properties
    if (// Microbit,HM10-clone have WRITE
      writeProperties and BluetoothGattCharacteristic.PROPERTY_WRITE + BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE == 0) { // HM10,TI uart,Telit have only WRITE_NO_RESPONSE
      onSerialConnectError(IOException("write characteristic not writable"))
      return
    }
    if (!gatt.setCharacteristicNotification(readCharacteristic, true)) {
      onSerialConnectError(IOException("no notification for read characteristic"))
      return
    }
    val readDescriptor = readCharacteristic!!.getDescriptor(BLUETOOTH_LE_CCCD)
    if (readDescriptor == null) {
      onSerialConnectError(IOException("no CCCD descriptor for read characteristic"))
      return
    }
    val readProperties = readCharacteristic!!.properties
    if (readProperties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
      Timber.tag(TAG).d("enable read indication")
      readDescriptor.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
    } else if (readProperties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
      Timber.tag(TAG).d("enable read notification")
      readDescriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
    } else {
      onSerialConnectError(IOException("no indication/notification for read characteristic ($readProperties)"))
      return
    }
    Timber.tag(TAG).d("writing read characteristic descriptor")
    if (!gatt.writeDescriptor(readDescriptor)) {
      onSerialConnectError(IOException("read characteristic CCCD descriptor not writable"))
    }
    // continues asynchronously in onDescriptorWrite()
  }

  override fun onDescriptorWrite(
    gatt: BluetoothGatt,
    descriptor: BluetoothGattDescriptor,
    status: Int
  ) {
    delegate!!.onDescriptorWrite(gatt, descriptor, status)
    if (canceled) return
    if (descriptor.characteristic === readCharacteristic) {
      Timber.tag(TAG).d("writing read characteristic descriptor finished, status=%s", status)
      if (status != BluetoothGatt.GATT_SUCCESS) {
        onSerialConnectError(IOException("write descriptor failed"))
      } else {
        // onCharacteristicChanged with incoming data can happen after writeDescriptor(ENABLE_INDICATION/NOTIFICATION)
        // before confirmed by this method, so receive data can be shown before device is shown as 'Connected'.
        onSerialConnect()
        connected = true
        Timber.tag(TAG).d("connected")
      }
    }
  }

  //  For android < 12
  @Deprecated("Deprecated in Java")
  override fun onCharacteristicChanged(
    gatt: BluetoothGatt,
    characteristic: BluetoothGattCharacteristic,
  ) {
    if (canceled) return
    delegate!!.onCharacteristicChanged(gatt, characteristic)
    if (canceled) return
    if (characteristic === readCharacteristic) { // NOPMD - test object identity
      val data = readCharacteristic!!.value
      onSerialRead(data)
//      Timber.tag(TAG).d("read, len=%s", data.size)
    }
  }

  override fun onCharacteristicChanged(
    gatt: BluetoothGatt,
    characteristic: BluetoothGattCharacteristic,
    value: ByteArray
  ) {
    if (canceled) return
    delegate!!.onCharacteristicChanged(gatt, characteristic)
    if (canceled) return
    if (characteristic === readCharacteristic) { // NOPMD - test object identity
      onSerialRead(value)
//      Timber.tag(TAG).d("read, len=%s", value.size)
    }
  }


  fun write(data: ByteArray) {
    if (canceled || !connected || writeCharacteristic == null) throw IOException("not connected")
    var data0: ByteArray?
    synchronized(writeBuffer) {
      data0 = if (data.size <= payloadSize) {
        data
      } else {
        Arrays.copyOfRange(data, 0, payloadSize)
      }
      if (!writePending && writeBuffer.isEmpty() && delegate!!.canWrite()) {
        writePending = true
      } else {
        writeBuffer.add(data0)
        Timber.tag(TAG)
          .d("write queued, len=%s", data0!!.size)
        data0 = null
      }
      if (data.size > payloadSize) {
        for (i in 1 until (data.size + payloadSize - 1) / payloadSize) {
          val from = i * payloadSize
          val to = Math.min(from + payloadSize, data.size)
          writeBuffer.add(Arrays.copyOfRange(data, from, to))
          Timber.tag(TAG)
            .d("write queued, len=%s", to - from)
        }
      }
    }
    if (data0 != null) {
      writeCharacteristic!!.value = data0
      if (!gatt!!.writeCharacteristic(writeCharacteristic)) {
        onSerialIoError(IOException("write failed"))
      } else {
        Timber.tag(TAG).d("write started, len=%s", data0!!.size)
      }
    }
    // continues asynchronously in onCharacteristicWrite()
  }

  override fun onCharacteristicWrite(
    gatt: BluetoothGatt,
    characteristic: BluetoothGattCharacteristic,
    status: Int
  ) {
    if (canceled || !connected || writeCharacteristic == null) return
    if (status != BluetoothGatt.GATT_SUCCESS) {
      onSerialIoError(IOException("write failed"))
      return
    }
    delegate!!.onCharacteristicWrite(gatt, characteristic, status)
    if (canceled) return
    if (characteristic === writeCharacteristic) { // NOPMD - test object identity
      Timber.tag(TAG).d("write finished, status=%s", status)
      writeNext()
    }
  }

  private fun writeNext() {
    val data: ByteArray?
    synchronized(writeBuffer) {
      if (!writeBuffer.isEmpty() && delegate!!.canWrite()) {
        writePending = true
        data = writeBuffer.removeAt(0)
      } else {
        writePending = false
        data = null
      }
    }
    if (data != null) {
      writeCharacteristic!!.value = data
      if (!gatt!!.writeCharacteristic(writeCharacteristic)) {
        onSerialIoError(IOException("write failed"))
      } else {
        Timber.tag(TAG).d("write started, len=%s", data.size)
      }
    }
  }

  private fun onSerialConnect() {
    if (listener != null) listener!!.onSerialConnect()
  }

  private fun onSerialConnectError(e: Exception) {
    canceled = true
    if (listener != null) listener!!.onSerialConnectError(e)
  }

  private fun onSerialRead(data: ByteArray) {
    if (listener != null) listener!!.onSerialRead(data)
  }

  private fun onSerialIoError(e: Exception) {
    writePending = false
    canceled = true
    if (listener != null) listener!!.onSerialIoError(e)
  }

  /**
   * device delegates
   */
  private inner class Cc245XDelegate : DeviceDelegate() {
    override fun connectCharacteristics(s: BluetoothGattService): Boolean {
      Timber.tag(TAG).d("service cc254x uart")
      readCharacteristic = s.getCharacteristic(BLUETOOTH_LE_CC254X_CHAR_RW)
      writeCharacteristic = s.getCharacteristic(BLUETOOTH_LE_CC254X_CHAR_RW)
      return true
    }
  }

  private inner class MicrochipDelegate : DeviceDelegate() {
    override fun connectCharacteristics(s: BluetoothGattService): Boolean {
      Timber.tag(TAG).d("service microchip uart")
      readCharacteristic = s.getCharacteristic(BLUETOOTH_LE_MICROCHIP_CHAR_RW)
      writeCharacteristic = s.getCharacteristic(BLUETOOTH_LE_MICROCHIP_CHAR_W)
      if (writeCharacteristic == null) writeCharacteristic = s.getCharacteristic(
        BLUETOOTH_LE_MICROCHIP_CHAR_RW
      )
      return true
    }
  }

  private inner class NrfDelegate : DeviceDelegate() {
    override fun connectCharacteristics(s: BluetoothGattService): Boolean {
      Timber.tag(TAG).d("service nrf uart")
      val rw2 = s.getCharacteristic(BLUETOOTH_LE_NRF_CHAR_RW2)
      val rw3 = s.getCharacteristic(BLUETOOTH_LE_NRF_CHAR_RW3)
      if (rw2 != null && rw3 != null) {
        val rw2prop = rw2.properties
        val rw3prop = rw3.properties
        val rw2write = rw2prop and BluetoothGattCharacteristic.PROPERTY_WRITE != 0
        val rw3write = rw3prop and BluetoothGattCharacteristic.PROPERTY_WRITE != 0
        Timber.tag(TAG).d("characteristic properties $rw2prop/$rw3prop")
        if (rw2write && rw3write) {
          onSerialConnectError(IOException("multiple write characteristics ($rw2prop/$rw3prop)"))
        } else if (rw2write) {
          writeCharacteristic = rw2
          readCharacteristic = rw3
        } else if (rw3write) {
          writeCharacteristic = rw3
          readCharacteristic = rw2
        } else {
          onSerialConnectError(IOException("no write characteristic ($rw2prop/$rw3prop)"))
        }
      }
      return true
    }
  }

  private inner class TelitDelegate : DeviceDelegate() {
    private var readCreditsCharacteristic: BluetoothGattCharacteristic? = null
    private var writeCreditsCharacteristic: BluetoothGattCharacteristic? = null
    private var readCredits = 0
    private var writeCredits = 0
    override fun connectCharacteristics(s: BluetoothGattService): Boolean {
      Timber.tag(TAG).d("service telit tio 2.0")
      readCredits = 0
      writeCredits = 0
      readCharacteristic = s.getCharacteristic(BLUETOOTH_LE_TIO_CHAR_RX)
      writeCharacteristic = s.getCharacteristic(BLUETOOTH_LE_TIO_CHAR_TX)
      readCreditsCharacteristic = s.getCharacteristic(
        BLUETOOTH_LE_TIO_CHAR_RX_CREDITS
      )
      writeCreditsCharacteristic = s.getCharacteristic(
        BLUETOOTH_LE_TIO_CHAR_TX_CREDITS
      )
      if (readCharacteristic == null) {
        onSerialConnectError(IOException("read characteristic not found"))
        return false
      }
      if (writeCharacteristic == null) {
        onSerialConnectError(IOException("write characteristic not found"))
        return false
      }
      if (readCreditsCharacteristic == null) {
        onSerialConnectError(IOException("read credits characteristic not found"))
        return false
      }
      if (writeCreditsCharacteristic == null) {
        onSerialConnectError(IOException("write credits characteristic not found"))
        return false
      }
      if (!gatt!!.setCharacteristicNotification(readCreditsCharacteristic, true)) {
        onSerialConnectError(IOException("no notification for read credits characteristic"))
        return false
      }
      val readCreditsDescriptor = readCreditsCharacteristic!!.getDescriptor(BLUETOOTH_LE_CCCD)
      if (readCreditsDescriptor == null) {
        onSerialConnectError(IOException("no CCCD descriptor for read credits characteristic"))
        return false
      }
      readCreditsDescriptor.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
      Timber.tag(TAG).d("writing read credits characteristic descriptor")
      if (!gatt!!.writeDescriptor(readCreditsDescriptor)) {
        onSerialConnectError(IOException("read credits characteristic CCCD descriptor not writable"))
        return false
      }
      Timber.tag(TAG).d("writing read credits characteristic descriptor")
      return false
      // continues asynchronously in connectCharacteristics2
    }

    override fun onDescriptorWrite(
      g: BluetoothGatt,
      d: BluetoothGattDescriptor,
      status: Int
    ) {
      if (d.characteristic === readCreditsCharacteristic) {
        Timber.tag(TAG)
          .d("writing read credits characteristic descriptor finished, status=%s", status)
        if (status != BluetoothGatt.GATT_SUCCESS) {
          onSerialConnectError(IOException("write credits descriptor failed"))
        } else {
          connectCharacteristics2(g)
        }
      }
      if (d.characteristic === readCharacteristic) {
        Timber.tag(TAG).d("writing read characteristic descriptor finished, status=%s", status)
        if (status == BluetoothGatt.GATT_SUCCESS) {
          readCharacteristic!!.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
          writeCharacteristic!!.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
          grantReadCredits()
          // grantReadCredits includes gatt.writeCharacteristic(writeCreditsCharacteristic)
          // but we do not have to wait for confirmation, as it is the last write of connect phase.
        }
      }
    }

    override fun onCharacteristicChanged(
      g: BluetoothGatt, c: BluetoothGattCharacteristic
    ) {
      if (c === readCreditsCharacteristic) { // NOPMD - test object identity
        val newCredits = readCreditsCharacteristic!!.value[0].toInt()
        synchronized(writeBuffer) { writeCredits += newCredits }
        Timber.tag(TAG).d("got write credits +$newCredits =$writeCredits")
        if (!writePending && !writeBuffer.isEmpty()) {
          Timber.tag(TAG).d("resume blocked write")
          writeNext()
        }
      }
      if (c === readCharacteristic) { // NOPMD - test object identity
        grantReadCredits()
        Timber.tag(TAG).d("read, credits=%s", readCredits)
      }
    }

    override fun onCharacteristicWrite(
      g: BluetoothGatt, c: BluetoothGattCharacteristic, status: Int
    ) {
      if (c === writeCharacteristic) { // NOPMD - test object identity
        synchronized(writeBuffer) { if (writeCredits > 0) writeCredits -= 1 }
        Timber.tag(TAG).d("write finished, credits=%s", writeCredits)
      }
      if (c === writeCreditsCharacteristic) { // NOPMD - test object identity
        Timber.tag(TAG).d("write credits finished, status=%s", status)
      }
    }

    override fun canWrite(): Boolean {
      if (writeCredits > 0) return true
      Timber.tag(TAG).d("no write credits")
      return false
    }

    override fun disconnect() {
      readCreditsCharacteristic = null
      writeCreditsCharacteristic = null
    }

    private fun grantReadCredits() {
      val minReadCredits = 16
      val maxReadCredits = 64
      if (readCredits > 0) readCredits -= 1
      if (readCredits <= minReadCredits) {
        val newCredits = maxReadCredits - readCredits
        readCredits += newCredits
        val data = byteArrayOf(newCredits.toByte())
        Timber.tag(TAG).d("grant read credits +$newCredits =$readCredits")
        writeCreditsCharacteristic!!.value = data
        if (!gatt!!.writeCharacteristic(writeCreditsCharacteristic)) {
          if (connected) onSerialIoError(IOException("write read credits failed")) else onSerialConnectError(
            IOException("write read credits failed")
          )
        }
      }
    }
  }
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
  SDK_INT >= 33 ->
    getParcelableExtra(key, T::class.java)

  else ->
    getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
  SDK_INT >= 33 ->
    getParcelable(key, T::class.java)

  else ->
    getParcelable(key) as? T
}