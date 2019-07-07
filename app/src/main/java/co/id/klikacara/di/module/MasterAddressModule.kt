package co.id.klikacara.di.module

import co.id.klikacara.master.contract.MasterContract
import co.id.klikacara.master.presenter.MasterAddressPresenter
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class MasterAddressModule {

    @Provides
    fun provideMasterAddressPresenter(
        view: MasterContract.MasterAddressView
    ): MasterAddressPresenter {
        return MasterAddressPresenter(view, FirebaseFirestore.getInstance())
    }
}