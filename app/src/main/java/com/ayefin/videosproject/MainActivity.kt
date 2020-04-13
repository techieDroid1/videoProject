package com.ayefin.videosproject

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayefin.videosproject.adapters.VideoListAdapter
import com.ayefin.videosproject.databinding.ActivityMainBinding
import com.ayefin.videosproject.models.VideoModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mainBinding : ActivityMainBinding

    private var mStorageRef: StorageReference? = null

    val modelList = arrayListOf<VideoModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.ivCaptureVideo.setOnClickListener(this)

        mStorageRef = FirebaseStorage.getInstance().reference

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivCaptureVideo -> startCameraActivity()
        }
    }

    private fun startCameraActivity() {
        val intent = Intent(this, LaunchCamera::class.java)

        startActivityForResult(intent, REQUEST_CAMERA_VIDEO)

    }

    private fun getFirebaseVideos(){
        var storage = FirebaseStorage.getInstance()
        var ref : StorageReference = storage.getReference().child("videos/")

        ref.listAll().addOnSuccessListener {
            for (item in it.items){
                val temp = VideoModel(item.name, item)
                modelList.add(temp)
            }

            mainBinding.rvVideoList.layoutManager = LinearLayoutManager(this)
            mainBinding.rvVideoList.adapter = VideoListAdapter(this, modelList)
        }



    }

    override fun onResume() {
        super.onResume()
        getFirebaseVideos()
    }
}
