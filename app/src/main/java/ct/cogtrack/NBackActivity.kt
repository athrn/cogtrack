package ct.cogtrack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_nback.*
import java.util.*

class NBackActivity : AppCompatActivity() {

    private var rnd = Random()
    private var nback = NBack(maxRounds = 5, charGenerator = CharSequence("XAXBC")::next)
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nback)

        // setSupportActionBar(toolbar)
        quitButton.setOnClickListener { view -> this.endGame() }

        // this.nback = NBack({ char: Char -> letterView.text = char.toString() })
        matchButton.setOnClickListener { this.guessMatch() }

        nextChar()
    }

    fun updateStats() {
        charCounter.text = this.nback.rounds.toString()
        noResponseCounter.text = this.nback.wrongNoResponse.toString()
        rightCounter.text = this.nback.right.toString()
        wrongCounter.text = this.nback.wrongGuess.toString()
    }


    fun guessMatch() {
        this.nback.guessIsMatch()
        this.updateStats()
    }

    fun showChar(char: Char) {
        letterView.text = char.toString()
    }

    fun nextChar() {

        if(this.nback.isFinished()) {
            this.endGame()
            return
        }



        this.showChar(this.nback.nextChar())
        this.updateStats()

        val visibleTime = 500L
        val nextCharDelay = 1000L

        assert(nextCharDelay > visibleTime)

        // val handler = Handler()
        handler.postDelayed({ this.showChar(' ') }, visibleTime)
        handler.postDelayed(this::nextChar, nextCharDelay)
    }


    fun endGame() {
        this.nback.stop()
        handler.removeCallbacks(this::nextChar)

        val intent = Intent(this, ResultActivity::class.java)
        // intent.putExtra("score", this.nback.score())
        ResultHolder.result = this.nback.score()
        this.startActivity(intent)
    }




    fun start() {

    }
}
