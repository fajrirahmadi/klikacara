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
    var createdDate: Long = System.currentTimeMillis()
) : Serializable {
    constructor() : this(null, null, null, null, null)
}