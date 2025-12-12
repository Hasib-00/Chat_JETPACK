plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.chat"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.chat"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)



    /* ----------------------------------------------------
* HILT (Dependency Injection)
* ---------------------------------------------------- */

    kapt("com.google.dagger:hilt-compiler:2.57.2")



    /* ----------------------------------------------------
     * FIREBASE
     * ---------------------------------------------------- */
    // Firebase BOM controls all Firebase versions
    //implementation(platform("com.google.firebase:firebase-bom:34.6.0"))

    //implementation("com.google.firebase:firebase-analytics-ktx")
    //implementation("com.google.firebase:firebase-auth-ktx")
    //implementation("com.google.firebase:firebase-database-ktx")
    //implementation("com.google.firebase:firebase-storage-ktx")
    //implementation("com.google.firebase:firebase-messaging-ktx")

    // Crashlytics (actual library)
    // implementation("com.google.firebase:firebase-crashlytics-ktx:19.4.4")


    /* ----------------------------------------------------
     * UI LIBRARIES
     * ---------------------------------------------------- */
    // Material
    implementation("com.google.android.material:material:1.13.0")

    // Coil
    implementation("io.coil-kt:coil:2.7.0")


    /* ----------------------------------------------------
     * SUPABASE
     * ---------------------------------------------------- */
    // implementation("io.github.jan-tennert.supabase:storage-kt:3.2.6")
    //implementation("io.github.jan-tennert.supabase:compose-auth:3.2.6")


    /* ----------------------------------------------------
     * KTOR HTTP CLIENT
     * ---------------------------------------------------- */
    // val ktorVersion = "2.3.13"
    //implementation("io.ktor:ktor-client-android:$ktorVersion")
    // implementation("io.ktor:ktor-client-core:$ktorVersion")
    // implementation("io.ktor:ktor-utils:$ktorVersion")


    /* ----------------------------------------------------
     * NAVIGATION (Compose + Fragment)
     * ---------------------------------------------------- */
    implementation("androidx.navigation:navigation-compose:2.9.6")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.6")

    // Hilt Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")


    /* ----------------------------------------------------
     * COMPOSE (UI)
     * ---------------------------------------------------- */
    implementation("androidx.activity:activity-compose:1.12.1")
    implementation("androidx.compose.ui:ui:1.10.0")
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.10.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.10.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.1")


    /* ----------------------------------------------------
     * LIFECYCLE (ViewModel, LiveData)
     * ---------------------------------------------------- */
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")


    /* ----------------------------------------------------
     * OTHER LIBRARIES
     * ---------------------------------------------------- */
    // Google Auth
    //implementation("com.google.auth:google-auth-library-oauth2-http:1.41.0")

    // PermissionX
    //implementation("com.guolindev.permissionx:permissionx:1.8.1")

    // ZEGOCLOUD Call Kit (stable version)
    //implementation("com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:1.0.15")

}