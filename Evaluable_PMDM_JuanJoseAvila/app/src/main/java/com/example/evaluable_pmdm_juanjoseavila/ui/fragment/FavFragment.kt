package com.example.evaluable_pmdm_juanjoseavila.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evaluable_pmdm_juanjoseavila.adapter.FavoriteAdapter
import com.example.evaluable_pmdm_juanjoseavila.databinding.FragmentFavBinding
import com.example.evaluable_pmdm_juanjoseavila.model.Team
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavFragment : Fragment(), FavoriteAdapter.OnFavClickListener {

    private lateinit var binding: FragmentFavBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adapterFav: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        binding = FragmentFavBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance("https://ligasapp-d6424-default-rtdb.europe-west1.firebasedatabase.app/")

        setupRecyclerView()
        loadFavoriteTeams()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapterFav = FavoriteAdapter(arrayListOf(), this)
        binding.recyclerFav.adapter = adapterFav
        binding.recyclerFav.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadFavoriteTeams() {
        val userId = auth.currentUser?.uid ?: return
        database.reference.child("users").child(userId).child("favorite")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favoriteTeams = mutableListOf<Team>()
                    for (teamSnapshot in snapshot.children) {
                        val team = teamSnapshot.getValue(Team::class.java)
                        team?.let { favoriteTeams.add(it) }
                    }
                    adapterFav.updateList(favoriteTeams) // Actualiza el RecyclerView
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseDB", "Error al obtener equipos favoritos", error.toException())
                }
            })
    }

    override fun onDeleteClicked(team: Team) {
        val userId = auth.currentUser?.uid ?: return
        database.reference.child("users").child(userId).child("favorite").child(team.idTeam.toString())
            .removeValue()
            .addOnSuccessListener {
                Log.d("FirebaseDB", "${team.strTeam} eliminado de favoritos")
            }
            .addOnFailureListener {
                Log.e("FirebaseDB", "Error al eliminar equipo", it)
            }
    }
}
