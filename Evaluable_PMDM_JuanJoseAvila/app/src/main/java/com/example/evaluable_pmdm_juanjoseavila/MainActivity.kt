package com.example.evaluable_pmdm_juanjoseavila

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavOptions
import com.example.evaluable_pmdm_juanjoseavila.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Configuramos el comportamiento del botón de navegación según el destino actual.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // Cuando estemos en el MainFragment, el botón del toolbar llevará al LoginFragment
                R.id.leagueFragment -> {

                    binding.toolbar.setNavigationOnClickListener {
                        // Con setPopUpTo eliminamos MainFragment de la pila (inclusive = true)
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.leagueFragment, inclusive = true)
                            .build()
                        navController.navigate(R.id.action_leagueFragment_to_loginFragment, null, navOptions)
                        auth.signOut()
                    }
                }
                // En cualquier otro destino, se usa el comportamiento por defecto
                else -> {
                    binding.toolbar.setNavigationOnClickListener {
                        navController.navigateUp()
                    }
                }
            }
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.to_Login -> {
                auth.signOut()
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.loginFragment)
                true
            }
            R.id.to_FavFragment -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.favFragment)
                true
            }
            R.id.to_Close -> {
                auth.signOut()
                finishAffinity() // Cierra la aplicación completamente
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
