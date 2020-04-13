package com.ayefin.videosproject.adapters

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.ayefin.videosproject.R
import com.ayefin.videosproject.models.VideoModel
import com.bumptech.glide.Glide

class VideoListAdapter(val activity : Activity, val list: ArrayList<VideoModel>) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mView = itemView
        var tvName = itemView.findViewById<TextView>(R.id.rvTvName)
        var rvVideoView = itemView.findViewById<VideoView>(R.id.rvVideoView)
        var ivThumbnail = itemView.findViewById<ImageView>(R.id.ivThumbnail)
        var ivPlay = itemView.findViewById<ImageView>(R.id.ivPlayButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_videos_list, parent, false)
        return VideoListAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(h: ViewHolder, position: Int) {
        h.tvName.text = list.get(position).vidName
        list.get(position).ref.downloadUrl.addOnSuccessListener {
            Glide.with(activity).load(it).into(h.ivThumbnail)
            h.ivPlay.setOnClickListener {onlick ->
                h.ivThumbnail.visibility = View.GONE
                h.ivPlay.visibility = View.GONE
                h.rvVideoView.setVideoURI(it)
                h.rvVideoView.visibility = View.VISIBLE
                h.rvVideoView.start()
                h.rvVideoView.setOnCompletionListener {
                    h.ivThumbnail.visibility = View.VISIBLE
                    h.ivPlay.visibility = View.VISIBLE
                    h.rvVideoView.visibility = View.GONE
                }
            }
        }

        Log.i("recyclerview", "recyclerview  : " + list.get(position).vidName)
    }
}