package co.id.klikacara.`object`

import org.parceler.Parcel
import java.io.Serializable

@Parcel(Parcel.Serialization.BEAN)
class Bank(
    var key: String?,
    var namaRekening: String = "",
    var bankName: String = "",
    var nomorRekening: String = "",
    var url: String = ""
) : Serializable {

    constructor() : this(null)
}