/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.forage.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.forage.model.Forageable
import kotlinx.coroutines.CoroutineScope

/**
 * Room database to persist data for the Forage app.
 * This database stores a [Forageable] entity
 */

// database that exposes Forageable entities from DAO to Room
@Database(entities = [Forageable::class], version = 1, exportSchema = false)
public abstract class ForageDatabase : RoomDatabase(){
    // returns ForageableDao
    abstract fun forageableDao(): ForageableDao

    /*
     * companion object that creates a private variable, INSTANCE, and a getDatabase() function that
     * returns the ForageDatabase instance.
     */
    companion object{
        @Volatile
        private var INSTANCE: ForageDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): ForageDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    ForageDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
