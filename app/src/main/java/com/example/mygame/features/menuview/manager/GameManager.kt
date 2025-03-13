package com.example.mygame.features.menuview.manager

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mygame.features.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GameManager(private val context: Context) {

    fun getCoins(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[GAME_COIN] ?: "0"
        }
    }

    fun getNumbers(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[GAME_NUMBER] ?: "1"
        }
    }

    suspend fun saveGame(gameNumber: String, gameCoin: String) {
        context.dataStore.edit { preferences ->
            preferences[GAME_NUMBER] = gameNumber
            preferences[GAME_COIN] = gameCoin
        }
    }

    suspend fun getCoin(): String {
        return context.dataStore.data.first()[GAME_COIN] ?: "0"
    }
    suspend fun getGameNumber(): String {
        return context.dataStore.data.first()[GAME_NUMBER] ?: "1"
    }


    companion object {
        private val GAME_NUMBER = stringPreferencesKey("game_number")
        private val GAME_COIN = stringPreferencesKey("game_coin")
    }
}