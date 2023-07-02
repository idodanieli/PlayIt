package com.idodanieli.playit

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.idodanieli.playit.activities.RegisterActivity
import com.idodanieli.playit.activities.openRegisterActivity
import com.idodanieli.playit.clients.GameClient
import com.idodanieli.playit.games.chess.MODE_LOCAL
import com.idodanieli.playit.games.chess.MODE_ONLINE
import com.idodanieli.playit.games.chess.game_subscriber.*
import com.idodanieli.playit.games.chess.logic.*
import com.idodanieli.playit.games.chess.ui.DialogBuilder
import com.idodanieli.playit.games.chess.ui.Speaker
import com.idodanieli.playit.games.chess.variants.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Field


class MainActivity : AppCompatActivity(), GameSubscriber {
    private val dialogBuilder = DialogBuilder(this)

    private lateinit var viewPager: ViewPager2
    private lateinit var localPlayButton: Button
    private lateinit var createGameButton: Button
    private lateinit var joinGameButton: Button
    private lateinit var findGameButton: Button
    private lateinit var gameIDEditText: EditText

    companion object {
        lateinit var games: List<Game>
    }

    // Flow starts here
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()

        games = createGames()

        GameClient.initialize("https://idodanieli.pythonanywhere.com")
        User.initialize(baseContext)

        if ( !User.getInstance().isRegistered() ) {
            openRegisterActivity()
        }

        initUI(games)
    }

    // Override systems back button
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewPager.gameIsPlaying()) {
                dialogBuilder.showAreYouSureYouWantToQuitDialog {
                    // TODO: Exit game
                    enableScrolling()
                }

                return true
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onGameEvent(event: GameEvent) {
        when(event) {
            is GameOverEvent -> {
                dialogBuilder.showGameOverDialog(event.winner) {
                    viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                    enableScrolling()
                }
            }

            is PlayersJoinedEvent -> {
                this.setPlayers(event.hero, event.opponent)
            }
        }
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

    // --- UI --------------------------------------------------------------------------------------
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
        chessView.subscribe(this)
        chessView.subscribe(Speaker(this))

        chessView.select(mode, gameID)
        chessView.invalidate()
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

        val screenWidth = resources.displayMetrics.widthPixels

        viewPager.adapter = PageviewAdapter(games, screenWidth, this)
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

    // --- Set players names when they join a game ----------------------------------------------------
    private fun setPlayers(hero: String, opponent: String) {
        setPlayer(viewPager.currentPage().findViewById(R.id.playerHero), hero)
        setPlayer(viewPager.currentPage().findViewById(R.id.playerOpponent), opponent)

        val heroColor = viewPager.currentChessview().hero
        viewPager.currentHeroCapturedPiecesView().player = heroColor
        viewPager.currentOpponentCapturedPiecesView().player = heroColor.opposite()
    }

    private fun setPlayer(textView: TextView, player: String) {
        textView.text = player
        textView.visibility = View.VISIBLE
    }
}