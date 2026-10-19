import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

// Read optional overrides from local.properties (never committed), falling back to
// gradle.properties. Used for the API base URL and the Google web client ID.
val localProps = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

fun setting(key: String, default: String): String =
    localProps.getProperty(key) ?: (project.findProperty(key) as String?) ?: default

android {
    namespace = "com.harvestprintingcentre.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.harvestprintingcentre.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "2.0"

        buildConfigField("String", "API_BASE_URL", "\"${setting("harvest.apiBaseUrl", "")}\"")
        buildConfigField("String", "WEB_CLIENT_ID", "\"${setting("harvest.webClientId", "")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    testOptions {
        // Lets JVM unit tests call android.util.Log (Log.d is a no-op there) without crashing.
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    // --- AndroidX / UI ---
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.activity:activity-ktx:1.9.3")

    // --- Navigation, lifecycle ---
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // --- Coroutines (play-services adds Task.await() for Firebase) ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")

    // --- Firebase Authentication (email/password + Google) ---
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-auth")

    // --- Google sign-in through Credential Manager ---
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // --- Retrofit (API client) ---
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.11.0")

    // --- Unit tests (run on the JVM, no phone needed) ---
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}
