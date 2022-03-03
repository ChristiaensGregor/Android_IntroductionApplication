package com.gregorchristiaens.introduction

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.gregorchristiaens.introduction.databinding.ActivityLauncherBinding

class LaunchActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        //TODO Due to a bug the findNavController breaks when working with FragmentContainer.
        //TODO Check if this has been fixed otherwise keep this workaround.
        //val navController = findNavController(R.id.nav_host_fragment_content_launcher)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_launcher) as NavHostFragment
        val navController = navHostFragment.navController
        //Original setup for appBarConfiguration using the navGraph to decide what fragments to show the upnavigation on.
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //Pass a set of top level destinations that wont display the navigate up arrow
        appBarConfiguration = AppBarConfiguration(setOf(R.id.landingFragment, R.id.loginFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        //TODO Code optionally usable on contact page or about page
        binding.fab.setOnClickListener {
            startActivity(Intent.createChooser(getSendEmailIntent(), "Choose an Email client:"))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_launcher)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    //TODO Code optionally usable on contact page or about page.
    private fun getSendEmailIntent(): Intent {
        return Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "gregorchristiaens@gmail.com", null
            )
        ).putExtra(Intent.EXTRA_SUBJECT, "Greetings from cv app")
            .putExtra(Intent.EXTRA_TEXT, "Hey I just had a look at your cv app")
    }
}