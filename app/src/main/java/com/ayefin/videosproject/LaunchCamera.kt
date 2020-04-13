package com.ayefin.videosproject

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.ayefin.videosproject.databinding.ActivityCameraBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Mode
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class LaunchCamera : AppCompatActivity(){
    lateinit var cameraBinding : ActivityCameraBinding
    private lateinit var cameraView : CameraView
    lateinit var videoFile : File
    var VIDEO_CAPTURE_STARTED = "N"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera)

        videoFile = createVideoFile()

        cameraView = cameraBinding.cameraView
        cameraView.setLifecycleOwner(this)
        cameraView.open()
        cameraView.mode = Mode.VIDEO

        cameraView.addCameraListener(object : CameraListener(){

            override fun onVideoTaken(result: VideoResult) {
                super.onVideoTaken(result)

                uploadVideo(result.file.toUri())
            }

        })
        cameraBinding.ivStartVideoCapture.setOnClickListener {
            if("N".equals(VIDEO_CAPTURE_STARTED)) {
                VIDEO_CAPTURE_STARTED = "Y"
                cameraView.takeVideoSnapshot(videoFile)
            } else{
                VIDEO_CAPTURE_STARTED = "N"
                cameraView.stopVideo()
            }
        }
    }

    private fun uploadVideo(absolutePath: Uri) {
        var progBar = ProgressBar(this)
        progBar = cameraBinding.pbCamera
        progBar.visibility = View.VISIBLE

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "videos/MP4_$timeStamp"

        var storageRef : StorageReference = FirebaseStorage.getInstance().reference.child(imageFileName)
        storageRef.putFile(absolutePath).addOnSuccessListener {
            progBar.visibility = View.GONE
            Toast.makeText(this, "FileUploaded", Toast.LENGTH_LONG).show()
            this.finish()
        }
            .addOnFailureListener{
                progBar.visibility = View.GONE
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                cameraView.open()
            }
    }

    private fun createVideoFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "MP4_$timeStamp"
        val storageDir: File = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        val video = File.createTempFile(
            imageFileName,  /* prefix */
            SUFFX,  /* suffix */
            storageDir /* directory */
        )
        return video
    }

    override fun onRestart() {
        super.onRestart()

    }

    override fun onResume() {
        super.onResume()
        cameraView.open()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraView.destroy()

    }

    override fun onPause() {
        super.onPause()
        cameraView.close()
    }
}