package com.szareckii.popularlibraries.mvp.model

import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Completable
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface IConverter: MvpView {
    fun readImage(uri: String): @NonNull Completable?
    fun convertImage(): @NonNull Completable?
}