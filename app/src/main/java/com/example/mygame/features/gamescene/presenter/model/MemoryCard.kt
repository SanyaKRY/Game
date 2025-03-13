package com.example.mygame.features.gamescene.presenter.model

data class MemoryCard(
    var value: Int,
    var isBackDisplayed: Boolean = true,
    var matchFound: Boolean = false
) {
    fun flipCard(){
        isBackDisplayed = !isBackDisplayed
    }
}
