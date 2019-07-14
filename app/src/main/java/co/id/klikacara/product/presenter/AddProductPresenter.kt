package co.id.klikacara.product.presenter

import android.net.Uri
import androidx.appcompat.widget.AppCompatEditText
import co.id.klikacara.BuildConfig
import co.id.klikacara.`object`.BaseProduct
import co.id.klikacara.`object`.KlikMenu
import co.id.klikacara.`object`.PaymentType
import co.id.klikacara.`object`.adapter.TextAdapter
import co.id.klikacara.`object`.authentication.Mitra
import co.id.klikacara.base.presenter.BasePresenter
import co.id.klikacara.base.utils.stringhelper.StringHelper
import co.id.klikacara.product.contract.ProductContract
import co.id.klikacara.validator.ValidatorUsecase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.apache.commons.lang3.StringUtils
import java.io.File

class AddProductPresenter(
    private val view: ProductContract.AddProductView,
    private val validator: ValidatorUsecase,
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) : BasePresenter() {

    fun bindingCurrency(nominalEditText: AppCompatEditText) {
        addDisposable(
            validator.bindingCurrency(nominalEditText)
                .subscribe { value ->
                    if (StringUtils.isNotEmpty(value)) {
                        nominalEditText.setText(value)
                        nominalEditText.setSelection(nominalEditText.text.toString().length)
                    }
                }
        )
    }

    fun getProductKategoriById(productCategory: String) {
        database.collection(BuildConfig.klikMenuDb).document(productCategory).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val category = it.result!!.toObject(KlikMenu::class.java)
                    view.showProductCategory(category)
                }
            }
    }

    fun getMitraData() {
        database.collection(BuildConfig.mitraDb).document(auth.uid!!)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val mitra = it.result!!.toObject(Mitra::class.java)
                    if (mitra != null) {
                        view.setMitraData(mitra)
                        getListProductCategory(mitra.type.toString())
                    }
                }
            }
    }

    private fun getListProductCategory(mitraType: String) {
        database.collection(BuildConfig.klikMenuDb)
            .whereEqualTo("mitraType", mitraType)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val categoryList = it.result!!.toObjects(KlikMenu::class.java)
                    val categoryAdapter = ArrayList<TextAdapter>()
                    for (category in categoryList)
                        categoryAdapter.add(TextAdapter(category))
                    view.setListProductAdapter(categoryAdapter)
                }
            }
    }

    fun getPaymentTypeAdapter() {
        val paymentTypeAdapter = ArrayList<TextAdapter>()
        paymentTypeAdapter.add(TextAdapter(PaymentType.JAM))
        paymentTypeAdapter.add(TextAdapter(PaymentType.HARI))
        paymentTypeAdapter.add(TextAdapter(PaymentType.PAKET))
        paymentTypeAdapter.add(TextAdapter(PaymentType.SATUAN))
        view.setPaymentTypeAdapter(paymentTypeAdapter)
    }

    fun uploadImage(product: BaseProduct, pathAbsolute: String, index: Int) {
        view.showProgressDialog()
        val path = StringHelper.getStringBuilderToString(
            auth.uid,
            product.productCategory,
            "_",
            System.currentTimeMillis().toString()
        )
        storage.reference.child(StringHelper.getStringBuilderToString(BuildConfig.productDb, "/", path))
            .putFile(Uri.fromFile(File(pathAbsolute)))
            .addOnSuccessListener {
                it.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { url ->
                        view.dismissProgressDialog()
                        view.setUploadedImageUrl(url.toString(), index)
                    }.addOnFailureListener {
                        view.dismissProgressDialog()
                    }
            }
            .addOnFailureListener {
                view.setFailedImage(index)
                view.dismissProgressDialog()
            }
    }

    fun submitProduct(product: BaseProduct) {
        view.showProgressDialog()
        val reference = database.collection(BuildConfig.productDb)
        val key = if (StringUtils.isNotBlank(product.key)) product.key else reference.document().id
        product.key = key
        product.vendorId = auth.uid!!
        reference.document(product.key!!).set(product)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    view.doOnSubmitProductSuccess()
                } else {
                    view.doOnSubmitProductFailed()
                }
                view.dismissProgressDialog()
            }
    }
}