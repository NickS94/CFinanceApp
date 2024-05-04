package com.example.cfinanceapp


import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.isGone
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cfinanceapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        viewBinding.bottomNavigationView.setupWithNavController(navHost.navController)



        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                com.google.android.material.R.id.design_bottom_sheet -> viewBinding.bottomNavigationView.visibility = View.GONE
                R.id.detailsFragment -> viewBinding.bottomNavigationView.visibility = View.GONE
                R.id.registerFragment -> viewBinding.bottomNavigationView.visibility = View.GONE
                R.id.loginFragment -> viewBinding.bottomNavigationView.visibility = View.GONE
                else -> viewBinding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewBinding.fragmentContainerView.findNavController().navigateUp()
            }
        })




    }
}