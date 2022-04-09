package yunho.app.lolapitest.DTO

import com.google.gson.annotations.SerializedName

data class MiniSeriesDTO(
    @SerializedName("losses") val losses : Int,
    @SerializedName("progress") val progress : String,
    @SerializedName("target") val target : Int,
    @SerializedName("wins") val wins : Int,

)
