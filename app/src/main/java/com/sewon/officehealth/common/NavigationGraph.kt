package com.sewon.officehealth.common

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sewon.officehealth.R
import com.sewon.officehealth.screen.activity.ScreenActivity
import com.sewon.officehealth.screen.device.DeviceList
import com.sewon.officehealth.screen.splash.SplashScreen
import com.sewon.officehealth.screen.welcome.WelcomeScreen


@Composable
fun NavigationGraph(
  modifier: Modifier = Modifier,
  finishActivity: () -> Unit = {},
  navController: NavHostController = rememberNavController(),
  startDestination: String = AppDestinations.DEVICE_ROUTE,
  showOnboardingInitially: Boolean = false
) {


  val context = LocalContext.current

  val onboardingComplete = remember(showOnboardingInitially) {
    mutableStateOf(!showOnboardingInitially)
  }

  NavHost(
    navController = navController,
    startDestination = startDestination
  ) {
    var redirectRoute = AppDestinations.DEVICE_ROUTE


    composable(AppDestinations.SPLASH_ROUTE) {
      // Intercept back in Onboarding: make it finish the activity
      BackHandler {
        finishActivity()
      }
      SplashScreen(
        navController = navController,
        redirectRoute = redirectRoute
      )
    }

    composable(AppDestinations.WELCOME_ROUTE) {
      WelcomeScreen(
        navController = navController,
      )
    }

    composable(AppDestinations.DEVICE_ROUTE) {
      DeviceList(
        navController = navController
      )
    }

    composable(AppDestinations.ACTIVITY_ROUTE) {
      ScreenActivity(

      )
    }
  }
}


private fun NavBackStackEntry.lifecycleIsResumed() =
  this.lifecycle.currentState == Lifecycle.State.RESUMED


object AppDestinations {
  const val SPLASH_ROUTE = "splash_route"
  const val WELCOME_ROUTE = "welcome_route"
  const val DEVICE_ROUTE = "device_route"
  const val ACTIVITY_ROUTE = "activity_route"

}

enum class MainTabs(
  @StringRes val title: Int,
  @DrawableRes val icon: Int,
  val route: String
) {
  SPLASH(R.string.splash_screen, R.drawable.ic_menu_camera, AppDestinations.SPLASH_ROUTE),
  DEVICE(R.string.device_list, R.drawable.ic_menu_camera, AppDestinations.DEVICE_ROUTE),
  ACTIVITY(R.string.activity_page, R.drawable.ic_menu_camera, AppDestinations.ACTIVITY_ROUTE),
}