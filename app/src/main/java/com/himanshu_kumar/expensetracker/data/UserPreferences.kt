package com.himanshu_kumar.expensetracker.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore("user_preferences")
class UserPreferences(private val context:Context) {
    private val dataStore = context.dataStore

    companion object{
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val IS_FIRST_TIME_KEY = booleanPreferencesKey("is_first_time")
    }

    // save userName
    suspend fun saveUserName(name:String){
       dataStore.edit {
           preferences->
           if(preferences[IS_FIRST_TIME_KEY] != false)
           {
               preferences[USER_NAME_KEY] = name
               preferences[IS_FIRST_TIME_KEY] = false
           }
       }
    }

    // get userName
   val userName:Flow<String?> = dataStore.data.map {
       preferences->
       preferences[USER_NAME_KEY]
   }

   // check is first time
   val isFirstTime:Flow<Boolean> = dataStore.data.map {
       preferences->
       preferences[IS_FIRST_TIME_KEY] ?: true
   }
}