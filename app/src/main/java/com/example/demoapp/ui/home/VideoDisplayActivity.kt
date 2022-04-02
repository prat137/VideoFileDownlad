package com.example.demoapp.ui.home

import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.URLUtil
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.demoapp.R
import com.example.demoapp.databinding.ActivityVideoBinding


class VideoDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding
    private var videoUrl = ""


    private var mCurrentPosition = 0
    private val PLAYBACK_TIME = "play_time"

    override fun onStart() {
        super.onStart()
        videoUrl = intent.getStringExtra("url").toString()
        Log.e("abc", videoUrl)
        initializePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PLAYBACK_TIME, binding.videoView.currentPosition)
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            binding.videoView.pause()
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME)
        }

        // Set up the media controller widget and attach it to the video view.

        // Set up the media controller widget and attach it to the video view.
        val controller = MediaController(this)
        controller.setMediaPlayer(binding.videoView)
        binding.videoView.setMediaController(controller)


    }

    private fun initializePlayer() {
        // Show the "Buffering..." message while the video loads.
        binding.tvBuffering.visibility = VideoView.VISIBLE

        // Buffer and decode the video sample.
        //  val videoUri: Uri = getMedia(videoUrl)

        binding.videoView.setVideoPath(videoUrl);
        binding.videoView.start();

//        binding.videoView.setVideoURI(videoUri)

        // Listener for onPrepared() event (runs after the media is prepared).
        binding.videoView.setOnPreparedListener(
            OnPreparedListener { // Hide buffering message.
                binding.tvBuffering.visibility = VideoView.INVISIBLE

                // Restore saved position, if available.
                if (mCurrentPosition > 0) {
                    binding.videoView.seekTo(mCurrentPosition)
                } else {
                    // Skipping to 1 shows the first frame of the video.
                    binding.videoView.seekTo(1)
                }

                // Start playing!
                binding.videoView.start()
            })

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        binding.videoView.setOnCompletionListener(
            OnCompletionListener {
                Toast.makeText(
                    this,
                    "finish",
                    Toast.LENGTH_SHORT
                ).show()

                // Return the video position to the start.
                binding.videoView.seekTo(0)
            })
    }


    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
    private fun releasePlayer() {
        binding.videoView.stopPlayback()
    }

    // Get a Uri for the media sample regardless of whether that sample is  
    // embedded in the app resources or available on the internet.  
    private fun getMedia(mediaName: String): Uri {
        return if (URLUtil.isValidUrl(mediaName)) {
            // Media name is an external URL.  
            Uri.parse(mediaName)
        } else {

            // you can also put a video file in raw package and get file from there as shown below  
            Uri.parse(
                "android.resource://" + packageName +
                        "/raw/" + mediaName
            )
        }
    }


}