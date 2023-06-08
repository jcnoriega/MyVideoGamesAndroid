package com.example.myvideogames

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.myvideogames.data.model.GameTrailer
import com.example.myvideogames.databinding.ActivityMainBinding
import com.example.myvideogames.ui.mediaplayer.MediaPlayerContainerListener
import com.example.myvideogames.ui.mediaplayer.MediaPlayerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MediaPlayerContainerListener {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayerFragment = MediaPlayerFragment.newInstance()
    private lateinit var navController: NavController

    private var isCollapsed = false

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_activity_main
        ) as NavHostFragment

        navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        observeTransition()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.player_fragment, mediaPlayerFragment, MediaPlayerFragment.TAG)
            .hide(mediaPlayerFragment)
            .commitAllowingStateLoss()
    }

    private fun observeTransition() {
        viewModel.transitionProgress.observe(this) {
            val motionLayout = binding.container
            if (ViewCompat.isLaidOut(motionLayout)) {
                motionLayout.progress = 1f - it
            } else {
                motionLayout.post { motionLayout.progress = 1f - it }
            }
        }
        // We have to make sure the transition is complete to avoid blank page
        viewModel.collapsed.observe(this) {
            isCollapsed = it
            if (isCollapsed) {
                binding.container.transitionToStart()
            } else {
                binding.container.transitionToEnd()
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentByTag(MediaPlayerFragment.TAG)?.isVisible == true) {
            if (isCollapsed) {
                super.onBackPressed()
            } else {
                //TODO: check this to avoid clipping on top error
                viewModel.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun launchMediaPlayer(gameTrailer: GameTrailer) {

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .show(mediaPlayerFragment).commit()
        viewModel.setMediaSource(gameTrailer)
        //onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        val motionLayout = binding.container
        if (ViewCompat.isLaidOut(motionLayout)) {
            motionLayout.transitionToEnd()
        } else {
            motionLayout.post { motionLayout.transitionToEnd() }
        }
    }
}