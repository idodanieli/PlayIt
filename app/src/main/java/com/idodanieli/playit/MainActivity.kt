package com.idodanieli.playit

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.idodanieli.playit.games.chess.logic.Game
import com.idodanieli.playit.games.chess.logic.GameParser
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.ui.GameListener
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this

        val gameParser = GameParser()
        val games = getGameJSONS().map { gameParser.parse(it) }.shuffled()

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val gameListener = object : GameListener {
            override fun onGameOver(winner: Player) {
                viewPager.showDialog(context, winner)
            }
        }

        viewPager.adapter = PageviewAdapter(games, gameListener)

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

fun ViewPager2.showDialog(context: Context, winner: Player) {
    val dialogBuilder = AlertDialog.Builder(context)

    // Set dialog title and message
    dialogBuilder.setTitle("GAME OVER")
    dialogBuilder.setMessage("$winner is the winner!")

    // Set positive button with click listener
    dialogBuilder.setPositiveButton("NEW GAME") { dialog, _ ->
        // Handle positive button click
        setCurrentItem(currentItem + 1, true)
        dialog.dismiss()
    }

    // Set negative button with click listener
    dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
        // Handle negative button click
        dialog.dismiss()
    }

    // Create and show the dialog
    val dialog = dialogBuilder.create()
    dialog.show()
}