package com.umc.personal

import android.app.Application
import android.content.Context

class App : Application() {
    
    init {
        instance = this
    }

    companion object {
        private var instance : App? = null

        fun context() : Context {
            return instance!!.applicationContext
        }
    }
}