package com.example.mygame.features.gamescene.presenter.vm

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygame.R
import com.example.mygame.features.gamescene.presenter.event.CardClickEvent
import com.example.mygame.features.gamescene.presenter.event.Event
import com.example.mygame.features.gamescene.presenter.event.EventChannel
import com.example.mygame.features.gamescene.presenter.model.Card
import com.example.mygame.features.gamescene.presenter.model.GameStopWatch
import com.example.mygame.features.gamescene.presenter.model.MemoryState
import com.example.mygame.features.gamescene.presenter.model.MyGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class GameSceneViewModel @Inject constructor() : ViewModel() {

    private var stopWatch: CountDownTimer? = null

    init {
        startStopWatch()
    }

    private fun startStopWatch() {
        viewModelScope.launch(Dispatchers.Main) {
            // 1 час, каждую секунду
            stopWatch = object : CountDownTimer(1000L * 60L * 60L, 1_000) {
                override fun onTick(timer: Long) {
                    _stopWatchStateFlow.value =
                        _stopWatchStateFlow.value.copy(
                            currentSec = _stopWatchStateFlow.value.currentSec + 1,
                            currentCoin = setCoin(_stopWatchStateFlow.value.currentSec + 1, _stopWatchStateFlow.value.currentCoin)
                        )


                }
                override fun onFinish() {
                }
            }
            stopWatch?.start()
        }
    }

    private fun setCoin(sec: Long, coin: Int): Int {
        if (sec < 61) {
            return 100
        }
        print("QQQQQQQ"+(sec> 60)+"  "+(coin > 10))
        if ((sec > 60) and (coin > 10)) {
            return coin - 5
        }

        return 10
    }

    private val _stopWatchStateFlow: MutableStateFlow<GameStopWatch> =
        MutableStateFlow(GameStopWatch())
    val stopWatchStateFlow: Flow<GameStopWatch>
        get() = _stopWatchStateFlow





//    var numberCanClicked = 4
//    var list = mutableListOf<Int>()
//    val default = R.drawable.default_background_card
//    val myGame: MyGame = MyGame()
    private val eventChannel = Channel<EventChannel>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _state: MutableStateFlow<MemoryState> = MutableStateFlow(MemoryState())
    val state: Flow<MemoryState>
        get() = _state
    private var delayedCompareJob: Job? = null

//    private fun setDefaultCards(): List<Card> {
//        val list: MutableList<Card> = mutableListOf()
//        for(i in 0..19) {
//            list.add(i, Card(R.drawable.default_background_card))
//        }
//        return list
//    }

    fun handleEvent(event: Event) {
        when (event) {
            is CardClickEvent -> onCardClick(event.cardId)
        }
    }

    private fun compareValues(first: Int?, second: Int?){
        val cards = _state.value.cards.copyOf()
        if(second != null && first != null){
            val card1 = cards[first]
            val card2 = cards[second]

            if(card1.value != card2.value){
                cards[first].flipCard()
                cards[second].flipCard()
            }else{
                cards[first].matchFound = true
                cards[second].matchFound = true
                _state.value = _state.value.copy(
                    cards = cards,
                    pairsMatched = _state.value.pairsMatched + 1
                )
            }
        }

        isGuessedAllCards()
        resetCompareCards()
    }

    private fun isGuessedAllCards() {
        if (_state.value.pairCount == _state.value.pairsMatched){
            viewModelScope.launch (Dispatchers.IO) {
                eventChannel.send(EventChannel.ShowToast("Congratulation"))
            }
        }
    }

    private fun resetCompareCards(){
        if(_state.value.card2 != null){
            _state.value = _state.value.copy(card1 = null, card2 = null)
        }

//        if (_state.value.clickCount == _state.value.pairsMatched){
//            viewModelScope.launch (Dispatchers.IO) {
//                eventChannel.send(EventChannel.ShowToast("Congratulation"))
//            }
//        }
    }

    private fun cancelPreviousJob(){
        val firstIndex = _state.value.card1
        val secondIndex = _state.value.card2
        if(delayedCompareJob != null){
            delayedCompareJob?.cancel()
            compareValues(firstIndex, secondIndex)
        }
    }

    private fun increaseClickCount(){
        _state.value = _state.value.copy(
            clickCount = _state.value.clickCount + 1
        )
    }

    private fun onCardClick(id: Int){
        cancelPreviousJob()
        increaseClickCount()
        val cards = _state.value.cards

        if(cards[id].isBackDisplayed){
            delayedCompareJob = viewModelScope.launch (Dispatchers.IO) {
                flip(id)
                val firstIndex = _state.value.card1
                val secondIndex = _state.value.card2
                val bothCardsAreNotNull = firstIndex != null && secondIndex != null
                val cardsMatchSkipDelay = if(bothCardsAreNotNull) cards[firstIndex!!].value == cards[secondIndex!!].value
                else false
                if(!cardsMatchSkipDelay){
                    delay(2000)
                }
                compareValues(firstIndex, secondIndex)
            }
        }


    }

    private fun flip(id: Int){
        val cards = _state.value.cards.copyOf()
        cards[id].flipCard()
        val card2 = _state.value.card1
        _state.value = _state.value.copy(
            card1 = id,
            card2 = card2,
            cards = cards
        )
    }



//    init {
//        setListOfCard()
//        setMyGame()
//    }

    private fun setListOfCard() {
        val listOfImages = mutableListOf<Int>(
            R.drawable.ipo, R.drawable.ipo,
            R.drawable.interest, R.drawable.interest,
            R.drawable.cash, R.drawable.cash,
            R.drawable.index, R.drawable.index,
            R.drawable.law, R.drawable.law,
            R.drawable.loss, R.drawable.loss,
            R.drawable.my_category, R.drawable.my_category,
            R.drawable.online, R.drawable.online,
            R.drawable.p2p, R.drawable.p2p,
            R.drawable.profit, R.drawable.profit,
            )
        listOfImages.shuffle()
//        list.addAll(listOfImages)
    }

//    private fun setMyGame() {
//        val myGame: MyGame = MyGame( list = list.map { Card(default, false) })
//        _myGameStateFlow.value = myGame
//    }
//
//
//    fun clickOnCard(id: Int) {
//        val cards = _myGameStateFlow.value.cards
//        if(cards[id].isTurnedOver) {
//            flip(id)
//
//        }
//    }

//    private fun flip(id: Int){
//        val cards = _state.value.cards.copyOf()
//        cards[id].flipCard()
//        val card2 = _state.value.card1
//        _state.value = _state.value.copy(
//            card1 = id,
//            card2 = card2,
//            cards = cards
//        )
//    }
}