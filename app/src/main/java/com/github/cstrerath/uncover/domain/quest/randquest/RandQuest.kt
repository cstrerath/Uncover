package com.github.cstrerath.uncover.domain.quest.randquest

import com.github.cstrerath.uncover.data.database.entities.Location

data class RandQuest(
    val id: Int,
    val location: Location,
    val textString: String
)