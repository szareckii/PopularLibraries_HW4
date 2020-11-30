package com.szareckii.popularlibraries.ui.activity

import android.os.Bundle
import com.szareckii.popularlibraries.R
import com.szareckii.popularlibraries.mvp.presenter.MainPresenter
import com.szareckii.popularlibraries.mvp.view.MainView
import com.szareckii.popularlibraries.ui.App
import com.szareckii.popularlibraries.ui.BackButtonListener
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class MainActivity : MvpAppCompatActivity(), MainView {

    val navigatorHolder = App.instance.navigatorHolder
    val navigator = SupportAppNavigator(this, supportFragmentManager, R.id.container)

    val presenter by moxyPresenter {
        MainPresenter(App.instance.router)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
         supportFragmentManager.fragments.forEach{
             if(it is BackButtonListener && it.backPressed()){
                 return
             }
         }
        presenter.backClick()
    }
}