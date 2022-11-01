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

import androidx.room.*
import com.example.forage.model.Forageable
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for database interaction.
 */
@Dao
interface ForageableDao {

    // method that returns a Flow<List<Forageable>> for every row in the database
    @Query("SELECT * FROM forageable_database ORDER BY name ASC")
    fun getForageables(): Flow<List<Forageable>>

    // method that takes in an id
    // returns the Flow<Forageable> that is associated with the id
    @Query("SELECT * from forageable_database WHERE id = :id")
    fun getForageable(id: Long): Flow<Forageable>

    // method that takes in Forageable
    // inserts new Forageable into the database using OnConflictStrategy.REPLACE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forageable: Forageable)

    // method that takes in Forageable that exists in the database
    // updates existing Forageable in the database
    @Update
    suspend fun update(forageable: Forageable)

    // method that takes in Forageable as a parameter
    // deletes Forageable from the database
    @Delete
    suspend fun delete(forageable: Forageable)
}
