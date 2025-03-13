package com.example.mygame.features.endgamepopup.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygame.features.menuview.manager.GameManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EndGamePopupViewModel @Inject constructor(
    private val gameManager: GameManager
) : ViewModel() {

    fun saveValues(coins: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val number = gameManager.getGameNumber().toInt()
            val cons: Int = gameManager.getCoin().toInt()
            val sum: Int = cons +coins
            val gameNum: Int = number+1
            gameManager.saveGame(gameNum.toString(), sum.toString())
        }
    }
}