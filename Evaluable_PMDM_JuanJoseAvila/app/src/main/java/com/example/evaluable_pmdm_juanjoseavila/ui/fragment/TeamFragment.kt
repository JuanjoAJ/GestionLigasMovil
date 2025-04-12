package com.example.evaluable_pmdm_juanjoseavila.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.evaluable_pmdm_juanjoseavila.R
import com.example.evaluable_pmdm_juanjoseavila.adapter.LeagueAdapter
import com.example.evaluable_pmdm_juanjoseavila.adapter.TeamAdapter
import com.example.evaluable_pmdm_juanjoseavila.databinding.FragmentLeagueBinding
import com.example.evaluable_pmdm_juanjoseavila.databinding.FragmentTeamBinding
import com.example.evaluable_pmdm_juanjoseavila.model.League
import com.example.evaluable_pmdm_juanjoseavila.model.Team
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import org.json.JSONArray

class TeamFragment:Fragment(), TeamAdapter.OnTeamClickListener {

    private lateinit var binding: FragmentTeamBinding
    private var league: League? =null;
    private lateinit var adapterTeam:TeamAdapter
    private lateinit var listTeams: ArrayList<Team>
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()


        binding = FragmentTeamBinding.inflate(inflater, container, false)
        instances()
        if(league!=null){
            val request = JsonObjectRequest(
                "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=${league!!.strLeague}",
                {
                    val teams = it.getJSONArray("teams")
                    val gson = Gson()
                    for (i in 0..<teams.length()) {
                        val teamJSON = teams.getJSONObject(i)
                        val team = gson.fromJson(teamJSON.toString(), Team::class.java)
                       adapterTeam.addTeam(team)
                    }
                },
                { Log.v("error", "Fallo en la obtención de equipos") }
            )
            Volley.newRequestQueue(requireContext()).add(request)

        }

        return binding.root
    }



    override fun onStart() {
        super.onStart()

    }

    private fun instances() {
        listTeams = ArrayList()
        adapterTeam = TeamAdapter(listTeams, this)
        binding.recyclerTeam.adapter = adapterTeam
        binding.recyclerTeam.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        database = FirebaseDatabase.getInstance("https://ligasapp-d6424-default-rtdb.europe-west1.firebasedatabase.app/")
        this.league = arguments?.getSerializable("league") as League
        (activity as AppCompatActivity).supportActionBar?.title = "Equipos de ${league?.strLeague ?: "la liga"}"
        if(league!=null)
            this.league!!.strLeague = league!!.strLeague!!.replace(" ", "%20")
    }

    override fun onFavoriteClicked(team: Team) {


                database.reference.child("users").child(auth.currentUser!!.uid).child("favorite")
                    .child(team.idTeam.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Snackbar.make(binding.root, "${team.strTeam} ya es favorito", Snackbar.LENGTH_SHORT).show()
                        } else {
                            database.reference.child("users").child(auth.currentUser!!.uid).child("favorite")
                                .child(team.idTeam.toString()).setValue(team)
                                .addOnSuccessListener {
                                    Snackbar.make(binding.root, "${team.strTeam} agregado a favoritos", Snackbar.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Snackbar.make(binding.root, "Error al agregar favorito", Snackbar.LENGTH_SHORT).show()
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Snackbar.make(requireView(), "Error al comprobar favorito", Snackbar.LENGTH_SHORT).show()
                    }
                })


    }


}