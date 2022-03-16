package com.gregorchristiaens.introduction

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.gregorchristiaens.introduction.databinding.ActivityLauncherBinding

class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //TODO Due to a bug the findNavController breaks when working with FragmentContainer.Check if this has been fixed otherwise keep this workaround.
        //val navController = findNavController(R.id.nav_host_fragment_content_launcher)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_launcher) as NavHostFragment
        navHostFragment.navController
    }

}