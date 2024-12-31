package com.github.cstrerath.uncover.data.database.initialization.data

import com.github.cstrerath.uncover.data.database.entities.Location

object QuestLocationData {
    fun getAllLocations() = listOf(
        // Quest 1: DHBW -> BASF
        Location(1, 49.47297803570765, 8.535173407950223),  // DHBW Learning Center Eingang
        Location(2, 49.49593423311647, 8.431272436505415),  // BASF Besucherzentrum
        Location(3, 49.48272000923852, 8.464351658349386),  // Universitätsbibliothek Mannheim (Schloss)

// Quest 2: Hauptbahnhof -> Wasserturm -> Lidl
        Location(4, 49.47705343043826, 8.432418595211635),  // Ludwigshafen Hauptbahnhof
        Location(5, 49.48289555163139, 8.476367083713155),  // Wasserturm Außenbereich
        Location(6, 49.47426506655806, 8.53374564113204),  // Lidl DHBW

// Quest 3: Kulturhalle -> Asia Trade
        Location(7, 49.48565997460245, 8.528981354072782),  // Kulturhalle Feudenheim
        Location(8, 49.46973471028306, 8.434653832446136),  // Weg zum Asia Trade
        Location(9, 49.45666713682467, 8.429591109206267),  // Asia Trade Eingang

// Quest 4: Großmarkthalle -> Technoseum
        Location(10, 49.47311828759486, 8.496575218097348),  // Großmarkthalle Außenbereich
        Location(11, 49.47623366569589, 8.495674234136057),  // Technoseum Straßenbahnhaltestelle
        Location(12, 49.4759312154969, 8.497260166922345),  // Technoseum Haupteingang

// Quest 5: Planetarium -> Luisenpark
        Location(13, 49.47792652281293, 8.491381837197133),  // Planetarium Eingang
        Location(14, 49.47879209454876, 8.496128329556747),  // Luisenpark Haupteingang
        Location(15, 49.48305067368438, 8.490826545242466),  // Luisenpark Vorplatz
// Quest 6: Fernmeldeturm -> BASF
        Location(16, 49.48687890856305, 8.49337057262413),  // Fernmeldeturm Aussichtspunkt
        Location(17, 49.492594596728196, 8.437178404530306),  // BASF Tor 7 (öffentlich)
        Location(18, 49.49593423311647, 8.431272436505415),  // BASF Besucherzentrum

// Quest 7: Carl-Benz-Stadion
        Location(19, 49.47813342891891, 8.502956494636273),  // Stadion Haupteingang
        Location(20, 49.48048162592764, 8.50368033710593),  // Stadion Vorplatz Ost
        Location(21, 49.47946043156593, 8.49895431534689),  // Stadion Vorplatz West
// Quest 8: Wasserturm (Rückkehr)
        Location(22, 49.484602620459505, 8.474055873390489),  // Wasserturm Platz
        Location(23, 49.48396615575223, 8.475820251407065),  // Wasserturm Ostseite
        Location(24, 49.483073281365705, 8.478095370428441),  // Wasserturm Brunnen

// Quest 9: Friedrichsplatz
        Location(25, 49.484634391762945, 8.476334814152864),  // Friedrichsplatz Zentrum
        Location(26, 49.48339179091861, 8.477976133117322),  // Friedrichsplatz Ost
        Location(27, 49.48373120282102, 8.474302974611293),  // Friedrichsplatz West

// Quest 10: DHBW Learning Center (Finale)
        Location(28, 49.47463228182847, 8.53446310833228),  // DHBW Haupteingang
        Location(29, 49.47303855876596, 8.53518586147973),  // DHBW Bibliothek
        Location(30, 49.47322268247364, 8.535214817204267),   // DHBW Learning Center

// Mannheim
        Location(100, 49.48901, 8.46697),  // Paradeplatz Zentrum
        Location(101, 49.48559, 8.47753),  // Marktplatz
        Location(102, 49.48325, 8.45823),  // Rheinpromenade
        Location(103, 49.47936, 8.47016),  // Hauptbahnhof Vorplatz
        Location(104, 49.48663, 8.48221),  // Neckaruferbebauung Süd
        Location(105, 49.49073, 8.47827),  // Alter Messplatz
        Location(106, 49.47583, 8.52289),  // Hochschule Mannheim Eingang
        Location(107, 49.47731, 8.49327),  // MVV Hochhaus Vorplatz
        Location(108, 49.48235, 8.48673),  // Herzogenriedpark Haupteingang
        Location(109, 49.48893, 8.46983),  // Kunsthalle Vorplatz

// Ludwigshafen
        Location(110, 49.48033, 8.44327),  // Berliner Platz
        Location(111, 49.47742, 8.44492),  // Berliner Platz Süd
        Location(112, 49.47955, 8.43821),  // Rhein-Galerie Eingang
        Location(113, 49.47633, 8.42983),  // Lutherplatz
        Location(114, 49.48322, 8.43255),  // Hemshofpark

// Weitere Mannheim
        Location(115, 49.49234, 8.47926),  // Neckarstadt Alter Meßplatz
        Location(116, 49.48456, 8.47893),  // Kapuzinerplanken
        Location(117, 49.47823, 8.48234),  // Universitätsklinikum Haupteingang
        Location(118, 49.48734, 8.46583),  // Schillerplatz
        Location(119, 49.47456, 8.51783),  // Hans-Reschke-Ufer

// Weitere Ludwigshafen
        Location(120, 49.47844, 8.44673),  // Ludwigsplatz
        Location(121, 49.48211, 8.43677),  // Friedenspark
        Location(122, 49.47533, 8.42789),  // Rheinallee Park
        Location(123, 49.48455, 8.43123),  // Ernst-Bloch-Platz
        Location(124, 49.47922, 8.44234)   // Stadtbibliothek Ludwigshafen
    )
}
