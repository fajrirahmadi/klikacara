package co.id.klikacara.`object`

import org.parceler.Parcel
import java.io.Serializable

@Parcel(Parcel.Serialization.BEAN)
class MasterData(
    var key: String,
    var name: String,
    var description: String,
    var code: String,
    var createdDate: Long = System.currentTimeMillis()
) : Serializable {
    constructor() : this("", "", "", "")

    constructor(key: String, name: String, description: String) : this(key, name, description, "00")
}