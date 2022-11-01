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
package com.example.forage.ui.viewmodel

import androidx.lifecycle.*
import com.example.forage.data.ForageableDao
import com.example.forage.model.Forageable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Shared [ViewModel] to provide data to the [ForageableListFragment], [ForageableDetailFragment],
 * and [AddForageableFragment] and allow for interaction the the [ForageableDao]
 */

class ForageableViewModel(
    // Passing an instance of ForageableDao as a parameter into the view model constructor
    private val forageableDaoIns: ForageableDao
): ViewModel() {

    /* variable of type LiveData<List<Forageable>> that gets the entire list of forageables with the DAO,
     * and converts it to LiveData.
    */
    val allForageables: LiveData<List<Forageable>> = forageableDaoIns.getForageables().asLiveData()


    /* method that takes in an id of type Long and returns a LiveData<Forageable> from the database,
     * using getForageable() method from the DAO, and converts Forageable into LiveData
     */
    fun get(id: Long): LiveData<Forageable> {
        return forageableDaoIns.getForageable(id).asLiveData()
    }

    fun addForageable(
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        val forageable = Forageable(
            name = name,
            address = address,
            inSeason = inSeason,
            notes = notes
        )

    /* Launches a coroutine using the viewModelScope and calls the insert method from the DAO
     * to insert the Forageable into the database.
    */
    viewModelScope.launch {
        forageableDaoIns.insert(forageable)
    }

    }

    fun updateForageable(
        id: Long,
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        val forageable = Forageable(
            id = id,
            name = name,
            address = address,
            inSeason = inSeason,
            notes = notes
        )
        viewModelScope.launch(Dispatchers.IO) {
            // calls the DAO update method to update a forageable in the database
            forageableDaoIns.update(forageable)
        }
    }

    fun deleteForageable(forageable: Forageable) {
        viewModelScope.launch(Dispatchers.IO) {
            // calls the DAO delete method to delete a forageable in the database
            forageableDaoIns.delete(forageable)
        }
    }

    fun isValidEntry(name: String, address: String): Boolean {
        return name.isNotBlank() && address.isNotBlank()
    }
}

// A view model factory that creates an instance of ForagaeableViewModel using a ForageableDao constructor parameter
class ForageableViewModelFactory( private val forageableDaoIns: ForageableDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom((ForageableViewModel::class.java))){
            return ForageableViewModel(forageableDaoIns) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}