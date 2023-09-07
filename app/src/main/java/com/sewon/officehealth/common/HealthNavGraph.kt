package com.sewon.officehealth.common

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.sewon.officehealth.data.HealthDataStore
import com.sewon.officehealth.screen.singleview.TermAgreement
import com.sewon.officehealth.screen.singleview.SplashScreen




@Composable
fun HealthNavGraph(
    modifier: Modifier = Modifier,
    finishActivity: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.MAIN_ROUTE,
    showOnboardingInitially: Boolean = false
) {



    val mainStartDestination = MainDestinations.ACTIVITY_ROUTE

    val context = LocalContext.current
    val store = HealthDataStore(context)
    val isAccepted = store.getIsTermAgreementAccepted.collectAsState(initial = false)

    val onboardingComplete = remember(showOnboardingInitially) {
        mutableStateOf(!showOnboardingInitially)
    }

    val actions = remember(navController) { MainActions(navController) }
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        var redirectRoute = AppDestinations.TERM_AGREEMENT_ROUTE
        if (isAccepted.value) {
            redirectRoute = MainDestinations.ACTIVITY_ROUTE
        }

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

        composable(AppDestinations.TERM_AGREEMENT_ROUTE) {
            TermAgreement(
                navController = navController,
                redirectRoute = MainDestinations.ACTIVITY_ROUTE,

            )
        }

        navigation(
            route = AppDestinations.MAIN_ROUTE,
            startDestination = mainStartDestination
        ) {
            mainNavGraph(
                onCourseSelected = actions.openCourse,
                onboardingComplete = onboardingComplete,
                navController = navController,
                modifier = modifier
            )
        }
//        composable(
//            "${MainDestinations.COURSE_DETAIL_ROUTE}/{$COURSE_DETAIL_ID_KEY}",
//            arguments = listOf(
//                navArgument(COURSE_DETAIL_ID_KEY) { type = NavType.LongType }
//            )
//        ) { backStackEntry: NavBackStackEntry ->
//            val arguments = requireNotNull(backStackEntry.arguments)
//            val currentCourseId = arguments.getLong(COURSE_DETAIL_ID_KEY)
//            CourseDetails(
//                courseId = currentCourseId,
//                selectCourse = { newCourseId ->
//                    actions.relatedCourse(newCourseId, backStackEntry)
//                },
//                upPress = { actions.upPress(backStackEntry) }
//            )
//        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val onboardingComplete: () -> Unit = {
        navController.popBackStack()
    }

    // Used from COURSES_ROUTE
    val openCourse = { newCourseId: Long, from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.USER_ROUTE}/$newCourseId")
        }
    }

    // Used from COURSE_DETAIL_ROUTE
    val relatedCourse = { newCourseId: Long, from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.REPORT_ROUTE}/$newCourseId")
        }
    }

    // Used from COURSE_DETAIL_ROUTE
    val upPress: (from: NavBackStackEntry) -> Unit = { from ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigateUp()
        }
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

