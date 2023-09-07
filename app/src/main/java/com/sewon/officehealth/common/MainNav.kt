
package com.sewon.officehealth.common

import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.sewon.officehealth.screen.activity.SleepActivity
import com.sewon.officehealth.screen.report.Report
import com.sewon.officehealth.screen.setting.UserSetting

fun NavGraphBuilder.mainNavGraph(
    onCourseSelected: (Long, NavBackStackEntry) -> Unit,
    onboardingComplete: State<Boolean>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    composable(MainTabs.ACTIVITY.route) { backStackEntry ->
//        val parentEntry = remember(backStackEntry) {
//            navController.getBackStackEntry("parentNavigationRoute")
//        }

//        val parentViewModel = hiltViewModel(parentEntry)


        SleepActivity(modifier)
        // Show onboarding instead if not shown yet.
//        LaunchedEffect(onboardingComplete) {
//            if (!onboardingComplete.value) {
//                navController.navigate(AppDestinations.SPLASH_ROUTE)
//            }
//        }
//        if (onboardingComplete.value) { // Avoid glitch when showing onboarding
//            FeaturedCourses(
//                courses = courses,
//                selectCourse = { id -> onCourseSelected(id, from) },
//                modifier = modifier
//            )
//        }

    }

    composable(MainTabs.REPORT.route) { from ->
        Report(modifier)
//        MyCourses(
//            courses = courses,
//            { id -> onCourseSelected(id, from) },
//            modifier
//        )
    }

    composable(MainTabs.USER.route) {
        UserSetting(modifier)
    }

//    composable(MainTabs.USER.route) { from ->
//        MyCourses(
//            courses = courses,
//            { id -> onCourseSelected(id, from) },
//            modifier
//        )
//    }

}




