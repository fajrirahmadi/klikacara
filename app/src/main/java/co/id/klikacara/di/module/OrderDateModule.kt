package co.id.klikacara.di.module

import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.presenter.OrderDatePresenter
import dagger.Module
import dagger.Provides

@Module
class OrderDateModule {

    @Provides
    fun provideOrderDatePresenter(
        view: OrderContract.OrderDateView
    ): OrderDatePresenter {
        return OrderDatePresenter(view)
    }
}