package com.ayefin.videosproject.models

import android.net.Uri
import com.google.firebase.storage.StorageReference
import java.io.Serializable

data class VideoModel(var vidName : String , var ref : StorageReference) : Serializable