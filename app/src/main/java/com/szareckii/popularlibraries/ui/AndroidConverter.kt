package com.szareckii.popularlibraries.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import com.szareckii.popularlibraries.mvp.model.IConverter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class AndroidConverter(private val cont: Context): IConverter {

    private lateinit var bmpConvert: Bitmap

    private fun read(uri: String): Boolean {
        return try {
            val inputStream: InputStream? = cont.contentResolver?.openInputStream(uri.toUri())
            Thread.sleep(2000)
            bmpConvert = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override fun readImage(uri: String): Completable = Completable.create { emitter ->
        read(uri).let{
            if (it) {
                emitter.onComplete()
            } else {
                emitter.onError(RuntimeException("Error"))
                return@create
            }
        }
    }
            .subscribeOn(Schedulers.io())

    private fun convert(): Boolean {
        return try {
            Thread.sleep(2000)
            val convertedImage =
                    File(cont.getExternalFilesDir(null).toString() + "/convertedimg.png")
            val outStream = FileOutputStream(convertedImage)
            bmpConvert.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override fun convertImage(): Completable = Completable.create { emitter ->
        convert().let{
            if (it) {
                emitter.onComplete()
            } else {
                emitter.onError(RuntimeException("Error"))
                return@create
            }
        }
    }
            .subscribeOn(Schedulers.computation())


//    override fun readImage(uri: String): Boolean {
//        return try {
//            val inputStream: InputStream? = cont.contentResolver?.openInputStream(uri.toUri())
//            Thread.sleep(2000)
//            bmpConvert = BitmapFactory.decodeStream(inputStream)
//            inputStream?.close()
//            true
//        } catch (e: IOException) {
//            e.printStackTrace()
//            false
//        }
//    }
//
//
//    override fun convertImage(uri: String): Boolean {
//        return try {
//            Thread.sleep(2000)
//            val convertedImage =
//                    File(cont.getExternalFilesDir(null).toString() + "/convertedimg.png")
//            val outStream = FileOutputStream(convertedImage)
//            bmpConvert.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//            true
//        } catch (e: IOException) {
//            e.printStackTrace()
//            false
//        }
//    }

}