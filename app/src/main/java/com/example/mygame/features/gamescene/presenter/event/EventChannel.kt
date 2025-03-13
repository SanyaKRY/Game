package com.example.mygame.features.gamescene.presenter.event

sealed class EventChannel {
    data class ShowToast(val text: String): EventChannel()
}
