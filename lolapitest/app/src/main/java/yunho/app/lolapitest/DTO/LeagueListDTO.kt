package yunho.app.lolapitest.DTO

import com.google.gson.annotations.SerializedName

data class LeagueListDTO(
    @SerializedName("leagueId") val leagueID : String,
    @SerializedName("entries") val entries : List<LeagueItemDTO>,
    @SerializedName("tier") val tier : String,
    @SerializedName("name") val name : String,
    @SerializedName("queue") val queue : String,
)
