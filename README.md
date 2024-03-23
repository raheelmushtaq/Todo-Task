# Todo List

**This Project is implemented using.**
* Android Studio.
* Kotlin
* Compose

**Architecture of Project:**
* MVVM with Jetpack Compose.

**Api Calling Libraries**

* Retrofit.
* implementation 'com.squareup.retrofit2:retrofit:2.9.0'
* implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
* implementation 'com.squareup.retrofit2:adapter-rxjava2:2.9.0'
* implementation 'com.squareup.okhttp3:logging-interceptor:4.7.2'

**Storage**

* DataStore


**All Libraries**

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //navigation
    val raamCastaComposeVersion = "1.9.57"
    implementation("io.github.raamcosta.compose-destinations:core:${raamCastaComposeVersion}")
    implementation("io.github.raamcosta.compose-destinations:animations-core:${raamCastaComposeVersion}")
    implementation("io.github.raamcosta.compose-destinations:wear-core:${raamCastaComposeVersion}")
    ksp("io.github.raamcosta.compose-destinations:ksp:${raamCastaComposeVersion}")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

    // Compose dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-compiler:2.48.1")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")


Developer.
Raheel Mushtaq raheelmushtaq41@gmail.com
