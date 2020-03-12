package co.id.klikacara.order.view.fragment

import android.os.Bundle
import androidx.core.util.PatternsCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.`object`.BaseProduct
import co.id.klikacara.`object`.Order
import co.id.klikacara.base.view.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_order_detail.*
import org.apache.commons.lang3.StringUtils
import org.parceler.Parcels

class OrderDetailFragment : BaseFragment() {

    val order = Order()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getInflate(inflater, R.layout.fragment_order_detail, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        order.product = Parcels.unwrap<BaseProduct>(activity?.intent?.extras?.getParcelable(BuildConfig.productDb))
    }

    @OnClick(R.id.nextButton)
    fun onNextButtonClicked() {
        if (StringUtils.isBlank(namaAcaraEditText.text.toString()))
            showError("Nama acara tidak boleh kosong")
        else if (StringUtils.isBlank(penanggungJawabEditText.text.toString()))
            showError("Penanggung jawab tidak boleh kosong")
        else if (StringUtils.isBlank(emailEditText.text.toString()))
            showError("Email penanggung jawab tidak boleh kosong")
        else if (!PatternsCompat.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches())
            showError("Email penanggung jawab tidak valid")
        else if (StringUtils.isBlank(phoneEditText.text.toString()))
            showError("Nomor Telepon Penanggung jawab tidak boleh kosong")
        else {
            order.name = namaAcaraEditText.text.toString()
            order.penanggungJawab = penanggungJawabEditText.text.toString()
            order.email = emailEditText.text.toString()
            order.phone = phoneEditText.text.toString()
            val bundle = Bundle()
            bundle.putParcelable(BuildConfig.orderDb, Parcels.wrap(order))
            navigateTo(R.id.orderNavGraph, R.id.action_move_fromOrderDetailFragment_toOrderAddressFragment, bundle)
        }
    }
}