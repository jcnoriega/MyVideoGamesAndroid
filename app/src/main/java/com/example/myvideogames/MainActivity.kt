package com.example.myvideogames

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.myvideogames.data.GameTrailer
import com.example.myvideogames.databinding.ActivityMainBinding
import com.example.myvideogames.ui.mediaplayer.MediaPlayerContainerListener
import com.example.myvideogames.ui.mediaplayer.MediaPlayerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MediaPlayerContainerListener {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayerFragment = MediaPlayerFragment.newInstance()

    private val onBackPressedCallback = object: OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            viewModel.onBackPressed()
        }
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment_activity_main
        ) as NavHostFragment

        val navController = navHostFragment.navController
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
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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
        viewModel.collapsed.observe(this) { isCollapsed ->
            onBackPressedCallback.isEnabled = !isCollapsed //we disable it is collapsed
        }
    }

    override fun launchMediaPlayer(gameTrailer: GameTrailer) {
        val fragment = supportFragmentManager.findFragmentByTag(MediaPlayerFragment.TAG)
        if (fragment == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.player_fragment, mediaPlayerFragment, MediaPlayerFragment.TAG)
                .commitAllowingStateLoss()
        }
        viewModel.setMediaSource(gameTrailer)
        onBackPressedCallback.isEnabled = true
        val motionLayout = binding.container
        if (ViewCompat.isLaidOut(motionLayout)) {
            motionLayout.transitionToEnd()
        } else {
            motionLayout.post { motionLayout.transitionToEnd() }
        }
    }
}