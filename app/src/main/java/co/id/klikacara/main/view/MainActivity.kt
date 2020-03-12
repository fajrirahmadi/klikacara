package co.id.klikacara.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.MitraType
import co.id.klikacara.`object`.adapter.*
import co.id.klikacara.`object`.authentication.Role
import co.id.klikacara.`object`.authentication.User
import co.id.klikacara.authentication.view.AuthenticationActivity
import co.id.klikacara.base.utils.imagehelper.GlideUtils
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.base.utils.updatehelper.ForceUpdateChecker
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.base.view.adapter.ViewPagerAdapter
import co.id.klikacara.createevent.view.CreateEventActivity
import co.id.klikacara.createevent.view.DetailEventActivity
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.HomePresenter
import co.id.klikacara.product.view.MyProductActivity
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.sajaksenja.search.SearchEventActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_main_home.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.HomeView, SwipeRefreshLayout.OnRefreshListener,
    ForceUpdateChecker.OnUpdateNeededListener {

    @Inject
    lateinit var homePresenter: HomePresenter
    private var user: User? = null

    private val perlengkapanAcaraAdapter = FastItemAdapter<KlikMenuAdapter>()
    private val paketAcaraAdapter = FastItemAdapter<KlikMenuAdapter>()
    private val pengisiAcaraAdapter = FastItemAdapter<KlikMenuAdapter>()
    private val crewAcaraAdapter = FastItemAdapter<KlikMenuAdapter>()
    private val ulasanAdapter = FastItemAdapter<UlasanAdapter>()
    private val mitraAdapter = FastItemAdapter<MitraAdapter>()
    private val eventAdapter = FastItemAdapter<EventAdapter>()
    private lateinit var pagerAdapter: ViewPagerAdapter
    private var marginVeryLarge: Int = 48
    private var marginMedium: Int = 16
    private var isLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_main)
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check()
        swipeHome.setOnRefreshListener(this)
        configureAdapter()
        configureViewPager()
        onRefresh()
    }

    override fun doOnUserLogin() {
        isLogin = true
        ViewHelper.showView(createEventOrProductButton)
    }

    override fun doOnUserNotLogin() {
        isLogin = false
        ViewHelper.hideView(createEventOrProductButton)
    }

    override fun setUserData(user: User) {
        ViewHelper.showView(createEventOrProductButton)
        this.user = user
        labelGreetingTextView.text =
            StringHelper.getStringBuilderToString("Hi, ", user.name.split(" ")[0])
        if (StringUtils.isNotBlank(user.url))
            GlideUtils.setFotoCircleFromStorage(
                this,
                "user/${user.url}",
                profileButton
            )
        else
            profileButton.setImageResource(R.drawable.logo_klikacara)
        if (Role.VENDOR == user.type)
            createEventOrProductButton.text = "Tambah Produk/Layanan"
        else if (Role.PENGGUNA == user.type)
            createEventOrProductButton.text = "Buat Acara"
    }

    private fun configureViewPager() {
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        bannerPager.adapter = pagerAdapter
        bannerPager.setPadding(marginVeryLarge, 0, marginVeryLarge, 0)
        bannerPager.clipToPadding = false
        bannerPager.pageMargin = marginMedium
        bannerPager.setPageTransformer(false) { page, _ ->
            if (bannerPager != null && bannerPager.currentItem == 0) {
                page.translationX = -24f
            } else if (bannerPager != null && bannerPager.currentItem == pagerAdapter.count - 1) {
                page.translationX = 24f
            } else {
                page.translationX = 0f
            }
        }
        tabDots.setupWithViewPager(bannerPager, true)
        homePresenter.runningPager()
    }

    override fun changeBanner() {
        if (pagerAdapter.count > 0) {
            if (bannerPager.currentItem == pagerAdapter.count - 1)
                bannerPager.currentItem = 0
            else
                bannerPager.currentItem = bannerPager.currentItem + 1
        }
    }

    override fun onUpdateNeeded(updateUrl: String) {
        showInfo(
            "Tersedia versi terbaru, silahkan update aplikasi Anda terlebih dahulu",
            View.OnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            })
    }

    private fun configureAdapter() {
        configureHorizontalItemAdapter(perlengkapanAcaraAdapter, perlengkapanRecycleView)
        configureHorizontalItemAdapter(ulasanAdapter, ulasanRecycleView)
        configureItemAdapter(eventAdapter, eventRecycleView)
        eventAdapter.withOnClickListener { _, _, item, _ ->
            val intent = getIntent(this, DetailEventActivity::class.java)
            intent.putExtra(BuildConfig.orderDb, Parcels.wrap(item.order))
            showActivity(intent)
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter.stopPager()
    }

    override fun onRefresh() {
        swipeHome.isRefreshing = false
        homePresenter.getBanner()
        homePresenter.getEvent()
        homePresenter.getMenuByType(MitraType.PERLENGKAPAN_ACARA)
        homePresenter.getUlasan()
        homePresenter.getMitra()
        homePresenter.checkLoginStatus()
    }

    override fun showSaldo(saldo: Long) {

    }

    override fun setBannerAdapter(bannerListAdapter: List<BannerAdapter>) {
        pagerAdapter.clearFragment()
        for (banner in bannerListAdapter) {
            pagerAdapter.addFragment(BannerFragment.newInstance(banner.imageUrl), "")
        }
        pagerAdapter.notifyDataSetChanged()
        ViewHelper.showView(areaBanner)
    }

    override fun setPerlengkapanAcaraAdapter(klikMenuListAdapter: ArrayList<KlikMenuAdapter>) {
        perlengkapanAcaraAdapter.clear()
        perlengkapanAcaraAdapter.add(klikMenuListAdapter)
        ViewHelper.showView(perlengkapanRecycleView)
        ViewHelper.hideView(perlengkapanLoading)
    }

    override fun setPaketAcaraAdapter(klikMenuListAdapter: ArrayList<KlikMenuAdapter>) {
        paketAcaraAdapter.clear()
        paketAcaraAdapter.add(klikMenuListAdapter)
        ViewHelper.showView(paketAcaraRecycleView)
        ViewHelper.hideView(paketAcaraLoading)
    }

    override fun setPengisiAcaraAdapter(klikMenuListAdapter: ArrayList<KlikMenuAdapter>) {
        pengisiAcaraAdapter.clear()
        pengisiAcaraAdapter.add(klikMenuListAdapter)
        ViewHelper.showView(pengisiAcaraRecycleView)
        ViewHelper.hideView(pengisiAcaraLoading)
    }

    override fun setCrewAcaraAdapter(klikMenuListAdapter: java.util.ArrayList<KlikMenuAdapter>) {
        crewAcaraAdapter.clear()
        crewAcaraAdapter.add(klikMenuListAdapter)
        ViewHelper.showView(crewAcaraRecycleView)
        ViewHelper.hideView(crewAcaraLoading)
    }

    override fun setUlasanAdapter(ulasanListAdapter: ArrayList<UlasanAdapter>) {
        ulasanAdapter.clear()
        ulasanAdapter.add(ulasanListAdapter)
        ViewHelper.showView(ulasanRecycleView)
        ViewHelper.hideView(ulasanLoading)
    }

    override fun setMitraAdapter(mitraListAdapter: ArrayList<MitraAdapter>) {
        mitraAdapter.clear()
        mitraAdapter.add(mitraListAdapter)
        ViewHelper.showView(mitraRecycleView)
        ViewHelper.hideView(mitraLoading)
    }

    @OnClick(R.id.createEventOrProductButton)
    fun onCreateEventOrProductButtonClicked() {
        if (user != null && Role.VENDOR == user!!.type)
            showActivity(getIntent(this, MyProductActivity::class.java))
        else if (user != null && Role.PENGGUNA == user!!.type)
            showActivity(getIntent(this, CreateEventActivity::class.java))
    }

    override fun showEvent(eventAdapter: ArrayList<EventAdapter>) {
        this.eventAdapter.clear()
        this.eventAdapter.add(eventAdapter)
        ViewHelper.showView(eventRecycleView)
        ViewHelper.hideView(eventLoading)
    }

    private fun handleShowMore(
        textView: AppCompatTextView,
        adapter: FastItemAdapter<*>,
        recycleView: RecyclerView
    ) {
        if (textView.isSelected) {
            configureHorizontalItemAdapter(adapter, recycleView)
            textView.text = "Show more"
        } else {
            configureGridItemAdapter(adapter, recycleView, 3)
            textView.text = "Show less"
        }
        textView.isSelected = !textView.isSelected
    }

    @OnClick(R.id.showMorePerlengkapan)
    fun onShowMorePerlengkapanClicked() {
        handleShowMore(showMorePerlengkapan, perlengkapanAcaraAdapter, perlengkapanRecycleView)
    }

    @OnClick(R.id.showMoreButton, R.id.searchEditText)
    fun showMoreButton() {
        showActivity(getIntent(this, SearchEventActivity::class.java))
    }

    @OnClick(R.id.historyButton)
    fun orderButton() {
        if (isLogin)
            showActivity(getIntent(this, OrderActivity::class.java))
        else
            showActivity(getIntent(this, AuthenticationActivity::class.java))
    }

    @OnClick(R.id.profileButton)
    fun profileButton() {
        if (isLogin)
            showActivity(getIntent(this, ProfileActivity::class.java))
        else
            showActivity(getIntent(this, AuthenticationActivity::class.java))
    }
}
