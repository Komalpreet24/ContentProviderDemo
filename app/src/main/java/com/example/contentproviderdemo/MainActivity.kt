package com.example.contentproviderdemo

import android.content.ContentUris
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.Calendar
import java.util.Date
import android.Manifest
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

  private lateinit var viewModel: ImagesVIewModel
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    ActivityCompat.requestPermissions(
      this,
      arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
      0
    )

    viewModel = ViewModelProvider(this).get(ImagesVIewModel::class.java)


    val projection = arrayOf(
      MediaStore.Images.Media._ID,
      MediaStore.Images.Media.DISPLAY_NAME
    )

    val millisYesterday = Calendar.getInstance().apply {
      add(Calendar.DAY_OF_YEAR, -1)
    }.timeInMillis

    val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"

    val selectionArgs = arrayOf(millisYesterday.toString())

    viewModel.imageLiveData.observe(this, Observer{
      println("Printing Images")
      println(it)
    })


    val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

    contentResolver.query(
      MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
      projection,
      selection,
      selectionArgs,
      sortOrder
    )?.use {  cursor ->

        val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val displayNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)

        val images = mutableListOf<Image>()

        while(cursor.moveToNext()){
          val id = cursor.getLong(idColumn)
          val displayName = cursor.getString(displayNameColumn)
          val uri = ContentUris.withAppendedId(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            id
          )
          images.add(Image(id, displayName, uri))
        }

        viewModel.updateImages(images)

    }

  }
}

data class Image(
  val id: Long,
  val name: String,
  val uri: Uri
)