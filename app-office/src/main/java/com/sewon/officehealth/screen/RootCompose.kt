package com.sewon.officehealth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sewon.officehealth.screen.a0common.theme.OfficeHealthTheme

@Composable
fun RootCompose(finishActivity: () -> Unit) {

  val navController = rememberNavController()
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

  OfficeHealthTheme() {
    val tabs = remember { MainTabs.values() }

    Scaffold(
    ) { innerPaddingModifier ->
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxSize()
          .background(Color(0xFFD5F3EB))
          .systemBarsPadding()
          .statusBarsPadding(),
      ) {
        NavigationGraph(
          finishActivity = finishActivity,
          navController = navController,
          modifier = Modifier.padding(innerPaddingModifier)
        )
      }
    }
  }
}

