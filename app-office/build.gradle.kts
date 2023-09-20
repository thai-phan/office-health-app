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
    versionCode = 1
    versionName = "1.0"

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
  implementation("androidx.activity:activity-compose:1.7.2")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("androidx.annotation:annotation:1.7.0")
  implementation("androidx.core:core-i18n:1.0.0-alpha01")
  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
  implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
  implementation("androidx.navigation:navigation-compose:2.7.2")
  implementation("androidx.navigation:navigation-fragment-ktx:2.7.2")
  implementation("androidx.navigation:navigation-ui-ktx:2.7.2")
  implementation("androidx.preference:preference-ktx:1.2.1")
  implementation("com.google.accompanist:accompanist-permissions:0.32.0")


  // Hilt dependency injection
  implementation("com.google.dagger:hilt-android:2.48")
  // kapt "com.google.dagger:hilt-compiler:2.46.1")
  kapt("androidx.hilt:hilt-compiler:1.0.0")
  kapt("com.google.dagger:hilt-android-compiler:2.48")

  implementation("androidx.hilt:hilt-navigation-compose:1.0.0")


  // DataStore
  //
  // Preferences DataStore (SharedPreferences like APIs)
  implementation("androidx.datastore:datastore-preferences:1.0.0")
  // optional - RxJava3 support
  implementation("androidx.datastore:datastore-preferences-rxjava3:1.0.0")

  // UI Compose
  //
  val composeVersion = "1.5.1"
  implementation("androidx.compose.compiler:compiler:1.5.3")
  implementation("androidx.compose.material3:material3:1.1.1")
  implementation("androidx.compose.animation:animation:$composeVersion")
  implementation("androidx.compose.foundation:foundation:$composeVersion")
  implementation("androidx.compose.foundation:foundation-layout:$composeVersion")
  implementation("androidx.compose.material:material-icons-core:$composeVersion")
  implementation("androidx.compose.material:material-icons-extended:$composeVersion")
  implementation("androidx.compose.runtime:runtime:$composeVersion")
  implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
  implementation("androidx.compose.ui:ui:$composeVersion")
  implementation("androidx.compose.ui:ui-util:$composeVersion")
  implementation("androidx.compose.ui:ui-text:$composeVersion")
  implementation("androidx.compose.ui:ui-tooling:$composeVersion")
  implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
  implementation("androidx.compose.ui:ui-viewbinding:$composeVersion")
  debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")

  // Log
  //
  implementation("com.jakewharton.timber:timber:5.0.1")


  // Test
  //
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}