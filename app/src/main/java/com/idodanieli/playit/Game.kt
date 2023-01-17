package com.idodanieli.playit

class Game (gameTitle: String){
    private var title: String

    init {
        title = gameTitle
    }

    fun getTitle(): String {
        return title
    }
}