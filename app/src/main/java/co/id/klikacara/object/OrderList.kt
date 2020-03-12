package co.id.klikacara.`object`

class OrderList(
    var key: String?,
    var oid: String?,
    var type: MitraType?,
    var paymentStatus: PaymentStatus?,
    var name: String,
    var amount: Long = 0L,
    var url: String = "",
    var startDate: Long = 0L,
    var endDate: Long = 0L
) {
    constructor() : this(null, null, null, null, "")
}

enum class PaymentStatus(val description: String) {
    MENCARI_VENDOR("Mencari Vendor"),
    MENUNGGU_PEMBAYARAN("Menunggu Pembayaran"),
    VERIFIKASI_PEMBAYARAN("Menunggu Verifikasi Pembayaran"),
    PESANAN_DITERIMA("Pesanan Diterima"),
    PESANAN_DIPROSES("Pesanan Diproses"),
    PESANAN_DIBATALKAN("Pesanan Dibatalkan"),
    PESANAN_SELESAI("Pesanan Selesai")
}