package com.sewon.officehealth.service.ble

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.compose.runtime.mutableLongStateOf
import androidx.core.app.NotificationCompat
import com.sewon.officehealth.R
import timber.log.Timber
import java.io.IOException
import java.util.ArrayDeque

/**
 * create notification and queue serial data while activity is not in the foreground
 * use listener chain: SerialSocket -> SerialService -> UI fragment
 */
class SerialService : Service(), SerialListener {
  inner class SerialBinder : Binder() {
    val service: SerialService
      get() = this@SerialService
  }

  private enum class QueueType {
    Connect, ConnectError, Read, IoError
  }

  private class QueueItem {
    var type: QueueType
    var datas: ArrayDeque<ByteArray>? = null
    var e: Exception? = null

    internal constructor(type: QueueType) {
      this.type = type
      if (type == QueueType.Read) init()
    }

    internal constructor(type: QueueType, e: Exception?) {
      this.type = type
      this.e = e
    }

    internal constructor(type: QueueType, datas: ArrayDeque<ByteArray>?) {
      this.type = type
      this.datas = datas
    }

    fun init() {
      datas = ArrayDeque()
    }

    fun add(data: ByteArray) {
      datas!!.add(data)
    }
  }

  private val mainLooper: Handler
  private val binder: IBinder
  private val queue1: ArrayDeque<QueueItem>
  private val queue2: ArrayDeque<QueueItem>
  private val lastRead: QueueItem
  private var socket: SerialSocket? = null
  private var listener: SerialListener? = null
  private var connected = false
  private val a = 10000L
  private val b = 1000L
  val c = mutableLongStateOf(a)

  init {
    mainLooper = Handler(Looper.getMainLooper())
    binder = SerialBinder()
    queue1 = ArrayDeque()
    queue2 = ArrayDeque()
    lastRead = QueueItem(QueueType.Read)
  }

  private val timer = object : CountDownTimer(a, b) {
    override fun onTick(millisUntilFinished: Long) {
      Timber.tag("MYLOG").d("text updated programmatically")
      c.longValue = millisUntilFinished
    }

    override fun onFinish() {
      c.longValue = 0
    }
  }


  override fun onDestroy() {
    cancelNotification()
    disconnect()
    super.onDestroy()
  }

  override fun onBind(intent: Intent): IBinder {
    return binder
  }

  @Throws(IOException::class)
  fun connect(socket: SerialSocket) {
    timer.start()
    Timber.tag("Timber").w("Connect SerialSocket")
    socket.connect(this)
    this.socket = socket
    connected = true
  }

  fun disconnect() {
    connected = false // ignore data,errors while disconnecting
    cancelNotification()
    if (socket != null) {
      socket!!.disconnect()
      socket = null
    }
  }

  @Throws(IOException::class)
  fun write(data: ByteArray?) {
    if (!connected) throw IOException("not connected")
    if (data != null) {
      socket!!.write(data)
    }
  }

  fun attach(listener: SerialListener) {

    require(Looper.getMainLooper().thread === Thread.currentThread()) { "not in main thread" }
    cancelNotification()
    // use synchronized() to prevent new items in queue2
    // new items will not be added to queue1 because mainLooper.post and attach() run in main thread
    synchronized(this) { this.listener = listener }
    for (item in queue1) {
      when (item.type) {
        QueueType.Connect -> listener.onSerialConnect()
        QueueType.ConnectError -> item.e?.let { listener.onSerialConnectError(it) }
        QueueType.Read -> item.datas?.let { listener.onSerialRead(it) }
        QueueType.IoError -> item.e?.let { listener.onSerialIoError(it) }
      }
    }
    for (item in queue2) {
      when (item.type) {
        QueueType.Connect -> listener.onSerialConnect()
        QueueType.ConnectError -> item.e?.let { listener.onSerialConnectError(it) }
        QueueType.Read -> item.datas?.let { listener.onSerialRead(it) }
        QueueType.IoError -> item.e?.let { listener.onSerialIoError(it) }
      }
    }
    queue1.clear()
    queue2.clear()
  }

  fun detach() {
    if (connected) createNotification()
    // items already in event queue (posted before detach() to mainLooper) will end up in queue1
    // items occurring later, will be moved directly to queue2
    // detach() and mainLooper.post run in the main thread, so all items are caught
    listener = null
  }

  fun createNotificationHealth() {
    val notificationChannel = NotificationChannel(
      Constants.NOTIFICATION_CHANNEL,
      "Background service", NotificationManager.IMPORTANCE_LOW
    )
    notificationChannel.setShowBadge(false)
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(notificationChannel)
    val restartIntent = Intent()
      .setClassName(this, Constants.INTENT_CLASS_MAIN_ACTIVITY)
      .setAction(Intent.ACTION_MAIN)
      .addCategory(Intent.CATEGORY_LAUNCHER)
    val flags = PendingIntent.FLAG_IMMUTABLE
    val restartPendingIntent = PendingIntent.getActivity(this, 1, restartIntent, flags)
    val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL)
      .setSmallIcon(R.drawable.ic_bluetooth_searching)
      .setContentTitle(resources.getString(R.string.app_name))
      .setContentText(if (socket != null) "Connected to " + socket!!.name else "Background Service")
      .setContentIntent(restartPendingIntent)
      .setOngoing(true)
    val notification = builder.build()
    notificationManager.notify(5, notification)
  }

  private fun cancelNotification() {
    stopForeground(true)
  }

  override fun onSerialConnect() {
    if (connected) {
      synchronized(this) {
        if (listener != null) {
          mainLooper.post {
            if (listener != null) {
              listener!!.onSerialConnect()
            } else {
              queue1.add(QueueItem(QueueType.Connect))
            }
          }
        } else {
          queue2.add(QueueItem(QueueType.Connect))
        }
      }
    }
  }


  override fun onSerialConnectError(e: Exception) {
    if (connected) {
      synchronized(this) {
        if (listener != null) {
          mainLooper.post {
            if (listener != null) {
              listener!!.onSerialConnectError(e)
            } else {
              queue1.add(
                QueueItem(
                  QueueType.ConnectError,
                  e
                )
              )
              disconnect()
            }
          }
        } else {
          queue2.add(
            QueueItem(
              QueueType.ConnectError,
              e
            )
          )
          disconnect()
        }
      }
    }
  }

  override fun onSerialRead(datas: ArrayDeque<ByteArray>) {
    throw UnsupportedOperationException()
  }

  /**
   * reduce number of UI updates by merging data chunks.
   * Data can arrive at hundred chunks per second, but the UI can only
   * perform a dozen updates if receiveText already contains much text.
   *
   *
   * On new data inform UI thread once (1).
   * While not consumed (2), add more data (3).
   */
  override fun onSerialRead(data: ByteArray) {
    //  TODO: Check data
    if (connected) {
      synchronized(this) {
        if (listener != null) {
          var first: Boolean
          synchronized(lastRead) {
            first = lastRead.datas!!.isEmpty() // (1)
            lastRead.add(data) // (3)
          }
          if (first) {
            mainLooper.post {
              var datas: ArrayDeque<ByteArray>?
              synchronized(lastRead) {
                datas = lastRead.datas
                lastRead.init() // (2)
              }
              if (listener != null) {
                datas?.let { listener!!.onSerialRead(it) }
              } else {
                queue1.add(
                  QueueItem(
                    QueueType.Read,
                    datas
                  )
                )
              }
            }
          }
        } else {
          if (queue2.isEmpty() || queue2.last.type != QueueType.Read) queue2.add(
            QueueItem(
              QueueType.Read
            )
          )
          queue2.last.add(data)
        }
      }
    }
  }

  override fun onSerialIoError(e: Exception) {
    if (connected) {
      synchronized(this) {
        if (listener != null) {
          mainLooper.post {
            if (listener != null) {
              listener!!.onSerialIoError(e)
            } else {
              queue1.add(
                QueueItem(
                  QueueType.IoError,
                  e
                )
              )
              disconnect()
            }
          }
        } else {
          queue2.add(
            QueueItem(
              QueueType.IoError,
              e
            )
          )
          disconnect()
        }
      }
    }
  }

  private fun createNotification() {
    val notificationChannel = NotificationChannel(
      Constants.NOTIFICATION_CHANNEL,
      "Background service", NotificationManager.IMPORTANCE_LOW
    )
    notificationChannel.setShowBadge(false)
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(notificationChannel)
    val disconnectIntent = Intent().setAction(Constants.INTENT_ACTION_DISCONNECT)
    val restartIntent = Intent()
      .setClassName(this, Constants.INTENT_CLASS_MAIN_ACTIVITY)
      .setAction(Intent.ACTION_MAIN)
      .addCategory(Intent.CATEGORY_LAUNCHER)
    val flags = PendingIntent.FLAG_IMMUTABLE
    val disconnectPendingIntent = PendingIntent.getBroadcast(this, 1, disconnectIntent, flags)
    val restartPendingIntent = PendingIntent.getActivity(this, 1, restartIntent, flags)
    val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL)
      .setSmallIcon(R.drawable.ic_bluetooth_searching)
      .setColor(resources.getColor(R.color.black))
      .setContentTitle(resources.getString(R.string.app_name))
      .setContentText(if (socket != null) "Connected to " + socket!!.name else "Background Service")
      .setContentIntent(restartPendingIntent)
      .setOngoing(true)
      .addAction(
        NotificationCompat.Action(
          R.drawable.ic_intelli,
          "Disconnect",
          disconnectPendingIntent
        )
      )
    // @drawable/ic_notification created with Android Studio -> New -> Image Asset using @color/colorPrimaryDark as background color
    // Android < API 21 does not support vectorDrawables in notifications, so both drawables used here, are created as .png instead of .xml
    val notification = builder.build()
    startForeground(Constants.NOTIFY_MANAGER_START_FOREGROUND_SERVICE, notification)
  }
}