package co.id.klikacara.order.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.Order
import co.id.klikacara.`object`.adapter.BankAdapter
import co.id.klikacara.base.view.fragment.BaseFragment
import co.id.klikacara.order.contract.OrderContract
import co.id.klikacara.order.presenter.OrderPaymentPresenter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_order_bank_list.*
import org.parceler.Parcels
import javax.inject.Inject

class OrderPaymentFragment : BaseFragment(), OrderContract.OrderPaymentView {

    @Inject
    lateinit var orderPaymentPresenter: OrderPaymentPresenter

    private val bankListAdapter = FastItemAdapter<BankAdapter>()
    private var selectedPosition: Int? = null
    lateinit var order: Order

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_order_bank_list, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        order = Parcels.unwrap(arguments?.getParcelable(BuildConfig.orderDb))
        configureAdapter()
        orderPaymentPresenter.getListBank()
    }

    private fun configureAdapter() {
        configureItemAdapter(bankListAdapter, bankListRecycleView)
        bankListAdapter.withOnClickListener { _, _, item, position ->
            item.isSelected = true
            bankListAdapter.set(position, item)

            order.bank = item.bank

            if (selectedPosition != null) {
                val lastSelectedItem = bankListAdapter.getAdapterItem(selectedPosition!!)
                lastSelectedItem.isSelected = false
                bankListAdapter.set(selectedPosition!!, lastSelectedItem)
            }

            selectedPosition = position

            true
        }
    }

    @OnClick(R.id.nextButton)
    fun onNextButtonClicked() {
        if (order.bank == null) {
            showError("Anda belum memilih metode pembayaran")
        } else {
            val bundle = Bundle()
            bundle.putParcelable(BuildConfig.orderDb, Parcels.wrap(order))
            navigateTo(
                R.id.orderNavGraph,
                R.id.action_move_fromOrderPaymentFragment_toOrderConfirmationFragment,
                bundle
            )
        }
    }

    override fun setBankListAdapter(bankListAdapter: ArrayList<BankAdapter>) {
        this.bankListAdapter.clear()
        this.bankListAdapter.add(bankListAdapter)
    }
}