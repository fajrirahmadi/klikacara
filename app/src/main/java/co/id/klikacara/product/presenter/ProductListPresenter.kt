package co.id.klikacara.product.presenter

import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.BaseProduct
import co.id.klikacara.`object`.adapter.ProductAdapter
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.product.contract.ProductContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProductListPresenter(
    private val view: ProductContract.ProductListView,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) : BasePresenter() {

    fun submitData(listProduct: ArrayList<BaseProduct>) {
        for (product in listProduct) {
            val key = database.collection(BuildConfig.productDb).document().id
            product.key = key
            product.vendorId = auth.uid!!
            database.collection(BuildConfig.productDb)
                .document(key).set(product)
        }
    }

    fun getListProductByCategory(key: String) {
        database.collection(BuildConfig.productDb).whereEqualTo("productCategory", key)
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
