package com.idodanieli.playit

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.logic.Game
import com.idodanieli.playit.games.chess.logic.GameParser
import com.idodanieli.playit.games.chess.logic.Player
import com.idodanieli.playit.games.chess.logic.Square
import com.idodanieli.playit.games.chess.ui.GameListener
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var playButton: Button

    // Flow starts here
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()

        val games = createGames()

        GameClient.initialize("http://192.168.1.33:5000")

        initUI(games, this)
    }

    private fun createGames(): List<Game> {
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

        return files.map { GameParser.parse(it) }.shuffled()
    }

    private fun findViews() {
        viewPager = findViewById(R.id.viewPager)
        playButton = findViewById(R.id.playButton)
    }

    private fun initUI(games: List<Game>, context: Context) {
        playButton.setOnClickListener() {
            disableScrolling()
        }

        val gameListener = object : GameListener {
            override fun onGameOver(winner: Player) {
                showGameOverDialog(context, winner)
            }

            override fun onPieceMoved(origin: Square, dst: Square) {
                GameClient.getInstance().movePiece(origin, dst)
            }
        }

        viewPager.adapter = PageviewAdapter(games, gameListener)
    }

    private fun showGameOverDialog(context: Context, winner: Player) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle("GAME OVER")
        dialogBuilder.setMessage("$winner is the winner!")

        dialogBuilder.setPositiveButton("NEW GAME") { dialog, _ ->
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            enableScrolling()

            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun disableScrolling() {
        viewPager.isUserInputEnabled = false
        playButton.visibility = View.INVISIBLE
    }
    private fun enableScrolling() {
        viewPager.isUserInputEnabled = true
        playButton.visibility = View.VISIBLE
    }
}