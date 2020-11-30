package com.szareckii.popularlibraries.mvp.presenter

import com.szareckii.popularlibraries.mvp.model.repo.ImageModel
import com.szareckii.popularlibraries.mvp.view.IImageConverter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router

class ConverterPresenter(val router: Router, val model: ImageModel): MvpPresenter<IImageConverter>() {

    val compositeDisposable = CompositeDisposable()

    val imageModel: ImageModel = ImageModel()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun addImage() {
        viewState.openImageDialog()
    }

    fun onUriFile(uriString: String) {
        imageModel.addImage(uriString)
        viewState.readIt(uriString)
    }

    fun result(success: Boolean) {
        if (success) {
            viewState.showMessage("Файл сконвертирован")
        } else {
            viewState.showMessage("Ошибка конвертации файла")
        }
    }

    fun readImage(uriString: String, tryRead: Boolean) {
        viewState.showMessage("Загрузка пошла")
        if (tryRead) {
            viewState.convertIt()
        } else {
            viewState.showMessage("Ошибка открытия файла")
        }
    }

    fun backClick(): Boolean {
        router.exit()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}