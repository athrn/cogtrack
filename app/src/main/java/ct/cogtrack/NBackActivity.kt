package ct.cogtrack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_nback.*
import java.util.*

class NBackActivity : AppCompatActivity() {

    private var nback = NBack(n_rounds = 5)
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nback)

        // setSupportActionBar(toolbar)
        quitButton.setOnClickListener { view -> this.endGame() }

        // this.nback = NBack({ char: Char -> letterView.text = char.toString() })
        matchButton.setOnClickListener { this.nback.match() }

        nextChar()
    }

    fun showChar(char: Char) {
        letterView.text = char.toString()
    }

    fun nextChar() {

        if(this.nback.finished()) {
            if(! this.nback.isStopped)
                this.endGame()

            return
        }

        this.showChar(this.nback.nextChar())

        val visible_time = 500L
        val hidden_time = 500L

        // val handler = Handler()
        handler.postDelayed({ this.showChar(' ') }, visible_time)
        handler.postDelayed(this::nextChar, visible_time + hidden_time)
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
