package com.example.demoapp.ui.home

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.demoapp.R
import com.example.demoapp.databinding.ActivityMainBinding
import com.example.demoapp.datalayer.model.DownloadData
import com.example.demoapp.datalayer.model.MoviesResponse
import com.example.demoapp.datalayer.model.Video
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.nio.channels.Channels


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var lstData: MutableList<Video>
    private lateinit var adapter: DemoAdapter
    private lateinit var lstDownloadedFileDAta: MutableList<DownloadData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        );
        initView()
        getfiles()
        getDataFromAsset()
        setUpAdapter()
    }

    private fun getfiles() {
        var filenames = arrayOfNulls<String>(0)
        val path = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()
        )
        if (path.exists()) {
            filenames = path.list()
        }
        for (i in filenames.indices) {
            var downloadData = DownloadData()
            downloadData.fileName = filenames[i]
            downloadData.fileName = path.path.toString() + "/" + filenames[i]
            lstDownloadedFileDAta.add(downloadData)
        }
    }

    private fun setUpAdapter() {
        adapter = DemoAdapter(fun(item: Video, position: Int, type: Int, path: String) {
            when (type) {
                R.id.btbDownload -> {
                    if (item.isDownloaded) {
                        Log.e("abc", "" + position + item.title)
                        val intent = Intent(this, VideoDisplayActivity::class.java).apply {
                            putExtra("url", path)
                        }
                        startActivity(intent)
                    } else {
                        downloadFromUrl(item.sources[0], item.title)
                    }
                }
            }
        })
        binding.rvMain.adapter = adapter
        adapter.setData(lstData, lstDownloadedFileDAta)
    }

    fun downloadFile(url: URL, outputFileName: String) {
        url.openStream().use {
            Channels.newChannel(it).use { rbc ->
                FileOutputStream(outputFileName).use { fos ->
                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                }
            }
        }
    }


    private fun downloadFromUrl(url: String, title: String) {
        try {
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle(title)
            request.setDescription("Downloading Your File")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "$title.mp4"
            )
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        } catch (exception: Exception) {
            Log.e("abccustom", "" + exception.localizedMessage)

        }
    }

    private fun initView() {
        lstData = ArrayList()
        lstDownloadedFileDAta = ArrayList()
    }


    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Fetching the download id received with the broadcast
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (this@MainActivity::adapter.isLateinit) {
                getfiles()
                adapter.setData(lstData, lstDownloadedFileDAta)
            }
        }
    }

    private fun getDataFromAsset() {
        var string: String? = ""
        try {
            val inputStream: InputStream = assets.open("DemoResponse.txt")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            string = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val response = Gson().fromJson(string, MoviesResponse::class.java)
        Log.e("abc", "" + response.categories.size)
        Log.e("abc", "" + response.categories.size)
        response.categories.forEach {
            Log.e("abc", "" + it.name)

            lstData = it.videos.toMutableList()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }
}