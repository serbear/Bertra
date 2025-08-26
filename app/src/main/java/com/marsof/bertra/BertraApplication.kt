package com.marsof.bertra

import android.app.Application
import com.marsof.bertra.data.BertraDataContainer

class BertraApplication : Application() {
    lateinit var container: BertraDataContainer

    override fun onCreate() {
        super.onCreate()
        container = BertraDataContainer(this)
    }
}