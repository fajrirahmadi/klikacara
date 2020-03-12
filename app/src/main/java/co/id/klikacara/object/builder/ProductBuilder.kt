package co.id.klikacara.`object`.builder

import co.id.klikacara.`object`.BaseProduct
import co.id.klikacara.`object`.adapter.ProductAdapter

internal class ProductBuilder(
    var baseProduct: BaseProduct? = null,
    var isChoosed: Boolean = false
) {

    fun baseProduct(baseProduct: BaseProduct): ProductBuilder {
        this.baseProduct = baseProduct
        return this
    }

    fun isChoosed(isChoosed: Boolean): ProductBuilder {
        this.isChoosed = isChoosed
        return this
    }

    fun create(): ProductAdapter {
        return ProductAdapter(this)
    }
}