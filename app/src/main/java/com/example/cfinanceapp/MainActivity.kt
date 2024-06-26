package com.example.cfinanceapp


import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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



        // Here we hide the bottom navigation view in some those specific fragments.
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment -> viewBinding.bottomNavigationView.visibility = View.GONE
                R.id.registerFragment -> viewBinding.bottomNavigationView.visibility = View.GONE
                R.id.loginFragment -> viewBinding.bottomNavigationView.visibility = View.GONE
                R.id.transactionsFragment -> viewBinding.bottomNavigationView.visibility = View.GONE
                R.id.transactionsDetailFragment -> viewBinding.bottomNavigationView.visibility =
                    View.GONE

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