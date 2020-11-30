package com.szareckii.popularlibraries.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.szareckii.popularlibraries.R
import com.szareckii.popularlibraries.mvp.model.repo.ImageModel
import com.szareckii.popularlibraries.mvp.presenter.ConverterPresenter
import com.szareckii.popularlibraries.mvp.view.IImageConverter
import com.szareckii.popularlibraries.ui.AndroidConverter
import com.szareckii.popularlibraries.ui.App
import com.szareckii.popularlibraries.ui.BackButtonListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_converter.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class ConverterFragment : MvpAppCompatFragment(), IImageConverter, BackButtonListener {

    private val IMAGE_REQUEST = 4578
    lateinit var androidConverter: AndroidConverter
    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance() = ConverterFragment()
    }

    private val presenter: ConverterPresenter by moxyPresenter {
        ConverterPresenter(App.instance.router, ImageModel())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        View.inflate(context, R.layout.fragment_converter, null)

    override fun init() {
        button_converter.setOnClickListener {
            presenter.addImage()
        }

        androidConverter = context?.let { AndroidConverter(it) }!!
    }

    override fun openImageDialog() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedFile = data!!.data.toString()
            presenter.onUriFile(selectedFile)
        }
    }

    override fun readIt(uri: String) {
        androidConverter.readImage(uri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    presenter.readImage(uri, true)
                }, {
                    it.printStackTrace()
                    presenter.readImage(uri, false)
                }).addTo(compositeDisposable)
    }

    override fun convertIt() {
        androidConverter.convertImage()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    presenter.result(true)
                }, {
                    it.printStackTrace()
                    presenter.result(false)
                }).addTo(compositeDisposable)
    }

    override fun showMessage(text: String) {
        textView_status.text = text
    }

    override fun backPressed() = presenter.backClick()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}