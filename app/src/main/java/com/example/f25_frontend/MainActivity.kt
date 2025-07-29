package com.example.f25_frontend

import android.Manifest
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.transition.Visibility
import com.example.f25_frontend.databinding.ActivityMainBinding
import com.example.f25_frontend.utils.FirebaseMsgUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging


/**
 * A simple activity demonstrating use of a NavHostFragment with a navigation drawer.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration : AppBarConfiguration
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                this,
                "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG,
            ).show()
        }
    }
    private fun askNotificationPermission() {
        // This is only necessary for API Level > 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navigationController = findNavController(R.id.nav_host)
        NavigationUI.setupWithNavController(binding.navBarBottom, navigationController)
        binding.btnPopBackStack.setOnClickListener{
            navigationController.popBackStack()
        }
        binding.btnNotify.setOnClickListener{
            navigationController.navigate(R.id.notifyFragment)
        }
//        scope에 로그인값이 없을 경우
        if(false){
            binding.navBarTop.visibility= View.INVISIBLE
            binding.navBarBottom.visibility= View.INVISIBLE
        }

        askNotificationPermission()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW,
                ),
            )
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.getString(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }
//         [END handle_data_extras]
//        토큰 얻기 버튼 샘플
//        findViewById<Button>(R.id.logTokenButton).setOnClickListener {
//            // Get token
//            // [START log_reg_token]
//            Firebase.messaging.token.addOnCompleteListener(
//                OnCompleteListener { task ->
//                    if (!task.isSuccessful) {
//                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                        return@OnCompleteListener
//                    }
//
//                    // Get new FCM registration token
//                    val token = task.result
//
//                    // Log and toast
//                    val msg = getString(R.string.msg_token_fmt, token)
//                    Log.d(TAG, msg)
//                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                },
//            )
//            // [END log_reg_token]
//        }

//        툴바 서포트 액션(차후 확인)
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment? ?: return

        // Set up Action Bar
        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.nav_graph)

        setupActionBar(navController, appBarConfiguration)

        setupNavigationMenu(navController)

        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }

            Toast.makeText(this@MainActivity, "Navigated to $dest",
                Toast.LENGTH_SHORT).show()
            Log.d("NavigationActivity", "Navigated to $dest")
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {

//        binding.btnExplore.setOnClickListener(){
//            findNavController().navigate(R.id.action_bottommenu_to_explore)
//        }
////      홈으로 돌아가기 버튼
//        binding.btnMainpage.setOnClickListener(){
//            findNavController().navigate(R.id.action_bottommenu_to_todo)
//        }
////      마이페이지 버튼
//        binding.btnMypage.setOnClickListener(){
//            findNavController().navigate(R.id.action_bottommenu_to_mypage)
//        }
        // TODO STEP 9.3 - Use NavigationUI to set up Bottom Nav
//        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
//        bottomNav?.setupWithNavController(navController)
        // TODO END STEP 9.3
    }

    private fun setupNavigationMenu(navController: NavController) {
        // TODO STEP 9.4 - Use NavigationUI to set up a Navigation View
//        // In split screen mode, you can drag this view out from the left
//        // This does NOT modify the actionbar
//        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
//        sideNavView?.setupWithNavController(navController)
        // TODO END STEP 9.4
    }

    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration) {
        // TODO STEP 9.6 - Have NavigationUI handle what your ActionBar displays
//        // This allows NavigationUI to decide what label to show in the action bar
//        // By using appBarConfig, it will also determine whether to
//        // show the up arrow or drawer menu icon
//        setupActionBarWithNavController(navController, appBarConfig)
        // TODO END STEP 9.6
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
//        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
//        if (navigationView == null) {
//            menuInflater.inflate(R.menu.overflow_menu, menu)
//            return true
//        }
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        // TODO STEP 9.2 - Have Navigation UI Handle the item selection - make sure to delete
        //  the old return statement above
//        // Have the NavigationUI look for an action or destination matching the menu
//        // item id and navigate there if found.
//        // Otherwise, bubble up to the parent.
//        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
//                || super.onOptionsItemSelected(item)
        // TODO END STEP 9.2
    }

    // TODO STEP 9.7 - Have NavigationUI handle up behavior in the ActionBar
//    override fun onSupportNavigateUp(): Boolean {
//        // Allows NavigationUI to support proper up navigation or the drawer layout
//        // drawer menu, depending on the situation
//        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
//    }
    // TODO END STEP 9.7


}