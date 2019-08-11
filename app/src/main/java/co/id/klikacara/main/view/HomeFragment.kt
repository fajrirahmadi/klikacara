package co.id.klikacara.main.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import co.id.klikacara.base.utils.updatehelper.ForceUpdateChecker
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.adapter.ViewPagerAdapter
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.createevent.view.CreateEventActivity
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.HomePresenter
import co.id.klikacara.product.view.MyProductActivity
import co.id.klikacara.product.view.ProductActivity
import co.id.klikacara.vendor.view.MitraDetailActivity
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_main_home.*
import org.parceler.Parcels
import javax.inject.Inject


class HomeFragment : BaseFragment(), MainContract.HomeView, SwipeRefreshLayout.OnRefreshListener,
    ForceUpdateChecker.OnUpdateNeededListener {

    @Inject
    lateinit var homePresenter: HomePresenter
    private var user: User? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private val bannerAdapter = FastItemAdapter<BannerAdapter>()
    private val perlengkapanAcaraAdapter = FastItemAdapter<KlikMenuAdapter>()
    private val paketAcaraAdapter = FastItemAdapter<KlikMenuAdapter>()
    private val pengisiAcaraAdapter = FastItemAdapter<KlikMenuAdapter>()
    private val ulasanAdapter = FastItemAdapter<UlasanAdapter>()
    private val mitraAdapter = FastItemAdapter<MitraAdapter>()
    private val eventAdapter = FastItemAdapter<EventAdapter>()
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_main_home, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ForceUpdateChecker.with(activity!!).onUpdateNeeded(this).check()
        swipeHome.setOnRefreshListener(this)
        configureAdapter()
        configureViewPager()
        homePresenter.getBanner()
        homePresenter.getEvent()
        homePresenter.getMenuByType(MitraType.PERLENGKAPAN_ACARA)
        homePresenter.getMenuByType(MitraType.PAKET_ACARA)
        homePresenter.getMenuByType(MitraType.PENGISI_ACARA)
        homePresenter.getUlasan()
        homePresenter.getMitra()
        homePresenter.checkLoginStatus()
    }

    override fun doOnUserLogin() {
        ViewHelper.showView(createEventOrProductButton)
    }

    override fun doOnUserNotLogin() {
        ViewHelper.hideView(createEventOrProductButton)
    }

    override fun setUserData(user: User) {
        ViewHelper.showView(createEventOrProductButton)
        this.user = user
        if (Role.VENDOR == user.type)
            createEventOrProductButton.text = "Tambah Produk/Layanan"
        else if (Role.PENGGUNA == user.type)
            createEventOrProductButton.text = "Buat Acara"
    }

    private fun configureViewPager() {
        pagerAdapter = ViewPagerAdapter(childFragmentManager)
        bannerPager.adapter = pagerAdapter
        tabDots.setupWithViewPager(bannerPager, true)
        homePresenter.runningPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homePresenter.stopPager()
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
        showInfo("Tersedia versi terbaru, silahkan update aplikasi Anda terlebih dahulu", View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        })
    }

    private fun configureAdapter() {
        configureHorizontalItemAdapter(bannerAdapter, bannerRecycleView)
        bannerAdapter.withOnClickListener { _, _, item, _ ->
            true
        }
        configureHorizontalItemAdapter(perlengkapanAcaraAdapter, perlengkapanRecycleView)
        perlengkapanAcaraAdapter.withOnClickListener { _, _, item, _ ->
            val intent = getIntent(activity!!, ProductActivity::class.java)
            intent.putExtra(BuildConfig.klikMenuDb, Parcels.wrap(item.klikMenu))
            showActivity(intent)
            true
        }
        configureHorizontalItemAdapter(paketAcaraAdapter, paketAcaraRecycleView)
        paketAcaraAdapter.withOnClickListener { _, _, item, _ ->
            val intent = getIntent(activity!!, ProductActivity::class.java)
            intent.putExtra(BuildConfig.klikMenuDb, Parcels.wrap(item.klikMenu))
            showActivity(intent)
            true
        }
        configureHorizontalItemAdapter(pengisiAcaraAdapter, pengisiAcaraRecycleView)
        pengisiAcaraAdapter.withOnClickListener { _, _, item, _ ->
            val intent = getIntent(activity!!, ProductActivity::class.java)
            intent.putExtra(BuildConfig.klikMenuDb, Parcels.wrap(item.klikMenu))
            showActivity(intent)
            true
        }
        configureHorizontalItemAdapter(ulasanAdapter, ulasanRecycleView)
        configureHorizontalItemAdapter(mitraAdapter, mitraRecycleView)
        mitraAdapter.withOnClickListener { _, _, item, _ ->
            val intent = getIntent(activity!!, MitraDetailActivity::class.java)
            intent.putExtra(BuildConfig.userDb, Parcels.wrap(item.user))
            showActivity(intent)
            true
        }
        configureHorizontalItemAdapter(eventAdapter, eventRecycleView)
        eventAdapter.withOnClickListener { _, _, item, position ->
            true
        }
    }

    override fun onRefresh() {
        swipeHome.isRefreshing = false
        homePresenter.getBanner()

    }

    override fun showSaldo(saldo: Long) {

    }

    override fun setBannerAdapter(bannerListAdapter: List<BannerAdapter>) {
        /*
        this.bannerAdapter.clear()
        this.bannerAdapter.add(bannerListAdapter)
        ViewHelper.showView(bannerRecycleView)
        */
        pagerAdapter.clearFragment()
        for (banner in bannerListAdapter) {
            pagerAdapter.addFragment(BannerFragment.newInstance(banner.imageUrl), "")
        }
        pagerAdapter.notifyDataSetChanged()
        ViewHelper.showView(areaBanner)
        ViewHelper.hideView(bannerLoading)
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
            showActivity(getIntent(activity!!, MyProductActivity::class.java))
        else if (user != null && Role.PENGGUNA == user!!.type)
            showActivity(getIntent(activity!!, CreateEventActivity::class.java))
    }

    override fun showEvent(eventAdapter: ArrayList<EventAdapter>) {
        this.eventAdapter.clear()
        this.eventAdapter.add(eventAdapter)
        ViewHelper.showView(eventRecycleView)
        ViewHelper.hideView(eventLoading)
    }

    @OnClick(R.id.showMorePerlengkapan)
    fun onShowMorePerlengkapanClicked() {
        handleShowMore(showMorePerlengkapan, perlengkapanAcaraAdapter, perlengkapanRecycleView)
    }

    @OnClick(R.id.showMorePaketAcara)
    fun onShowMorePaketAcaraClicked() {
        handleShowMore(showMorePaketAcara, paketAcaraAdapter, paketAcaraRecycleView)

    }

    @OnClick(R.id.showMorePengisiAcara)
    fun onShowMorePengisiAcaraClicked() {
        handleShowMore(showMorePengisiAcara, pengisiAcaraAdapter, pengisiAcaraRecycleView)
    }

    private fun handleShowMore(textView: AppCompatTextView, adapter: FastItemAdapter<*>, recycleView: RecyclerView) {
        if (textView.isSelected) {
            configureHorizontalItemAdapter(adapter, recycleView)
            textView.text = "Show more"
        } else {
            configureGridItemAdapter(adapter, recycleView, 4)
            textView.text = "Show less"
        }
        textView.isSelected = !textView.isSelected
    }
}