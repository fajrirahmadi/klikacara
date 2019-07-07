package co.id.klikacara.`object`.authentication

import org.parceler.Parcel
import java.io.Serializable

@Parcel(Parcel.Serialization.BEAN)
class RegistrationVendor(
    var user: User?,
    var mitra: Mitra?,
    var password: String
) : Serializable {

    constructor() : this(null, null, "")
}