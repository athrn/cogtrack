package ct.cogtrack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_nback.*
import java.util.*

class NBackActivity : AppCompatActivity()
{

    private var rnd = Random()
    private var nback = NBack(maxRounds = 5, charGenerator = CharSequence("XAXBC")::next)
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nback)

        // setSupportActionBar(toolbar)
        quitButton.setOnClickListener { this.endGame() }

        // this.nback = NBack({ char: Char -> letterView.text = char.toString() })
        matchButton.setOnClickListener { this.guessMatch() }

        nextChar()
    }

    fun updateStats()
    {
        // TODO: Count charCount instead of chars. Show 1 right after first char has been shown.
        // TODO: Red for both wrong counters and Green for both right counters (or use just one red, one green)
        roundCounter.text = "${this.nback.round}/${this.nback.maxRounds}" // this.nback.round.toString()
        correctCounter.text = (this.nback.correctMatch + this.nback.correctNoResponse).toString()
        wrongCounter.text = (this.nback.wrongMatch + this.nback.wrongNoResponse).toString()
    }


    fun guessMatch()
    {
        this.nback.guessIsMatch()
        this.updateStats()
    }

    fun showChar(char: Char)
    {
        letterView.text = char.toString()
    }

    fun nextChar()
    {
        val c = this.nback.nextChar()
        // TODO: NOTE: This is the correct way to stop the game. nextChar must be called for the last char to be scored. A bit ugly.
        // Calling nback.stop() is used to end prematurely. Score isn't counted the same way.
        if (this.nback.isStopped)
        {
            this.endGame()
            return
        }

        this.showChar(c)
        this.updateStats()

        val visibleTime = 500L
        val nextCharDelay = 1000L

        assert(nextCharDelay > visibleTime)

        // val handler = Handler()
        handler.postDelayed({ this.showChar(' ') }, visibleTime)
        handler.postDelayed(this::nextChar, nextCharDelay)
    }


    fun endGame()
    {
        this.nback.stop()
        this.updateStats()
        handler.removeCallbacks(this::nextChar)

        val intent = Intent(this, ResultActivity::class.java)
        // intent.putExtra("score", this.nback.score())
        ResultHolder.result = this.nback.score()
        this.startActivity(intent)
    }

}
