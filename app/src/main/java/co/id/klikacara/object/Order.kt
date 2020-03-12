package co.id.klikacara.`object`

import org.parceler.Parcel
import java.io.Serializable

@Parcel(Parcel.Serialization.BEAN)
class Order(
    var key: String?,
    var uid: String?,
    var name: String = "",
    var deskripsi: String = "",
    var penanggungJawab: String = "",
    var email: String = "",
    var phone: String = "",
    var address: String = "",
    var province: MasterData? = null,
    var city: MasterData? = null,
    var district: MasterData? = null,
    var postCode: String = "",
    var startDate: Long = 0L,
    var endDate: Long = 0L,
    var product: BaseProduct? = null,
    var pesanan: Int = 0,
    var paymentStatus: PaymentStatus = PaymentStatus.MENUNGGU_PEMBAYARAN,
    var amount: Long = 0L,
    var bank: Bank? = null,
    var expiredPayment: Long = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000),
    var reason: String = "",
    var buktiTransfer: String = "",
    var vendorId: String = "",
    var isPromo: Boolean = false,
    var isAllowRegister: Boolean = false,
    var posterUrl: String = "",
    var tiketPrice: Long = 0L,
    var linkAcara: String = "",
    var searchForVendor: Boolean = false,
    var descriptionVendor: String = "",
    var budget: Long = 0L,
    var recomendedVendor: String = "",
    var vendorChoosed: String = ""
) : Serializable {

    constructor() : this(null, null)
}