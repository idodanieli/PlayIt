package com.idodanieli.playit

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.idodanieli.playit.games.chess.logic.GameParser
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameParser = GameParser()
        val games = getGameJSONS().map { gameParser.parse(it) }.shuffled()

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

    private fun getGameJSONS(): List<JSONObject> {
        val files = arrayListOf<JSONObject>()
        val fields: Array<Field> = R.raw::class.java.fields
        fields.forEach {
            val resourceID = it.getInt(it)
            val file = resources.openRawResource(resourceID).bufferedReader(Charsets.UTF_8).use { it.readText() }
            try {
                val json = JSONObject(file)
                files.add(json)
            } catch (e: JSONException) {
                // do nothing
                Log.d("JsonException()", e.toString())
            }
        }

        return files
    }
}