package com.mbmc.fiinfo.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/*
Types update

previously
    NONE, // 0
    AIRPLANE_ON, // 1
    AIRPLANE_OFF, // 2
    BOOT, // 3
    DISCONNECTED, // 4
    MOBILE, // 5
    MOBILE_OFF, // 6
    SHUTDOWN, // 7
    WIFI, // 8
    WIFI_MOBILE, // 9
    WIFI_ON, // 10
    WIFI_OFF // 11
now
    UNKNOWN // 0
    AIRPLANE_OFF // 1
    AIRPLANE_ON // 2
    CARRIER // 3
    CELLULAR_OFF // 4
    CELLULAR_ON // 5
    DISCONNECTED // 6
    PHONE_OFF // 7
    PHONE_ON // 8
    WIFI // 9
    WIFI_CARRIER // 10
    WIFI_OFF // 11
    WIFI_ON // 12
 */
// TODO: some mccmnc and operator are stored as "" instead of null
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Fix erroneous empty fields
        database.execSQL("UPDATE event SET name = \"n/a\" WHERE name is null AND (type = 8 OR type = 9)")
        // Move name to mobile if event is carrier
        database.execSQL("UPDATE event SET mobile = name, name = null WHERE type = 5")
        // Move types to temp ones
        database.execSQL("UPDATE event SET type = 102 WHERE type = 1")
        database.execSQL("UPDATE event SET type = 101 WHERE type = 2")
        database.execSQL("UPDATE event SET type = 108 WHERE type = 3")
        database.execSQL("UPDATE event SET type = 106 WHERE type = 4")
        database.execSQL("UPDATE event SET type = 103 WHERE type = 5")
        database.execSQL("UPDATE event SET type = 104 WHERE type = 6")
        database.execSQL("UPDATE event SET type = 109 WHERE type = 8")
        database.execSQL("UPDATE event SET type = 110 WHERE type = 9")
        database.execSQL("UPDATE event SET type = 112 WHERE type = 10")
        // Move temp types to their final ones
        database.execSQL("UPDATE event SET type = 2 WHERE type = 102")
        database.execSQL("UPDATE event SET type = 1 WHERE type = 101")
        database.execSQL("UPDATE event SET type = 8 WHERE type = 108")
        database.execSQL("UPDATE event SET type = 6 WHERE type = 106")
        database.execSQL("UPDATE event SET type = 3 WHERE type = 103")
        database.execSQL("UPDATE event SET type = 4 WHERE type = 104")
        database.execSQL("UPDATE event SET type = 9 WHERE type = 109")
        database.execSQL("UPDATE event SET type = 10 WHERE type = 110")
        database.execSQL("UPDATE event SET type = 12 WHERE type = 112")
        // Replace empty with null
        database.execSQL("UPDATE event SET name = null WHERE name = '' OR name = \"\"")
        database.execSQL("UPDATE event SET mobile = null WHERE mobile = '' OR mobile = \"\"")
        database.execSQL("UPDATE event SET speed = null WHERE speed = '' OR speed = \"\"")
        // Replace mobile (cellular) with mccmnc
        // SPRINT -> 310120, T_MOBILE -> 310260, THREE_UK -> 23420, US_CELLULAR -> 311580
        database.execSQL("UPDATE event SET mobile = '310120' WHERE mobile = 'Sprint'")
        database.execSQL("UPDATE event SET mobile = '310260' WHERE mobile = 'T-Mobile'")
        database.execSQL("UPDATE event SET mobile = '23420' WHERE mobile = 'Three UK'")
        database.execSQL("UPDATE event SET mobile = '311580' WHERE mobile = 'US Cellular'")
        // Map speed to int
        database.execSQL("ALTER TABLE event ADD COLUMN speed2 INTEGER")
        database.execSQL("UPDATE event SET speed2 = 0 WHERE speed = 'Unknown'")
        database.execSQL("UPDATE event SET speed2 = 1 WHERE speed = '2G (GPRS)'")
        database.execSQL("UPDATE event SET speed2 = 2 WHERE speed = '2G (EDGE)'")
        database.execSQL("UPDATE event SET speed2 = 3 WHERE speed = '3G (UMTS)'")
        database.execSQL("UPDATE event SET speed2 = 4 WHERE speed = '2G (CDMA)'")
        database.execSQL("UPDATE event SET speed2 = 6 WHERE speed = '3G (EVDO_A)'")
        database.execSQL("UPDATE event SET speed2 = 7 WHERE speed = '2G (1xRTT)'")
        database.execSQL("UPDATE event SET speed2 = 8 WHERE speed = '3G (HSDPA)'")
        database.execSQL("UPDATE event SET speed2 = 9 WHERE speed = '3G (HDUPA)'")
        database.execSQL("UPDATE event SET speed2 = 10 WHERE speed = '3G (HSPA)'")
        database.execSQL("UPDATE event SET speed2 = 13 WHERE speed = '4G (LTE)'")
        database.execSQL("UPDATE event SET speed2 = 14 WHERE speed = '3G (EHRDP)'")
        database.execSQL("UPDATE event SET speed2 = 14 WHERE speed = '3G (EHRPD)'")
        database.execSQL("UPDATE event SET speed2 = 15 WHERE speed = '3G (HSPAP)'")
        database.execSQL("UPDATE event SET speed2 = 18 WHERE speed = 'IWLAN'")
        database.execSQL("UPDATE event SET speed2 = 20 WHERE speed = '5G (NR)'")
        // Remove temp table if exists
        database.execSQL("DROP TABLE IF EXISTS event2")
        // Drop country, add extra fields
        database.execSQL("CREATE TABLE event2 (_id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER NOT NULL, timestamp TEXT NOT NULL, timezone TEXT NOT NULL, ssid TEXT, frequency TEXT, mccmnc TEXT, operator TEXT, speed INTEGER)")
        // Rename fields
        database.execSQL("INSERT INTO event2 (type, timestamp, timezone, ssid, mccmnc, speed) SELECT cast(type AS SHORT), date, timeZone, name, mobile, speed2 FROM event")
        // Delete original database
        database.execSQL("DROP TABLE event")
        // Rename temp database
        database.execSQL("ALTER TABLE event2 RENAME TO event")
    }
}