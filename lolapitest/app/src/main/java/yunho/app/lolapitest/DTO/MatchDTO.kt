package yunho.app.lolapitest.DTO

import com.google.gson.annotations.SerializedName

data class MatchDTO(
    @SerializedName("metadata") val metadata : MetadataDTO,
    @SerializedName("info") val info : InfoDTO,
)
