package co.id.klikacara.main.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.*
import co.id.klikacara.`object`.adapter.BannerAdapter
import co.id.klikacara.`object`.adapter.KlikMenuAdapter
import co.id.klikacara.`object`.adapter.MitraAdapter
import co.id.klikacara.`object`.adapter.UlasanAdapter
import co.id.klikacara.`object`.authentication.Role
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.base.utils.SchedulersFacade
import co.id.klikacara.main.contract.MainContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class HomePresenter(
    private val view: MainContract.HomeView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val schedulersFacade: SchedulersFacade
) : BasePresenter() {

    private var bannerDisposable: Disposable? = null


    fun getBanner() {
        database.collection(BuildConfig.bannerDb).get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val bannerList = it.result!!.toObjects(Banner::class.java)
                    val bannerListAdapter = ArrayList<BannerAdapter>()
                    for (banner in bannerList)
                        bannerListAdapter.add(BannerAdapter(banner))
                    view.setBannerAdapter(bannerListAdapter)
                }
            }
    }

    fun sendMenuToRemote(menuList: ArrayList<KlikMenu>) {
        val reference = database.collection(BuildConfig.klikMenuDb)
        for (menu in menuList) {
            menu.key = reference.document().id
            reference.document(menu.key!!).set(menu)
        }
    }

    fun getMenuByType(mitraType: MitraType) {
        database.collection(BuildConfig.klikMenuDb)
            .whereEqualTo("mitraType", mitraType.toString())
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val klikMenuList = it.result!!.toObjects(KlikMenu::class.java)
                    val klikMenuListAdapter = ArrayList<KlikMenuAdapter>()
                    for (klikMenu in klikMenuList)
                        klikMenuListAdapter.add(KlikMenuAdapter(klikMenu))
                    setMenuToView(mitraType, klikMenuListAdapter)
                }
            }
    }

    private fun setMenuToView(mitraType: MitraType, klikMenuListAdapter: ArrayList<KlikMenuAdapter>) {
        when (mitraType) {
            MitraType.PERLENGKAPAN_ACARA -> {
                view.setPerlengkapanAcaraAdapter(klikMenuListAdapter)
            }
            MitraType.PAKET_ACARA -> {
                view.setPaketAcaraAdapter(klikMenuListAdapter)
            }
            MitraType.PENGISI_ACARA -> {
                view.setPengisiAcaraAdapter(klikMenuListAdapter)
            }
        }
    }

    fun initMenu() {
        val listMenu = ArrayList<KlikMenu>()
        listMenu.add(
            KlikMenu(
                null,
                "Penari",
                "Sekarang mencari penari untuk acara Anda sangat mudah dengan Klik Acara.",
                MitraType.PENGISI_ACARA
            )
        )
        listMenu.add(
            KlikMenu(
                null,
                "Penyanyi",
                "Sekarang mencari penyanyi di acara Anda sangat mudah di Klik Acara",
                MitraType.PENGISI_ACARA
            )
        )
        listMenu.add(
            KlikMenu(
                null,
                "Handy Talky",
                "Sekarang memesan handy talky untuk acara Anda sangat mudah di Klik Acara",
                MitraType.PERLENGKAPAN_ACARA
            )
        )
        listMenu.add(
            KlikMenu(
                null,
                "Lighting",
                "Sekarang lighting untuk acara Anda sangat mudah di Klik Acara",
                MitraType.PERLENGKAPAN_ACARA
            )
        )
        listMenu.add(
            KlikMenu(
                null,
                "Sound System",
                "Sekarang memesan sound system untuk acara Anda sangat mudah di Klik Acara",
                MitraType.PERLENGKAPAN_ACARA
            )
        )
        listMenu.add(
            KlikMenu(
                null,
                "Tenda",
                "Sekarang mencari tenda acara sangat mudah di Klik Acara",
                MitraType.PERLENGKAPAN_ACARA
            )
        )
        sendMenuToRemote(listMenu)
    }

    fun initUlasan() {
        val listUlasan = ArrayList<Ulasan>()
        listUlasan.add(
            Ulasan(
                null,
                "Arloji",
                "",
                Role.VENDOR,
                "Terimakasih klikacara, berkat klikacara kini pemesanan alat ditempat saya jadi lebih ramai"
            )
        )
        listUlasan.add(
            Ulasan(
                null,
                "Luthfi",
                "",
                Role.PENGGUNA,
                "Bikin acara gampang banget dah, thanks klikacara. Sukses selalu!"
            )
        )

        listUlasan.add(
            Ulasan(
                null,
                "Tari",
                "",
                Role.PENGGUNA,
                "Pelayanan ramah, vendornya juga terpercaya. Ga nyesel deh bikin acara dari klikacara, ntar gw pesen lagi buat pensinya."
            )
        )

        listUlasan.add(
            Ulasan(
                null,
                "TADI Alat Pesta",
                "",
                Role.VENDOR,
                "Awalnya saya iseng-iseng aja daftar jadi vendor, ternyata banyak yang mesen peralatan saya dari aplikasi ini. Sangat membantu"
            )
        )

        val reference = database.collection(BuildConfig.ulasanDb)
        for (ulasan in listUlasan) {
            ulasan.key = reference.document().id
            reference.document(ulasan.key!!).set(ulasan)
        }

    }

    fun getUlasan() {
        database.collection(BuildConfig.ulasanDb)
            .orderBy("createdDate", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val ulasanList = it.result!!.toObjects(Ulasan::class.java)
                    val ulasanListAdapter = ArrayList<UlasanAdapter>()
                    for (ulasan in ulasanList)
                        ulasanListAdapter.add(UlasanAdapter(ulasan))
                    view.setUlasanAdapter(ulasanListAdapter)
                }
            }
    }

    fun getMitra() {
        database.collection(BuildConfig.userDb)
            .whereEqualTo("type", Role.VENDOR.toString())
            .limit(5)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val mitraList = it.result!!.toObjects(User::class.java)
                    val mitraListAdapter = ArrayList<MitraAdapter>()
                    for (mitra in mitraList)
                        mitraListAdapter.add(MitraAdapter(mitra))
                    view.setMitraAdapter(mitraListAdapter)
                }
            }
    }

    fun sendProvinceRemote(listProvince: List<MasterData>) {
        for (master in listProvince) {
            database.collection(BuildConfig.districtDb).document(master.key).set(master)
        }
    }

    fun runningPager() {
        bannerDisposable = Observable.interval(2, TimeUnit.SECONDS)
            .subscribeOn(schedulersFacade.io())
            .observeOn(schedulersFacade.ui())
            .subscribe({
                view.changeBanner()
            }, {
                stopPager()
            })
        addDisposable(bannerDisposable!!)
    }

    fun stopPager() {
        if (bannerDisposable != null)
            bannerDisposable?.dispose()
    }

}