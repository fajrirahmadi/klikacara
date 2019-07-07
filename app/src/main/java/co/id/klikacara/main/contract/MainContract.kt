package co.id.klikacara.main.contract

import co.id.klikacara.`object`.adapter.*
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.contract.BaseContract

interface MainContract {

    interface HomeView : BaseContract.View {
        fun showSaldo(saldo: Long)
        fun setBannerAdapter(bannerListAdapter: List<BannerAdapter>)
        fun setPerlengkapanAcaraAdapter(klikMenuListAdapter: ArrayList<KlikMenuAdapter>)
        fun setPaketAcaraAdapter(klikMenuListAdapter: ArrayList<KlikMenuAdapter>)
        fun setPengisiAcaraAdapter(klikMenuListAdapter: ArrayList<KlikMenuAdapter>)
        fun setUlasanAdapter(ulasanListAdapter: ArrayList<UlasanAdapter>)
        fun setMitraAdapter(mitraListAdapter: ArrayList<MitraAdapter>)

    }

    interface OrderView : BaseContract.View {
        fun setOrderListAdapter(orderListAdapter: ArrayList<OrderListAdapter>)
        fun showNotLoginArea()
        fun showLoginArea()
        fun showNoOrderFound()

    }

    interface ProfileView : BaseContract.View {
        fun showNotLoginArea()
        fun showLoginArea()
        fun doOnLogoutSuccess()
        fun setUserData(user: User)
    }

    interface ProfileDetailView : BaseContract.View {
        fun setUserData(user: User)
        fun setMitraData(mitra: Mitra)
        fun submitProfileChange(path: String?)
        fun doOnSumbitUserSuccess()
    }

    interface UserProfileView : BaseContract.View {
        fun setUserData(user: User)
    }

    interface MitraProfileView : BaseContract.View {
        fun setMitraData(mitra: Mitra)
    }
}