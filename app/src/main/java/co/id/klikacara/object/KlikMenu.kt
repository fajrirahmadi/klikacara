package co.id.klikacara.`object`

import org.parceler.Parcel
import java.io.Serializable

@Parcel(Parcel.Serialization.BEAN)
class KlikMenu(
    var key: String?,
    var name: String,
    var description: String,
    var mitraType: MitraType?,
    var url: String = "",
    var active: Boolean = true,
    var createdMenu: Long = System.currentTimeMillis()
) : Serializable {
    constructor() : this(null, "", "", null)
}

enum class MitraType(var description: String) {
    PERLENGKAPAN_ACARA("Perlengkapan"),
    PAKET_ACARA("Paket Acara"),
    PENGISI_ACARA("Pengisi Acara"),
    CREW_ACARA("Crew Acara")
}