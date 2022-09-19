package com.siroca.todo09.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.siroca.todo09.R
import com.siroca.todo09.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavController()
        initClicker()
        manageToolbar()
    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun manageToolbar() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.onBoardFragment -> {
                    binding.toolbar.isVisible = false
                }
            }
        }
    }

    private fun initClicker() {
        with(binding) {
            toolbarBtn.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
                toolbar.title = "Профиль"
            }
        }
    }
}