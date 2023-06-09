package com.idodanieli.playit

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.idodanieli.playit.activities.RegisterActivity
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.MODE_LOCAL
import com.idodanieli.playit.games.chess.MODE_ONLINE
import com.idodanieli.playit.games.chess.game_listener.GameListener
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.ui.ChessView
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var localPlayButton: Button
    private lateinit var createGameButton: Button
    private lateinit var joinGameButton: Button
    private lateinit var findGameButton: Button
    private lateinit var gameIDEditText: EditText

    private val gameListener = object : GameListener {
        override fun onGameOver(winner: Player) {
            showGameOverDialog(winner)
        }

        override fun onGameSelected(chessView: ChessView, gameID: String) {
            val gameClient = GameClient.getInstance()

            val player = if (gameClient.join(gameID) == GameClient.PLAYER_WHITE) Player.WHITE else Player.BLACK
            chessView.setGameHero(player)

            waitForOpponentToJoin(chessView)
        }

        private fun waitForOpponentToJoin(chessView: ChessView) {
            val gameClient = GameClient.getInstance()

            val dialog = createWaitingForOpponentDialog(chessView.context, gameClient.gameID)
            dialog.show()

            Thread {
                Handler(mainLooper).post{
                    chessView.opponentTextView.text = gameClient.getOpponent()
                    chessView.opponentTextView.visibility = View.VISIBLE
                    chessView.heroTextView.visibility = View.VISIBLE
                    dialog.cancel()
                }

                chessView.startGame(gameClient.gameID)
            }.start()
        }
    }

    // Flow starts here
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()

        val games = createGames()

        GameClient.initialize("http://192.168.1.33:5000")
        SharedPrefsManager.initialize(baseContext)

        if ( !isRegistered() ) {
            openRegisterActivity()
        }

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
        createGameButton = findViewById(R.id.createGameButton)
        joinGameButton = findViewById(R.id.joinGameButton)
        findGameButton = findViewById(R.id.findGameButton)
        gameIDEditText = findViewById(R.id.gameIDEditText)
    }

    private fun playButtonOnClick(mode: String, gameID: String = "") {
        disableScrolling()

        val chessView = viewPager.currentChessview()
        chessView.setMode(mode)

        startGame(chessView, gameID)

        chessView.invalidate()
    }

    private fun startGame(chessView: ChessView, gameID: String) {
        gameListener.onGameSelected(chessView, gameID)
    }

    private fun initUI(games: List<Game>) {
        localPlayButton.setOnClickListener() {
            playButtonOnClick(MODE_LOCAL)
        }
        createGameButton.setOnClickListener {
            val gameID = GameClient.getInstance().create()
            playButtonOnClick(MODE_ONLINE, gameID)
        }
        joinGameButton.setOnClickListener {
            val gameID = gameIDEditText.text.toString()
            playButtonOnClick(MODE_ONLINE, gameID)
        }
        findGameButton.setOnClickListener {
            val chessView = viewPager.currentChessview()
            val gameID = GameClient.getInstance().findGame(chessView.game.name)
            playButtonOnClick(MODE_ONLINE, gameID)
        }

        viewPager.adapter = PageviewAdapter(games, gameListener)

        initDebugButton()
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
        createGameButton.visibility = View.INVISIBLE
        joinGameButton.visibility = View.INVISIBLE
        findGameButton.visibility = View.INVISIBLE
        gameIDEditText.visibility = View.INVISIBLE
    }
    private fun enableScrolling() {
        viewPager.isUserInputEnabled = true
        localPlayButton.visibility = View.VISIBLE
        createGameButton.visibility = View.VISIBLE
        joinGameButton.visibility = View.VISIBLE
        findGameButton.visibility = View.VISIBLE
        gameIDEditText.visibility = View.VISIBLE
    }

    private fun isRegistered(): Boolean {
        return SharedPrefsManager.getInstance().getUsername() != SharedPrefsManager.USERNAME_DEFAULT_VALUE
    }
    private fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun createWaitingForOpponentDialog(context: Context, gameID: String): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle("Game Created")
        dialogBuilder.setCancelable(false)
        dialogBuilder.setMessage(
            "Game ID: $gameID\n" +
            "Waiting for opponent"
        )

        return dialogBuilder.create()
    }

    private fun initDebugButton() {
        val printButton = findViewById<Button>(R.id.debugButton)
        printButton.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage(viewPager.currentChessview().game.board.toString())
            dialogBuilder.create().show()
        }
    }
}