package yunho.app.lolapitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteCallbackList
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.MotionEvent
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import yunho.app.lolapitest.lolAPI.lolAPIService
import yunho.app.lolapitest.DTO.SummonerDTO
import retrofit2.Callback
import yunho.app.lolapitest.Adapter.ParticipantAdapter
import yunho.app.lolapitest.DTO.LeagueEntryDTO
import yunho.app.lolapitest.DTO.LeagueListDTO
import yunho.app.lolapitest.DTO.MatchDTO

class MainActivity : AppCompatActivity() {
    //파이어 베이스 Realtime Database 사용을 위한 선언
    private val firebase = Firebase.database
    private val Ref = firebase.getReference()

    //파이어 베이스에 저장되있는 API_KEY를 저장하기위한 변수
    private lateinit var Remote_API_KEY:String

    //아이디 검색하는 창
    private val searchBar: EditText by lazy {
        findViewById(R.id.searchBar)
    }
    // 롤 API 인터페이스
    private lateinit var lolAPI: lolAPIService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://kr.api.riotgames.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //레트로 핏 라이브러리 빌드
        lolAPI = retrofit.create(lolAPIService::class.java)
        //롤 API 초기화
        setContentView(R.layout.activity_main)
        initSearchBar()

        //파이어 베이스에 저장되어있는 값을 불러온다
        Ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               Remote_API_KEY = snapshot.child("api_key").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    //기초 정보 검색
    //searchSummoner 함수를 통해 getSummonerInfoByName 함수 호출
    //getSummonerInfoByName를 통해 기초 소환사 정보값을 불러온다

    //매치 정보 검색
    //puuid 값을 통해 getMatchIdBypuuid 함수를 실행하여 매치아이디 리스트를 불러온다
    //불러온 리스트의 값들을 searchMatchInfoByMatchID 함수에 넣어 매치의 상세정보를 불러온다

    //소환사 상세 정보 검색
    //암호화된 id 값을 통해 searchLeagueEntryByEncryptedSummonerId 함수를 호출하여 소환사의 상세정보를 불러온다

    // 기초정보검색 -> 소환사 상세 정보 검색
    //             매치 정보 검색

    //검색버튼없이 엔터키로 바로 검색할 수있도록 서치바를 초기화해준다
    private fun initSearchBar() {
        searchBar.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                Toast.makeText(this, "${searchBar.text}(으)로 검색합니다", Toast.LENGTH_SHORT).show()
                searchSummoner(searchBar.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }


    //소환사 이름기준 검색
    private fun searchSummoner(name: String) {
        //getSummonerInfoByName 함수를 이용하여 기초적인 정보를 우선적으로 불러온다
        lolAPI.getSummonerInfoByName(name, Remote_API_KEY)
            .enqueue(object : Callback<SummonerDTO> {
                override fun onResponse(
                    call: Call<SummonerDTO>,
                    response: Response<SummonerDTO>
                ) {
                    //onResponse 통신 성공시
                    if (response.isSuccessful.not()) {
                        if(response.code() == 403){
                            Toast.makeText(this@MainActivity,"APIkey 교체가 필요합니다.",Toast.LENGTH_SHORT).show()
                        }
                        if(response.code() == 404){
                            Toast.makeText(this@MainActivity,"찾으시는 소환사가 존재하지 않습니다 .",Toast.LENGTH_SHORT).show()
                        }
                        //onResponse 통신 실패시
                        return
                    }
                    response.body()?.let {
                        val intent = Intent(this@MainActivity, SummonerInfoActivity::class.java)
                        intent.putExtra("SummonerDTO", it)
                        intent.putExtra("Remote_API_KEY",Remote_API_KEY)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<SummonerDTO>, t: Throwable) {
                    //onResponse 통신 실패시
                    Log.e("Fail","${t.message}")
                    Log.e("Fail","${call.request().url()}")
                }

            })
    }
}

//TODO sharedreference 이용하여 API key를 로컬로 변경가능하게 만들기
//TODO 기본은 내가 파이어베이스로 키를 관리하지만 안될경우 사용자가 로컬로 관리



