package com.idodanieli.playit

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.MODE_LOCAL
import com.idodanieli.playit.games.chess.MODE_ONLINE
import com.idodanieli.playit.games.chess.game_listener.GameListener
import com.idodanieli.playit.games.chess.logic.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var localPlayButton: Button
    private lateinit var onlinePlayButton: Button

    // Flow starts here
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()

        val games = createGames()

        GameClient.initialize("http://192.168.1.33:5000")

        initUI(games)
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
        localPlayButton = findViewById(R.id.localPlayButton)
        onlinePlayButton = findViewById(R.id.onlinePlayButton)
    }

    private fun playButtonOnClick(mode: String) {
        disableScrolling()

        val chessView = viewPager.currentChessview()
        chessView.setMode(mode)
        chessView.invalidate()
    }

    private fun initUI(games: List<Game>) {
        localPlayButton.setOnClickListener() {
            playButtonOnClick(MODE_LOCAL)
        }

        onlinePlayButton.setOnClickListener {
            playButtonOnClick(MODE_ONLINE)
        }

        val gameListener = object : GameListener {
            override fun onGameOver(winner: Player) {
                showGameOverDialog(winner)
            }
        }

        viewPager.adapter = PageviewAdapter(games, gameListener)
    }

    private fun showGameOverDialog(winner: Player) {
        val dialogBuilder = AlertDialog.Builder(this)

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
        localPlayButton.visibility = View.INVISIBLE
        onlinePlayButton.visibility = View.INVISIBLE
    }
    private fun enableScrolling() {
        viewPager.isUserInputEnabled = true
        localPlayButton.visibility = View.VISIBLE
        onlinePlayButton.visibility = View.INVISIBLE
    }
}