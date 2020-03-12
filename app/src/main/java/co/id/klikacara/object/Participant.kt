package co.id.klikacara.`object`

data class Participant(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var eventId: String = "",
    var rewardCode: String = "",
    var isRewardClaimed: Boolean = true,
    var expiredReward: Long = System.currentTimeMillis()
)