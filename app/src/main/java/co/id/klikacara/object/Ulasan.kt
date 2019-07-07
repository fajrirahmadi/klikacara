package co.id.klikacara.`object`

import co.id.klikacara.`object`.authentication.Role

class Ulasan(
    var key: String?,
    var name: String?,
    var url: String,
    var role: Role?,
    var ulasan: String,
    var createdDate: Long = System.currentTimeMillis()
) {
    constructor() : this(null, "", "", null, "")
}