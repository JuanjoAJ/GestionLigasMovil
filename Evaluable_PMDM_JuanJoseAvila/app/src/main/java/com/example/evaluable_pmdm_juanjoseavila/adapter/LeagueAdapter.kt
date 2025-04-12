package com.example.evaluable_pmdm_juanjoseavila.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.evaluable_pmdm_juanjoseavila.R
import com.example.evaluable_pmdm_juanjoseavila.model.League

class LeagueAdapter(
    var list: ArrayList<League>,
    private val listener: OnLeagueListener
) : RecyclerView.Adapter<LeagueAdapter.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameLeague)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_league, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val league = list[position]
        holder.name.text = league.strLeague
        holder.name.setOnClickListener {
            listener.onLeagueClick(league)
        }
    }

    fun addLeague(league: League) {
        this.list.add(league)
        notifyItemInserted(list.size - 1)
    }

    interface OnLeagueListener {
        fun onLeagueClick(league: League)
    }
}
