package com.sajaksenja.search

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.PaymentStatus
import co.id.klikacara.`object`.adapter.EventAdapter
import co.id.klikacara.base.utils.viewhelper.ViewHelper
import co.id.klikacara.base.view.activity.BaseActivity
import co.id.klikacara.createevent.view.DetailEventActivity
import co.id.klikacara.search.contract.SearchContract
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.sajaksenja.base.utils.helper.FirebaseListenerHelper
import com.sajaksenja.search.presenter.SearchPresenter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_error.*
import kotlinx.android.synthetic.main.content_loading.*
import org.parceler.Parcels

class SearchEventActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    SearchContract.View {

    private val database = FirebaseFirestore.getInstance()
    private val listenerList = ArrayList<FirebaseListenerHelper>()
    private val eventAdapter = FastItemAdapter<EventAdapter>()
    private val eventList = ArrayList<Order>()
    private val eventFiltered = ArrayList<Order>()
    private var page = 0
    private lateinit var searchPresenter: SearchPresenter
    private lateinit var eventListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        configureToolbarEmpty()
        searchPresenter = SearchPresenter(this)
        swipeSearch.setOnRefreshListener(this)
        configureItemAdapter(eventAdapter, searchListRecycleView)
        eventAdapter.withOnClickListener { _, _, item, position ->
            val intent = getIntent(this, DetailEventActivity::class.java)
            intent.putExtra(BuildConfig.orderDb, Parcels.wrap(item.order))
            showActivity(intent)
            true
        }
        getListEvent()
    }

    override fun searchValid(filter: String) {
        if (filter.isNotEmpty())
            ViewHelper.showView(clearButton)
        else
            ViewHelper.hideView(clearButton)
        eventFiltered.clear()
        eventAdapter.clear()
        if (eventList.size > 0) {
            ViewHelper.showView(loadingArea)
            for (data in eventList) {
                if (data.name.contains(filter, true))
                    eventFiltered.add(data)
            }
            page = 0
            showSajakAdapter(page, eventFiltered)
        }
    }

    override fun onRefresh() {
        swipeSearch.isRefreshing = false
        getListEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopListen()
        searchPresenter.clearDisposable()
    }

    private fun stopListen() {
        for (listener in listenerList) {
            listener.stopListen()
        }
        listenerList.clear()
    }

    private fun getListEvent() {
        page = 0
        eventList.clear()
        eventAdapter.clear()
        stopListen()
        ViewHelper.showView(loadingArea)
        eventListener = database.collection(BuildConfig.orderDb)
            .whereEqualTo("promo", true)
            .whereEqualTo("paymentStatus", PaymentStatus.PESANAN_SELESAI.toString())
            .orderBy("startDate", Query.Direction.ASCENDING)
            .addSnapshotListener { snap, e ->
                if (e != null)
                    return@addSnapshotListener

                if (snap != null && eventList.size == 0) {
                    eventList.addAll(snap.toObjects(Order::class.java))
                    searchPresenter.bindingSearchEditText(searchEditText)
                }
            }
    }

    private fun showSajakAdapter(page: Int, sajakList: List<Order>) {
        for ((index, data) in sajakList.withIndex()) {
            if (index >= page * 20)
                eventAdapter.add(eventAdapter.adapterItemCount, EventAdapter(data))
            if (index == (page + 1) * 20 - 1) {
                this.page++
                break
            }
        }
        if (eventAdapter.adapterItemCount > 0) {
            ViewHelper.showView(searchListRecycleView)
            ViewHelper.hideView(errorArea)
        } else {
            ViewHelper.showView(errorArea)
            ViewHelper.hideView(searchListRecycleView)
        }
        ViewHelper.hideView(loadingArea)
    }

    @OnClick(R.id.clearButton)
    fun clearButton() {
        searchEditText.setText("")
        ViewHelper.hideView(clearButton)
    }
}