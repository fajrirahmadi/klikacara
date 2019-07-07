package co.id.klikacara.main.view

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.MitraType
import co.id.klikacara.`object`.adapter.BannerAdapter
import co.id.klikacara.`object`.adapter.KlikMenuAdapter
import co.id.klikacara.`object`.adapter.MitraAdapter
import co.id.klikacara.`object`.adapter.UlasanAdapter
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.main.contract.MainContract
import co.id.klikacara.main.presenter.HomePresenter
import co.id.klikacara.product.view.ProductActivity
import co.id.klikacara.vendor.view.MitraDetailActivity
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_main_home.*
import org.parceler.Parcels
import javax.inject.Inject

class HomeFragment : BaseFragment(), MainContract.HomeView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var homePresenter: HomePresenter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private val bannerAdapter = FastItemAdapter<BannerAdapter>()
    private val perlengkapanAcaraAdapter = FastItemAdapter<KlikMenuAdapter>()
    private val paketAcaraAdapter = FastItemAdapter<KlikMenuAdapter>()
    private val pengisiAcaraAdapter = FastItemAdapter<KlikMenuAdapter>()
    private val ulasanAdapter = FastItemAdapter<UlasanAdapter>()
    private val mitraAdapter = FastItemAdapter<MitraAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_main_home, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeHome.setOnRefreshListener(this)
        configureAdapter()
        homePresenter.getBanner()
        homePresenter.getMenuByType(MitraType.PERLENGKAPAN_ACARA)
        homePresenter.getMenuByType(MitraType.PAKET_ACARA)
        homePresenter.getMenuByType(MitraType.PENGISI_ACARA)
        homePresenter.getUlasan()
        homePresenter.getMitra()
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
    }

    override fun onRefresh() {
        swipeHome.isRefreshing = false
        homePresenter.getBanner()

    }

    override fun showSaldo(saldo: Long) {

    }

    override fun setBannerAdapter(bannerListAdapter: List<BannerAdapter>) {
        this.bannerAdapter.clear()
        this.bannerAdapter.add(bannerListAdapter)
        ViewHelper.showView(bannerRecycleView)
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
}