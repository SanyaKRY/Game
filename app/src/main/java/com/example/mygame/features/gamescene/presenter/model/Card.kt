package com.example.mygame.features.gamescene.presenter.model

data class Card(
    var cardImage: Int,
    var isTurnedOver: Boolean = true,
    var matchFound: Boolean = false
)
