package com.exmaple.wildbicycle

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.exmaple.wildbicycle.bases.BaseViewModel
import com.exmaple.wildbicycle.databinding.ActivityMainBinding
import com.exmaple.wildbicycle.ui.home.HomeFragmentDirections

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_fragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.apply {
            setupWithNavController(navController)
            setNavigationItemSelectedListener(this@MainActivity)
        }
        setObservers()
    }

    private fun setObservers() = with(viewModel) {
        navigate.observe(this@MainActivity) {
            it.getContentIfNotHandled()?.let { navigate ->
                when (navigate) {
                    BaseViewModel.Navigate.Login -> {
                        HomeFragmentDirections.actionHomeFragmentToNavLogin().let { action ->
                            findNavController(R.id.nav_host_fragment_content_main).navigate(action)
                        }
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    else -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Error en la navegacion",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_home_fragment -> {
                viewModel.singOutUser(this, { resultSingOutNormal ->
                    resultSingOutNormal.fold(
                        onSuccess = {
                            if (it) Toast.makeText(
                                applicationContext, "Se ha deslogeado con Firebase auth",
                                Toast.LENGTH_LONG
                            ).show()
                            else Toast.makeText(
                                applicationContext, "error deslogin firebase auth",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onFailure = {

                        }
                    )
                }, { resultSingOutGoogle ->
                    resultSingOutGoogle.fold(
                        onSuccess = {
                            if (it) Toast.makeText(
                                applicationContext, "Se ha deslogeado con Google",
                                Toast.LENGTH_LONG
                            ).show()
                            else Toast.makeText(
                                applicationContext, "error deslogin Google",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onFailure = {

                        }
                    )
                })
                true
            }
            else -> false
        }
    }
}