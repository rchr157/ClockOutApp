package com.example.clockout

import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.clockout.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {  //

    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null
    private var toolNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Toolbar
        setSupportActionBar(toolbar)


        // Original code
//        val navController = findNavController(R.id.nav_host_fragment_container)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.homeFragment, R.id.dashboardFragment, R.id.notificationsFragment))  // , R.id.settingsFragment, R.id.aboutFragment
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        val bottomNavView: BottomNavigationView = findViewById(R.id.nav_view)
        val navHostFragment = supportFragmentManager.
        findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfigurationIds = setOf(R.id.home,
            R.id.notifications, R.id.dashboard)
        val appBarConfiguration = AppBarConfiguration(appBarConfigurationIds)
        bottomNavView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        // Working
//        if (savedInstanceState == null) {
//            setupBottomNavigationBar()
//        } // Else, need to wait for onRestoreInstanceState
    }


    // Working
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        // Now that BottomNavigationBar has restored its instance state
//        // and its selectedItemId, we can proceed with setting up the
//        // BottomNavigationBar with Navigation
//        setupBottomNavigationBar()
//    }

    // Working
//    private fun setupBottomNavigationBar() {
//        val bottomNavView: BottomNavigationView = findViewById(R.id.nav_view)
//
//        val navGraphIds = listOf(R.navigation.home, R.navigation.dashboard,
//                R.navigation.notifications, R.navigation.settings, R.navigation.settings)
//        // Setup the bottom navigation view with a list of navigation graphs
//        val controller = bottomNavView.setupWithNavController(
//                navGraphIds = navGraphIds,
//                fragmentManager = supportFragmentManager,
//                containerId = R.id.nav_host_fragment_container,
//                intent = intent
//        )
//        // Whenever the selected controller changes, setup the action bar
//        controller.observe(this, Observer { navController ->
//            setupActionBarWithNavController(navController)
//            // optional NavigationView for Drawer implementation
////            navView.setupWithNavController(navController)
//        })
//        currentNavController = controller
//    }

    // Working
//    override fun onSupportNavigateUp(): Boolean {
//        return currentNavController?.value?.navigateUp() ?: false
//    }


    // Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_container)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    // Working
    override fun onDestinationChanged(
            controller: NavController,
            destination: NavDestination,
            arguments: Bundle?) {
//        if (destination.id in controller) {
//            appBarLayout.setExpanded(true, true)
//        }
    }


}