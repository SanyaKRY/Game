package com.example.mygame.features.menuview.presenter.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygame.features.menuview.manager.GameManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MenuViewViewModel @Inject constructor(
    private val gameManager: GameManager
) : ViewModel() {

    val gameNumber = MutableLiveData<String>()
    val coinNumber = MutableLiveData<String>()

    init {
        getGameNumber()
        getCoinNumber()
    }

    fun getGameNumber() {
        viewModelScope.launch(Dispatchers.IO) {
            gameManager.getNumbers().collect {
                withContext(Dispatchers.Main) {
                    gameNumber.value = it
                }
            }
        }
    }

    fun getCoinNumber() {
        viewModelScope.launch(Dispatchers.IO) {
            gameManager.getCoins().collect {
                withContext(Dispatchers.Main) {
                    coinNumber.value = it
                }
            }
        }
    }
}