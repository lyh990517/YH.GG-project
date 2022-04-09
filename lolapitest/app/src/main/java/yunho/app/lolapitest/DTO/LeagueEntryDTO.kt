package yunho.app.lolapitest.DTO

import com.google.gson.annotations.SerializedName


data class LeagueEntryDTO(
    @SerializedName("leagueId") val leagueId : String,
    @SerializedName("summonerId") val summonerId : String,
    @SerializedName("summonerName") val summonerName : String,
    @SerializedName("queueType") val queueType : String,
    @SerializedName("tier") val tier : String,
    @SerializedName("rank") val rank : String,

    @SerializedName("leaguePoints") val leaguePoints : Int,
    @SerializedName("wins") val wins : Int,
    @SerializedName("losses") val losses : Int,

    @SerializedName("hotStreak") val hotStreak : Boolean,
    @SerializedName("veteran") val veteran : Boolean,
    @SerializedName("freshBlood") val freshBlood : Boolean,
    @SerializedName("inactive") val inactive : Boolean,

    @SerializedName("miniSeries") val miniSeries : MiniSeriesDTO

)
