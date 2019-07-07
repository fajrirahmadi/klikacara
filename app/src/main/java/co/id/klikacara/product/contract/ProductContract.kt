package co.id.klikacara.product.contract

import co.id.klikacara.`object`.KlikMenu
import co.id.klikacara.`object`.adapter.ProductAdapter
import co.id.klikacara.`object`.adapter.TextAdapter
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.base.contract.BaseContract

interface ProductContract {

    interface ProductListView : BaseContract.View {
        fun doOnGetListProductSuccess(productListAdapter: ArrayList<ProductAdapter>)
        fun doOnGetListProductFailed()
    }

    interface ProductDetailView : BaseContract.View {
        fun setDataVendor(mitra: Mitra)
        fun userIsVendor()
    }

    interface MyProductView : BaseContract.View {
        fun doOnGetListProductSuccess(productListAdapter: ArrayList<ProductAdapter>)
        fun doOnGetListProductFailed()
    }

    interface AddProductView : BaseContract.View {
        fun showProductCategory(category: KlikMenu?)
        fun setMitraData(mitra: Mitra)
        fun setListProductAdapter(categoryAdapter: ArrayList<TextAdapter>)
        fun setPaymentTypeAdapter(paymentTypeAdapter: List<TextAdapter>)
        fun setUploadedImageUrl(url: String, index: Int)
        fun doOnSubmitProductSuccess()
        fun doOnSubmitProductFailed()
        fun setFailedImage(index: Int)

    }
}