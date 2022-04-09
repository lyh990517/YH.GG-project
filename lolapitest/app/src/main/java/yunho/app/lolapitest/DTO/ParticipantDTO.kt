package yunho.app.lolapitest.DTO

import com.google.gson.annotations.SerializedName

data class ParticipantDTO(
    @SerializedName("summonerName") var summonerName : String,

    @SerializedName("kills") val kills : Int,
    @SerializedName("deaths") val deaths : Int,
    @SerializedName("assists") val assists : Int,

    @SerializedName("championId") val championId : Int,
    @SerializedName("championName") val championName : String,


    @SerializedName("timePlayed") val timePlayed : Int,

    @SerializedName("item0") val item0 : Int,
    @SerializedName("item1") val item1 : Int,
    @SerializedName("item2") val item2 : Int,
    @SerializedName("item3") val item3 : Int,
    @SerializedName("item4") val item4 : Int,
    @SerializedName("item5") val item5 : Int,
    @SerializedName("item6") val item6 : Int,

    @SerializedName("win") val win : Boolean,

    @SerializedName("summoner1Id") val spell1Casts : Int,
    @SerializedName("summoner2Id") val spell2Casts : Int,
    var queueType: Int


    )
