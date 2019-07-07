package co.id.klikacara.di.module

import android.content.Context
import co.id.klikacara.KlikApplication
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class AppModule {
    @Named("ApplicationContext")
    @Provides
    @Singleton
    internal fun provideContext(application: KlikApplication): Context {
        return application.applicationContext
    }
}