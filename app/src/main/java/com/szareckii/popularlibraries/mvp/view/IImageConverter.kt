package com.szareckii.popularlibraries.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface IImageConverter: MvpView {
    fun init()
    fun showMessage(text: String)
    fun openImageDialog()
    fun readIt(uri: String)
    fun convertIt()
}