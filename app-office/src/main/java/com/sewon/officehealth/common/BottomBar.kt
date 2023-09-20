package com.sewon.officehealth.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import java.util.Locale

@Composable
fun BottomBar(
  navController: NavController,
  bottomBarState: MutableState<Boolean>,
  tabs: Array<MainTabs>
) {

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route
    ?: MainTabs.ACTIVITY.route

  val routes = remember {
    MainTabs.values().map { it.route }
  }
  if (currentRoute in routes) {
    AnimatedVisibility(
      visible = bottomBarState.value,
      enter = slideInVertically(initialOffsetY = { it }),
      exit = slideOutVertically(targetOffsetY = { it }),
      content = {
        NavigationBar(
          containerColor = Color(0xFF000103),
        ) {
          tabs.forEach { tab ->
            NavigationBarItem(
              icon = { Icon(painterResource(tab.icon), contentDescription = null) },
              label = { Text(stringResource(tab.title).uppercase(Locale.getDefault())) },
              selected = currentRoute == tab.route,
              onClick = {
                if (tab.route != currentRoute) {
                  navController.navigate(tab.route) {
                    popUpTo(navController.graph.startDestinationId) {
                      saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                  }
                }
              },
              modifier = Modifier.navigationBarsPadding()
            )
          }
        }
      }
    )
  }

}
