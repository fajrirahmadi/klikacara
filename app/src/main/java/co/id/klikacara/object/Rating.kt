package co.id.klikacara.`object`

import org.parceler.Parcel
import java.io.Serializable

@Parcel(Parcel.Serialization.BEAN)
class Rating(
    var key: String?,
    var nama: String = "",
    var rating: Float = 0f,
    var ulasan: String = "",
    var url: String = "",
    var vendorId: String = "",
    var datePost: Long = System.currentTimeMillis()
) : Serializable {

    constructor() : this(null)
}