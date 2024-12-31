package com.github.cstrerath.uncover.data.database.initialization

interface DatabaseInitializer {
    suspend fun initialize()
    fun getInitializerName(): String
}
