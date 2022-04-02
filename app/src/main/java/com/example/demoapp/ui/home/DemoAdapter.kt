package com.example.demoapp.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.demoapp.R
import com.example.demoapp.databinding.ItemDataBinding
import com.example.demoapp.datalayer.model.DownloadData
import com.example.demoapp.datalayer.model.Video
import com.example.demoapp.utils.extension.isNetworkAvailable


class DemoAdapter(private val listener: (Video, Int, Int, String) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var lstData = emptyList<Video>()
    var lstDownloadData = emptyList<DownloadData>()

    fun setData(items: List<Video>, lstDownlaad: MutableList<DownloadData>) {
        lstData = items
        lstDownloadData = lstDownlaad
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return lstData.size
    }

    inner class ViewHolder(val binding: ItemDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Video, listener: (Video, Int, Int, String) -> Unit, position: Int
        ) {
/*            Glide.with()
                .load(item.thumb) // image url
                .placeholder(R.drawable.ic_icon) // any placeholder to load at start
                .error(R.drawable.ic_icon)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(binding.ivMain);*/
            binding.tvTitle.text = item.title
            binding.tvDesc.text = item.description
            binding.tvSubtitle.text = item.subtitle

            for (i in lstDownloadData.indices) {
                lstData[position].path = ""
                if (lstDownloadData[i].fileName.toString().contains(item.title)) {
                    binding.btbDownload.setText("Play")
                    binding.btbDownload.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorPrimary
                        )
                    )
                    binding.btbDownload.isClickable = true
                    binding.btbDownload.isEnabled = true
                    lstData[position].path = lstDownloadData[i].fileName.toString()
                    break
                } else {
                    binding.btbDownload.setText("download")
                }
            }


            if (item.isDownloadStarted) {
                binding.btbDownload.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.light_gray
                    )
                )
                binding.btbDownload.isEnabled = false

            } else {
                binding.btbDownload.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorPrimary
                    )
                )
            }

            Glide.with(itemView.context).load(item.sources.get(0)).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(binding.ivMain)

            binding.btbDownload.setOnClickListener {

                if (binding.btbDownload.text == "Play") {
                    item.isDownloaded = true
                    listener.invoke(item, position, R.id.btbDownload, lstData[position].path)
                } else {
                    if (itemView.context.isNetworkAvailable()) {

                        lstData[position].isDownloadStarted = true
                        notifyItemChanged(position)
                        item.isDownloaded = false
                        listener.invoke(item, position, R.id.btbDownload, lstData[position].path)
                    } else {
                        Toast.makeText(
                            itemView.context,
                            "No internet connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }


            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(lstData[position], listener, position)
    }

}