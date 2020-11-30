package com.szareckii.popularlibraries.navigation

import com.szareckii.popularlibraries.ui.fragment.ConverterFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class UsersScreen(): SupportAppScreen() {
        override fun getFragment() = ConverterFragment.newInstance()
    }
}