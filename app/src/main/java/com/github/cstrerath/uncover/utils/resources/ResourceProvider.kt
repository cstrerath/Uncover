package com.github.cstrerath.uncover.utils.resources

import android.content.Context
import android.util.Log

class ResourceProvider(private val context: Context) {
    companion object {
        private const val TAG = "ResourceProvider"
    }

    fun getString(resId: Int): String {
        return try {
            context.getString(resId).also {
                Log.d(TAG, "Retrieved string resource: $resId")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to retrieve string resource: $resId", e)
            ""
        }
    }
}
