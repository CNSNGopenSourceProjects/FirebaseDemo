package br.com.conseng.firebasedemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

/**
 * On this Demo App I will test the Google Firebase functionality, such as:
 * - push messages to the user.
 * To be able to implement the Firebase following changes were necessary on:
 * (1) Enable Google Sign-in on Project Sctructure/Authentication.
 * (2) Create the application key.
 * @see [https://developer.android.com/studio/publish/app-signing.html#manage-key]
 * (3) Sign the APK.
 * @see [https://developer.android.com/studio/publish/app-signing.html#sign-apk]
 * (4) Remove the APK key from Build.Gradle (App)
 * @see [https://developer.android.com/studio/publish/app-signing.html#secure-shared-keystore]
 * - Manifest:
 * - Build.Gradle (Project)
 * - Build.Gradle (App)
 *
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
