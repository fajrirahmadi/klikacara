package co.id.klikacara

import android.app.Activity
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.support.v4.app.Fragment
import co.id.klikacara.di.component.DaggerAppComponent
import com.google.firebase.database.FirebaseDatabase
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
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
    }

}