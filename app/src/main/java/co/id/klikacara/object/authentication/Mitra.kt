package co.id.klikacara.`object`.authentication

import co.id.klikacara.`object`.MasterData
import co.id.klikacara.`object`.MitraType
import org.parceler.Parcel
import java.io.Serializable

@Parcel(Parcel.Serialization.BEAN)
class Mitra(
    var key: String?,
    var type: MitraType?,
    var province: MasterData?,
    var city: MasterData?,
    var district: MasterData?,
    var name: String = "",
    var pic: String = "",
    var phone: String = "",
    var address: String = "",
    var postalCode: String = "",
    var description: String = "",
    var slogan: String = "",
    var verificationStatus: VerificationStatus = VerificationStatus.NOT_VERIFIED,
    var createdDate: Long = System.currentTimeMillis(),
    var identityUrl: String = "",
    var identityWithUserUrl: String = "",
    var userWithPlaceUrl: String = "",
    var documentUrl: String = ""
) : Serializable {
    constructor() : this(null, null, null, null, null)
}

enum class VerificationStatus(val description: String) {
    VERIFIED("Terverifikasi"),
    NOT_VERIFIED("Belum Terverifikasi"),
    BANNED("Terblokir")
}