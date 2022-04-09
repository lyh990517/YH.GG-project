package yunho.app.lolapitest.DTO

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//소환사 정보 데이터 클래스
//이 DTO 에서 다른 DTO 에서 요구하는 각종 ID 값을 가져오는 가장 기본적인 역할을 한다.
@Parcelize
data class SummonerDTO(
    @SerializedName("accountId") val id : String,
    @SerializedName("profileIconId") val profileIcon : Int,
    @SerializedName("revisionDate") val revisionDate : Long,
    @SerializedName("name") val summonerName : String,
    @SerializedName("id") val summonerId : String,
    @SerializedName("puuid") val puuId : String,
    @SerializedName("summonerLevel") val level : Long
): Parcelable
