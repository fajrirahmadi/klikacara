package co.id.klikacara

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import co.id.klikacara.`object`.constanta.DefaultConstanta
import co.id.klikacara.di.component.DaggerAppComponent
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import java.util.*
import javax.inject.Inject


class KlikApplication : MultiDexApplication(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentSupportInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentSupportInjector
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        DaggerAppComponent.builder().application(this).build().inject(this)
        createNotificationChannel()

        FirebaseMessaging.getInstance().subscribeToTopic("EVENT")
        FirebaseMessaging.getInstance().subscribeToTopic("ORDER")

        val remoteConfigDefaults = HashMap<String, Any>()
        remoteConfigDefaults[BuildConfig.isMaintainKey] = false
        remoteConfigDefaults[BuildConfig.currentVersionKey] = BuildConfig.VERSION_NAME
        remoteConfigDefaults[DefaultConstanta.PHONE_CS_KEY] = DefaultConstanta.CS_PHONE
        remoteConfigDefaults[DefaultConstanta.WA_CS_KEY] = DefaultConstanta.CS_PHONE
        remoteConfigDefaults[DefaultConstanta.EMAIL_CS_KEY] = DefaultConstanta.CS_EMAIL


        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)
        firebaseRemoteConfig.fetch(60)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseRemoteConfig.activate()
                }
            }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                name,
                importance
            )
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

}