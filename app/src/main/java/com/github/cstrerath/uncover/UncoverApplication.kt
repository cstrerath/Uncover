package com.github.cstrerath.uncover

import android.app.Application
import android.util.Log

class UncoverApplication : Application() {
    companion object {
        private const val TAG = "UncoverApplication"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Initializing Uncover Application")
    }
}
