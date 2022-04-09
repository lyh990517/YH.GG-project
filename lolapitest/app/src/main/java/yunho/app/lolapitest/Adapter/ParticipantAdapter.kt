package yunho.app.lolapitest.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yunho.app.lolapitest.DTO.ParticipantDTO
import yunho.app.lolapitest.R

class ParticipantAdapter : RecyclerView.Adapter<ParticipantAdapter.ViewHolder>() {
    var participants = mutableListOf<ParticipantDTO>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(participant: ParticipantDTO) {
            var kda = ""
            var tmp = ((participant.kills.toDouble() + participant.assists.toDouble())/participant.deaths.toDouble())
            if(participant.deaths != 0){
                kda = String.format("%.2f",tmp)
            }else{
                kda = "Perfect"
            }

            itemView.findViewById<TextView>(R.id.queueTypeTextView).text = QUEUE[participant.queueType]
            itemView.findViewById<TextView>(R.id.kill).text = participant.kills.toString()
            itemView.findViewById<TextView>(R.id.death).text = participant.deaths.toString()
            itemView.findViewById<TextView>(R.id.assist).text = participant.assists.toString()
            itemView.findViewById<TextView>(R.id.kdaTextView).text = kda
            itemView.findViewById<TextView>(R.id.WinOrLose).text = if (participant.win) {
                "win"
            } else {
                "lose"
            }
            if (participant.win) {
                itemView.findViewById<ConstraintLayout>(R.id.Container).setBackgroundColor(Color.parseColor(WIN_COLOR))
            } else {
                itemView.findViewById<ConstraintLayout>(R.id.Container).setBackgroundColor(Color.parseColor(LOSE_COLOR))
            }
            itemView.findViewById<TextView>(R.id.playTime).text =
                (participant.timePlayed / 60).toString() + ":" +
                        if (participant.timePlayed % 60 < 10) {
                            "0" + (participant.timePlayed % 60).toString()
                        } else {
                            (participant.timePlayed % 60).toString()
                        }

            Glide.with(itemView)
                .load(CHAMPION_SQUARE_IMAGE_BASE_URL + "${participant.championName}.png")
                .into(itemView.findViewById(R.id.championImageView))

            Glide.with(itemView)
                .load(SUMMONER_SPELL_IMAGE_BASE_URL + "${SPELLS[participant.spell1Casts]}.png")
                .into(itemView.findViewById(R.id.spell1))
            Glide.with(itemView)
                .load(SUMMONER_SPELL_IMAGE_BASE_URL + "${SPELLS[participant.spell2Casts]}.png")
                .into(itemView.findViewById(R.id.spell2))
            Glide.with(itemView)
                .load(ITEM_IMAGE_BASE_URL + "${participant.item0}.png")
                .into(itemView.findViewById(R.id.item0))

            Glide.with(itemView)
                .load(ITEM_IMAGE_BASE_URL + "${participant.item1}.png")
                .into(itemView.findViewById(R.id.item1))
            Glide.with(itemView)
                .load(ITEM_IMAGE_BASE_URL + "${participant.item2}.png")
                .into(itemView.findViewById(R.id.item2))
            Glide.with(itemView)
                .load(ITEM_IMAGE_BASE_URL + "${participant.item3}.png")
                .into(itemView.findViewById(R.id.item3))
            Glide.with(itemView)
                .load(ITEM_IMAGE_BASE_URL + "${participant.item4}.png")
                .into(itemView.findViewById(R.id.item4))
            Glide.with(itemView)
                .load(ITEM_IMAGE_BASE_URL + "${participant.item5}.png")
                .into(itemView.findViewById(R.id.item5))
            Glide.with(itemView)
                .load(ITEM_IMAGE_BASE_URL + "${participant.item6}.png")
                .into(itemView.findViewById(R.id.item6))


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParticipantAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_match, parent, false))
    }

    override fun onBindViewHolder(holder: ParticipantAdapter.ViewHolder, position: Int) {
        holder.bind(participants[position])
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    companion object {
        private const val WIN_COLOR = "#8EC0E8"
        private const val LOSE_COLOR = "#DA8680"
        private const val CHAMPION_SQUARE_IMAGE_BASE_URL =
            "https://ddragon.leagueoflegends.com/cdn/12.6.1/img/champion/"
        private const val ITEM_IMAGE_BASE_URL =
            "https://ddragon.leagueoflegends.com/cdn/12.6.1/img/item/"
        private const val SUMMONER_SPELL_IMAGE_BASE_URL =
            "https://ddragon.leagueoflegends.com/cdn/12.6.1/img/spell/"
        val QUEUE = hashMapOf<Int,String>(
            0 to "연습모드",
            400 to "일반",
            420 to "솔랭",
            430 to "일반",
            440 to "자랭",
            450 to "무작위 총력전",
            700 to "격전",
            830 to "ai",
            840 to "ai",
            850 to "ai",
            900 to "URF",
            920 to "포로왕",
            1020 to "포로왕",
            1300 to "돌격 넥서스",
            1400 to "궁극기 주문서",
            2000 to "튜토리얼",
            2010 to "튜토리얼",
            2020 to "튜토리얼",
        )
        val SPELLS = hashMapOf<Int,String>(
            21 to "SummonerBarrier",
            1 to "SummonerBoost",
            14 to "SummonerDot",
            3 to "SummonerExhaust",
            4 to "SummonerFlash",
            6 to "SummonerHaste",
            7 to "SummonerHeal",
            13 to "SummonerMana",
            30 to "SummonerPoroRecall",
            31 to "SummonerPoroThrow",
            11 to "SummonerSmite",
            39 to "SummonerSnowURFSnowball_Mark",
            32 to "SummonerSnowball",
            12 to "SummonerTeleport",
            54 to "Summoner_UltBookPlaceholder",
            55 to "Summoner_UltBookSmitePlaceholder"
        )
    }

}