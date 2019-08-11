package co.id.klikacara.`object`

import org.parceler.Parcel
import java.io.Serializable

@Parcel(Parcel.Serialization.BEAN)
class BaseProduct(
    var key: String?,
    var paymentType: PaymentType?,
    var name: String = "",
    var description: String = "",
    var url: Map<String, Boolean> = HashMap(),
    var price: Long = 0L,
    var vendorId: String = "",
    var productCategory: String = "",
    var createdDate: Long = System.currentTimeMillis()
) : Serializable {
    constructor() : this(null, null)
}

enum class PaymentType(val description: String) {
    JAM("Jam"),
    HARI("Hari"),
    PAKET("Paket"),
    SATUAN("Satuan"),
    METER("Meter Persegi")
}