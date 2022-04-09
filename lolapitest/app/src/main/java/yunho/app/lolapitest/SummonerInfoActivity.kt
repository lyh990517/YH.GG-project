package yunho.app.lolapitest

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import yunho.app.lolapitest.Adapter.ParticipantAdapter
import yunho.app.lolapitest.DTO.LeagueEntryDTO
import yunho.app.lolapitest.DTO.MatchDTO
import yunho.app.lolapitest.DTO.ParticipantDTO
import yunho.app.lolapitest.DTO.SummonerDTO
import yunho.app.lolapitest.lolAPI.lolAPIService

class SummonerInfoActivity : AppCompatActivity() {
    //롤 API를 불러오기위한 변수
    private lateinit var lolAPI: lolAPIService
    private lateinit var lolAPIForMatch: lolAPIService
    //리사이클러뷰 설정 어댑터
    private lateinit var participantAdapter: ParticipantAdapter
    //파이어 베이스에 저장되있는 API 키를 받는 변수
    private lateinit var Remote_API_KEY: String

    //리사이클러뷰
    private val recycler: RecyclerView by lazy {
        findViewById(R.id.RecyclerView)
    }

    //소환사 정보창의 요소들
    private val profileImageView: ImageView by lazy {
        findViewById(R.id.profileImageView)
    }
    private val TierImageView: ImageView by lazy {
        findViewById(R.id.TierImageView)
    }

    private val SummonerNameTextView: TextView by lazy {
        findViewById(R.id.SummonerNameTextView)
    }
    private val SummonerLevelTextView: TextView by lazy {
        findViewById(R.id.SummonerLevelTextView)
    }

    private val TierTextView: TextView by lazy {
        findViewById(R.id.TierTextView)
    }
    private val RankTextView: TextView by lazy {
        findViewById(R.id.RankTextView)
    }
    private val leaguePointTextView: TextView by lazy {
        findViewById(R.id.leaguePointTextView)
    }

    private val queueTypeTextView: TextView by lazy {
        findViewById(R.id.queueTypeTextView)
    }
    private val winsTextView: TextView by lazy {
        findViewById(R.id.winsVal)
    }
    private val losesTextView: TextView by lazy {
        findViewById(R.id.losesVal)
    }
    private val winRateTextView: TextView by lazy {
        findViewById(R.id.winRateVal)
    }
    private val infoView: ConstraintLayout by lazy {
        findViewById(R.id.InfoView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //MainActivity로부터 인텐트에 저장된 값을 넘겨받는다
        val Summoner = intent.getParcelableExtra<SummonerDTO>("SummonerDTO")
        Remote_API_KEY = intent.getStringExtra("Remote_API_KEY").toString()

        //레트로 핏 라이브러리 빌드
        //2번째 레트로핏은 매치정보를 불러오는 역할이다
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kr.api.riotgames.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofit2 = Retrofit.Builder()
            .baseUrl("https://asia.api.riotgames.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //API 서비스 초기화
        lolAPI = retrofit.create(lolAPIService::class.java)
        lolAPIForMatch = retrofit2.create(lolAPIService::class.java)

        setContentView(R.layout.activity_summoner_info)

        //리사이클러뷰 설정
        participantAdapter = ParticipantAdapter()
        recycler.adapter = participantAdapter
        recycler.layoutManager = LinearLayoutManager(this@SummonerInfoActivity)

        //전적을 불러오는 함수 호출
        getMatchIdBypuuid(Summoner?.puuId.orEmpty(), Summoner?.summonerName.orEmpty())
        //소환사 정보창 불러오는 함수 호출
        searchLeagueEntryByEncryptedSummonerId(Summoner?.summonerId.orEmpty())

        SummonerNameTextView.text = Summoner?.summonerName
        SummonerLevelTextView.text = Summoner?.level.toString()
        Glide.with(this)
            .load(getString(R.string.PROFILE_ICON_BASE_URL) + "${Summoner?.profileIcon}.png")
            .into(profileImageView)
        //소환사 정보창의 레이어 순서가 맨앞에 올 수 있도록 해준다
        infoView.bringToFront()


    }
    //puuid를 기준으로 소환사의 매치 ID를 불러오는 함수
    private fun getMatchIdBypuuid(puuid: String, summonerName: String) {
        lolAPIForMatch.getMatchIdBypuuid(puuid, 0, 20, Remote_API_KEY)
            .enqueue(object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {

                    if (response.isSuccessful.not()) {
                        return
                    }
                    response.body()?.let {
                        it.forEach {
                            //불러온 매치 ID를 통해 매치의 상세정보 검색
                            searchMatchInfoByMatchID(it, summonerName)
                        }
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {

                }

            })
    }
    //매치ID를 통해 매치의 상세정보를 불러오는 함수
    private fun searchMatchInfoByMatchID(matchId: String, summonerName: String) {
        lolAPIForMatch.getMatchInfoByMatchID(matchId, Remote_API_KEY)
            .enqueue(object : Callback<MatchDTO> {
                override fun onResponse(call: Call<MatchDTO>, response: Response<MatchDTO>) {
                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {

                        //큐타입은 게임의 유형을 알려주는 변수이다
                        var queueType = 999
                        queueType = it.info.queueId.toInt()

                        //검색한 소환사의 전적만 가져오도록 필터링
                        it.info.participants.filter {
                            it.summonerName == "${summonerName}"
                        }.forEach {
                            //전적정보를 어댑터에 입력해준다
                            it.queueType = queueType
                            participantAdapter.participants.add(it)
                            //아이템이 입력되었다고 알려준다
                            participantAdapter.notifyItemInserted(participantAdapter.participants.size)
                        }

                    }

                }

                override fun onFailure(call: Call<MatchDTO>, t: Throwable) {

                }

            })

    }
    //암호화된 소환사 ID를 통해 소환사의 리그정보를 가져오는 함수
    private fun searchLeagueEntryByEncryptedSummonerId(id: String) {
        lolAPI.getSummonerEntriesByEncryptedSummonerID(id, Remote_API_KEY)
            .enqueue(object : Callback<List<LeagueEntryDTO>> {
                override fun onResponse(
                    call: Call<List<LeagueEntryDTO>>,
                    response: Response<List<LeagueEntryDTO>>
                ) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    //언랭크일 경우 null 값이 올수도 있으므로 처리
                    if (response.body().isNullOrEmpty()) {
                        return
                    }

                    response.body()?.let {
                        it.get(0).let {
                            //소환사 정보창의 정보를 입력한다
                            RankTextView.text = it.tier
                            TierTextView.text = it.rank
                            leaguePointTextView.text = it.leaguePoints.toString()
                            winsTextView.text = it.wins.toString()
                            losesTextView.text = it.losses.toString()
                            queueTypeTextView.text = it.queueType
                            val winRate =
                                ((it.wins.toDouble()) / (it.wins.toDouble() + it.losses.toDouble())) * 100
                            SetRankImageView(it.tier)
                            winRateTextView.text = String.format("%.2f", winRate) + "%"


                        }
                    }
                }

                override fun onFailure(call: Call<List<LeagueEntryDTO>>, t: Throwable) {
                    //실패시

                }

            })
    }

    //티어에 따라 이미지뷰에 이미지를 지정해주는 함수
    private fun SetRankImageView(RanK: String) {
        when (RanK) {
            "IRON" -> {
                TierImageView.setImageResource(R.drawable.emblem_iron)
            }
            "BRONZE" -> {
                TierImageView.setImageResource(R.drawable.emblem_bronze)
            }
            "SILVER" -> {
                TierImageView.setImageResource(R.drawable.emblem_silver)
            }
            "GOLD" -> {
                TierImageView.setImageResource(R.drawable.emblem_gold)
            }
            "PLATINUM" -> {
                TierImageView.setImageResource(R.drawable.emblem_platinum)
            }
            "DIAMOND" -> {
                TierImageView.setImageResource(R.drawable.emblem_diamond)
            }
            "MASTER" -> {
                TierImageView.setImageResource(R.drawable.emblem_master)
            }
            "GRANDMASTER" -> {
                TierImageView.setImageResource(R.drawable.emblem_grandmaster)
            }
            else -> {
                TierImageView.setImageResource(R.drawable.emblem_challenger)
            }
        }
    }

}


