package com.sewon.officehealth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class BroadCastActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      // on below line we are specifying
      // background color for our application
      Surface(
        modifier = Modifier.fillMaxSize(),
      ) {
        // on below line we are specifying theme as scaffold.
        Scaffold(
          // in scaffold we are specifying top bar.
          topBar = {
            // inside top bar we are specifying background color.
            TopAppBar(
              // along with that we are specifying title for our top bar.
              title = {
                // in the top bar we are specifying tile as a text
                Text(
                  // on below line we are specifying text
                  // to display in top app bar.
                  text = "Local Broad Cast Manager",
                  // on below line we are specifying modifier
                  // to fill max width.
                  modifier = Modifier.fillMaxWidth(),
                  // on below line we are specifying text alignment.
                  textAlign = TextAlign.Center,
                  // on below line we are specifying color for our text.
                  color = Color.White
                )
              })
          }) { padding ->
          // on below line we are calling custom list
          // view function to display custom listview.
          Column(modifier = Modifier.padding(padding)) {
            BroadcastManager(LocalContext.current)
          }
        }
      }
    }
  }
}

@Composable
fun BroadcastManager(ctx: Context) {
  // on below line we are creating a variable for broad cast manager status.
  val broadCastMsg = remember {
    mutableStateOf("Welcome")
  }
  // on below line we are creating a new broad cast manager.
  val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
    // we will receive data updates in onReceive method.
    override fun onReceive(context: Context?, intent: Intent) {
      // Get extra data included in the Intent
      val message = intent.getStringExtra("message")
      // on below line we are updating the data in our text view.
      broadCastMsg.value = message!!
    }
  }
  // on below line we are registering our local broadcast manager.
  LocalBroadcastManager.getInstance(ctx).registerReceiver(
    broadcastReceiver, IntentFilter("custom-action-local-broadcast")
  )
  // on below line we are creating a column
  Column(
    // on below line we are specifying modifier
    // and setting max height and max width for our column
    modifier = Modifier
      .fillMaxSize()
      .fillMaxHeight()
      .fillMaxWidth()
      // on below line we are
      // adding padding for our column
      .padding(5.dp),
    // on below line we are specifying horizontal
    // and vertical alignment for our column
    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
  ) {
    // on below line we are creating a simple text for displaying a text
    Text(
      text = "Local Broad Cast Manager in Android",
      textAlign = TextAlign.Center,
      fontWeight = FontWeight.Bold,
      fontSize = 20.sp,
    )
    // on below line we are adding a spacer.
    Spacer(modifier = Modifier.height(20.dp))
    // on below line we are creating a text to display broad cast manager message.
    Text(
      text = broadCastMsg.value,
      textAlign = TextAlign.Center,
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(4.dp)
    )
    // on below line we are adding a spacer.
    Spacer(modifier = Modifier.height(20.dp))
    // on below line we are creating a button for sending broadcast message.
    Button(onClick = {
      // inside on click we are calling the intent with the action.
      val intent = Intent("custom-action-local-broadcast")
      // on below line we are passing data to our broad cast receiver with key and value pair.
      intent.putExtra("message", "Welcome\nto\nGeeks For Geeks")
      // on below line we are sending our broad cast with intent using broad cast manager.
      LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent)
    }) {
      // on below line we are specifying text for our button.
      Text(text = "Send Broadcast")
    }
  }
}