package yunho.app.lolapitest.DTO

import com.google.gson.annotations.SerializedName

data class MetadataDTO (
    @SerializedName("dataVersion") val dataVersion : String,
    @SerializedName("matchId") val matchId : String,
    @SerializedName("participants") val participants : List<String>
        )