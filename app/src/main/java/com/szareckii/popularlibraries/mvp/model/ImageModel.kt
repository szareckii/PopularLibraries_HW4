package com.szareckii.popularlibraries.mvp.model.repo

class ImageModel {

    var imageCurrent: String? = null

    fun addImage(uri: String) {
        imageCurrent = uri
    }

}