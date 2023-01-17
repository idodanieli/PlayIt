package com.idodanieli.playit

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val game1 = Game("Chess #1")
        val game2 = Game( "Chess #2")
        val games = listOf(game1, game2)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = PageviewAdapter(games)

        val viewPagerControlButton = findViewById<Button>(R.id.pageViewControlButton)
        viewPagerControlButton.setOnClickListener() {
            if ( viewPagerControlButton.text == "Disable" ) {
                viewPagerControlButton.text = "Enable"
                viewPager.isUserInputEnabled = false
            } else {
                viewPagerControlButton.text = "Disable"
                viewPager.isUserInputEnabled = true
            }
        }
    }
}