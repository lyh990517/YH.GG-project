package yunho.app.lolapitest.DTO

import com.google.gson.annotations.SerializedName

data class InfoDTO (
    @SerializedName("gameDuration") val gameDuration : Long,
    @SerializedName("gameMode") val gameMode : String,
    @SerializedName("gameType") val gameType : String,
    @SerializedName("queueId") val queueId : String,
    @SerializedName("participants") val participants : List<ParticipantDTO>,
    @SerializedName("gameEndTimestamp") val gameEndTimestamp : Long
        )