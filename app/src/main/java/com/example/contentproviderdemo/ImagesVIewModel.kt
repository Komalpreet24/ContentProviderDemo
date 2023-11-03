package com.example.contentproviderdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImagesVIewModel: ViewModel() {

  private var _imageLiveData = MutableLiveData<List<Image>>()
  val imageLiveData = _imageLiveData as LiveData<List<Image>>

  fun updateImages(images: List<Image>){
    _imageLiveData.value = images
  }

}