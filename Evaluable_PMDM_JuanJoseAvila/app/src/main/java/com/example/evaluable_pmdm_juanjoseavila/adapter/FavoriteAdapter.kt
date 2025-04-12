package com.example.evaluable_pmdm_juanjoseavila.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.evaluable_pmdm_juanjoseavila.R
import com.example.evaluable_pmdm_juanjoseavila.model.Team

class FavoriteAdapter(private var list: ArrayList<Team>, private val listener: OnFavClickListener) :
    RecyclerView.Adapter<FavoriteAdapter.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTeam: TextView = itemView.findViewById(R.id.nameTeam)
        val nameLeague: TextView = itemView.findViewById(R.id.nameLeague)
        val image: ImageView = itemView.findViewById(R.id.imageTeam)
        val btnDel: Button = itemView.findViewById(R.id.btnDel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_fav, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val team = list[position]
        holder.nameTeam.text = team.strTeam
        holder.nameLeague.text = team.strLeague
        Glide.with(holder.itemView).load(team.strBadge).placeholder(R.drawable.placeholder_logo).into(holder.image)

        holder.btnDel.setOnClickListener {
            listener.onDeleteClicked(team) // Llama a la función de eliminación
        }
    }

    fun updateList(newList: List<Team>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    interface OnFavClickListener {
        fun onDeleteClicked(team: Team)
    }
}
