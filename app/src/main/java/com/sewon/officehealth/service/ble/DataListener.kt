package com.sewon.officehealth.service.ble

import android.text.SpannableStringBuilder
import timber.log.Timber
import java.util.ArrayDeque

class DataListener : SerialListener {

  private enum class Connected {
    False, Pending, True
  }

  private val deviceAddress: String? = null
  private val service: SerialService? = null

  private val hexWatcher: TextUtil.HexWatcher? = null

  private var connected = Connected.False
  private val initialStart = true
  private val hexEnabled = false
  private var pendingNewline = false
  private val newline: String = TextUtil.newline_crlf
  val receiveText: SpannableStringBuilder = SpannableStringBuilder()


  override fun onSerialConnect() {
    connected = Connected.True
  }

  override fun onSerialConnectError(e: Exception) {
    connected = Connected.False
  }


  override fun onSerialRead(data: ByteArray) {
    val datas = ArrayDeque<ByteArray>()
    datas.add(data)
    receive(datas)
  }

  override fun onSerialRead(datas: ArrayDeque<ByteArray>) {
    if (datas != null) {
      receive(datas)
    }
  }

  override fun onSerialIoError(e: Exception) {
    Timber.tag("Timber").d("onSerialRead")
  }

  private fun receive(datas: ArrayDeque<ByteArray>) {
    val spn = SpannableStringBuilder()
    for (data in datas) {
      if (hexEnabled) {
        spn.append(TextUtil.toHexString(data)).append('\n')
      } else {
        var msg = String(data)
        if (newline == TextUtil.newline_crlf && msg.length > 0) {
          // don't show CR as ^M if directly before LF
          msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf)
          // special handling if CR and LF come in separate fragments
//          if (pendingNewline && msg[0] == '\n') {
//            if (spn.length >= 2) {
//              spn.delete(spn.length - 2, spn.length)
//            } else {
//              val edt: Editable = receiveText.getEditableText()
//              if (edt != null && edt.length >= 2) edt.delete(edt.length - 2, edt.length)
//            }
//          }
          pendingNewline = msg[msg.length - 1] == '\r'
        }
        spn.append(TextUtil.toCaretString(msg, newline.length != 0))
      }
    }
    receiveText.append(spn)
  }
}