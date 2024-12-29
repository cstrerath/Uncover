package com.github.cstrerath.uncover.data.database.initialization.data

import com.github.cstrerath.uncover.data.database.entities.Location

object QuestLocationData {
    fun getAllLocations() = listOf(
        Location(1, 49.47433, 8.53472),  // DHBW Mannheim
        Location(2, 49.49715, 8.43306),  // BASF-Tor
        Location(3, 49.47734, 8.43439),  // Ludwigshafen Hauptbahnhof
        Location(4, 49.47377, 8.51435),  // Carl-Benz-Stadion
        Location(5, 49.48382, 8.47645),  // Wasserturm
        Location(6, 49.48327, 8.47827),  // Rosengarten
        Location(7, 49.48475, 8.47375),  // Kunsthalle
        Location(8, 49.48361, 8.46023),  // Mannheimer Schloss
        Location(9, 49.48696, 8.47824),  // Neckarwiese
        Location(10, 49.48379, 8.46296),  // Uni Mannheim
        Location(11, 49.5, 8.5) // Test :)
    )
}
