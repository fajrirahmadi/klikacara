package co.id.klikacara.di.component

import co.id.klikacara.KlikApplication
import co.id.klikacara.di.module.AppModule
import co.id.klikacara.di.module.builder.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        AppModule::class,
        MainBuilderModule::class,
        AuthenticationBuilderModule::class,
        ProductBuilderModule::class,
        OrderBuilderModule::class,
        RegistrationBuilderModule::class,
        VendorBuilderModule::class]
)

interface AppComponent {

    @Component.Builder
    interface Builder {
        // provide Application instance into DI
        @BindsInstance
        fun application(application: KlikApplication): Builder

        fun build(): AppComponent
    }

    fun inject(application: KlikApplication)
}