package co.id.klikacara.product.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.BaseProduct
import co.id.klikacara.`object`.adapter.ProductAdapter
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.product.contract.ProductContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyProductPresenter(
    private val view: ProductContract.MyProductView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
) : BasePresenter() {

    fun getListProduct() {
        getListProductByVendorId(auth.uid!!)
    }

    fun getListProductByVendorId(vendorId: String) {
        database.collection(BuildConfig.productDb)
            .whereEqualTo("vendorId", vendorId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && !it.result!!.isEmpty) {
                    val productList = it.result!!.toObjects(BaseProduct::class.java)
                    val productListAdapter = ArrayList<ProductAdapter>()
                    for (product in productList)
                        productListAdapter.add(ProductAdapter(product))
                    view.doOnGetListProductSuccess(productListAdapter)
                } else {
                    view.doOnGetListProductFailed()

                }
            }
    }


}
