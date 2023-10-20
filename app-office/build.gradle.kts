plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.devtools.ksp") version ("1.8.20-1.0.11")
  id("kotlin-kapt")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.sewon.officehealth"
  compileSdk = 34
  defaultConfig {
    applicationId = "com.sewon.officehealth"
    minSdk = 29
    targetSdk = 34
    versionCode = 3
    versionName = "1.0.2"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.4.6"
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  buildFeatures {
    buildConfig = true
    viewBinding = true
    compose = true
  }
}

dependencies {
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.core.i18n)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.constraintlayout.compose)
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)
  implementation(libs.androidx.preference.ktx)
  implementation(libs.accompanist.permissions)


  // Hilt dependency injection
  implementation(libs.hilt.android)
  // kapt "com.google.dagger:hilt-compiler:2.46.1")
  kapt(libs.androidx.hilt.compiler)
  kapt(libs.hilt.android.compiler)

  implementation(libs.androidx.hilt.navigation.compose)


  // DataStore
  //
  // Preferences DataStore (SharedPreferences like APIs)
  implementation(libs.androidx.datastore.preferences)
  // optional - RxJava3 support
  implementation(libs.androidx.datastore.preferences.rxjava3)

  // UI Compose
  //
  val composeVersion = "1.5.3"
  implementation(libs.androidx.compiler)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.animation)
  implementation(libs.androidx.foundation)
  implementation(libs.androidx.foundation.layout)
  implementation(libs.androidx.material.icons.core)
  implementation(libs.androidx.material.icons.extended)
  implementation(libs.androidx.runtime)
  implementation(libs.androidx.runtime.livedata)
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.util)
  implementation(libs.androidx.ui.text)
  implementation(libs.androidx.ui.tooling)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.ui.viewbinding)
  debugImplementation(libs.androidx.compose.ui.ui.tooling)

  // Log
  //
  implementation(libs.timber)


  // Test
  //
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

}