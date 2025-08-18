package com.marsof.bertra.data

import android.content.Context

class BertraDataContainer(private val context: Context) {
    val trainDao: TrainDao by lazy {
        BertraDatabase.getDatabase(context).trainDao()
    }
}