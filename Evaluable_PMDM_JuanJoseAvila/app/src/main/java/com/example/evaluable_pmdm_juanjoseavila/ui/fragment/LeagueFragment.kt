package com.example.evaluable_pmdm_juanjoseavila.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.evaluable_pmdm_juanjoseavila.R
import com.example.evaluable_pmdm_juanjoseavila.adapter.LeagueAdapter
import com.example.evaluable_pmdm_juanjoseavila.databinding.FragmentLeagueBinding
import com.example.evaluable_pmdm_juanjoseavila.model.League
import com.google.gson.Gson
import org.json.JSONArray

class LeagueFragment : Fragment(), LeagueAdapter.OnLeagueListener {

    private lateinit var binding: FragmentLeagueBinding
    private lateinit var adapter: LeagueAdapter
    private lateinit var listLeagues: ArrayList<League>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeagueBinding.inflate(inflater, container, false)
        instances()

        // JSON Request
        val request = JsonObjectRequest(
            "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php",
            { response ->
                val array: JSONArray = response.getJSONArray("leagues")
                val gson = Gson()
                for (i in 0 until array.length()) {
                    val leagueJSON = array.getJSONObject(i)
                    val league = gson.fromJson(leagueJSON.toString(), League::class.java)
                    if (league.strSport.equals("Soccer", ignoreCase = true)
                        && !league.strLeague.equals("_No League")) {
                        adapter.addLeague(league)
                    }
                }
            },
            { Log.v("error", "Fallo en la obtención de ligas") }
        )
        Volley.newRequestQueue(requireContext()).add(request)

        return binding.root
    }

    private fun instances() {

        listLeagues = ArrayList()
        adapter = LeagueAdapter(listLeagues, this)
        binding.recyclerLeague.adapter = adapter
        binding.recyclerLeague.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun onLeagueClick(league: League) {
        val bundle = Bundle()
        bundle.putSerializable("league", league)
        findNavController().navigate(R.id.action_leagueFragment_to_teamFragment, bundle)
    }
}
