package com.example.evaluable_pmdm_juanjoseavila.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.evaluable_pmdm_juanjoseavila.R
import com.example.evaluable_pmdm_juanjoseavila.adapter.LeagueAdapter.OnLeagueListener
import com.example.evaluable_pmdm_juanjoseavila.model.Team

class TeamAdapter(private var list:ArrayList<Team>, private val listener: OnTeamClickListener):RecyclerView.Adapter<TeamAdapter.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.nameTeam)
        val image: ImageView = itemView.findViewById(R.id.imageTeam)
        val btnFav: Button = itemView.findViewById(R.id.btnFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
      val  team = list[position]
        holder.name.text = team.strTeam;
        Glide.with(holder.itemView).load(team.strBadge).placeholder(R.drawable.placeholder_logo).into(holder.image)
        holder.btnFav.setOnClickListener {
            listener.onFavoriteClicked(team)
        }
    }

    fun addTeam(team: Team)
    {
        list.add(team)
        notifyItemInserted(list.size - 1)
    }

    interface OnTeamClickListener {
        fun onFavoriteClicked(team: Team)
    }
}