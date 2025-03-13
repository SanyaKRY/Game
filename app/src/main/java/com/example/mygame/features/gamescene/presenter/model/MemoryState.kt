package com.example.mygame.features.gamescene.presenter.model

data class MemoryState(
    val cards: Array<MemoryCard> = generateCardsArray(10),
    val card1: Int? = null,
    val card2: Int? = null,
    val pairCount: Int = 10,
    val pairsMatched: Int = 0,
    val clickCount: Int = 0,
//    val currentTheme: HolidayTheme = ThanksgivingTheme()
)

fun generateCardsArray(matches: Int): Array<MemoryCard> {
    val singles = 1..matches
    val doubles = singles + singles
    return doubles.shuffled().map { MemoryCard(it) }.toTypedArray()
}
