package co.id.klikacara.`object`.authentication

import org.parceler.Parcel
import java.io.Serializable

@Parcel(Parcel.Serialization.BEAN)
class User(
    var uid: String?,
    var name: String = "",
    var email: String = "",
    var url: String = "",
    var type: Role = Role.PENGGUNA
) : Serializable {
    constructor() : this(null)
}

enum class Role(val description: String) {
    ADMIN("Admin"),
    PENGGUNA("Pengguna"),
    VENDOR("Vendor"),
    AMBASADOR("Ambasador")
}