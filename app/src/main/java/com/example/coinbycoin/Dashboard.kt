package com.example.coinbycoin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.room.Room
import com.example.coinbycoin.databinding.ActivityDashboardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Dashboard : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var usuarioViewModel: UsuarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDashboard.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as
                NavHostFragment
        val navController = navHostFragment.navController
        // Definir los destinos de nivel superior del menú de hamburguesa
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_dashboard, R.id.nav_perfil, R.id.nav_ingresos, R.id.nav_reporte), drawerLayout
        )
        val usuarioId: Long = intent.getLongExtra("usuario_id", -1)
        val bundle = Bundle().apply {
            putLong("usuario_id", usuarioId)
        }


        Log.d("DashboarActivity", "Id Usuario: $usuarioId")
// Configurar la ActionBar para que cambie con el NavController
        setupActionBarWithNavController(navController,appBarConfiguration)
// Configurar el NavigationView para que navegue con el NavController
        navView.setupWithNavController(navController)
        navController.setGraph(R.navigation.mobile_navigation, bundle)

        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)

        usuarioViewModel.getUsuarioPorId(usuarioId).observe(this) { usuario ->
            // Usuario encontrado, actualiza los TextViews en el nav_header_dashboard

            val navHeader = findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)
            val nombreUsuarioTextView = navHeader.findViewById<TextView>(R.id.NombreUsuario)
            val usuarioTextView = navHeader.findViewById<TextView>(R.id.Usuario)
            usuario?.let {
                Log.d("DashboardActivity", "usuario encontrado")
                nombreUsuarioTextView.text = "${it.nombres} ${it.apellidos}"
                usuarioTextView.text = it.usuario
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflar el menú; esto agrega elementos a la ActionBar si está presente.
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
