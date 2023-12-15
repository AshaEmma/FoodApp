plugins {
    id("com.android.application")
}

android {
    namespace = "com.cs407.zoomfoods"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cs407.zoomfoods"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    val room_version = "2.6.1"
    val nav_version = "2.7.5"

    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.mikhaellopez:circularprogressbar:3.1.0")
    implementation("com.android.volley:volley:1.2.1")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-guava:$room_version")
    implementation("com.google.guava:guava:32.1.3-android")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}