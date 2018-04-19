package ct.cogtrack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_nback.*

class NBackActivity : AppCompatActivity() {

    private var nback = NBack({ char: Char -> letterView.text = char.toString() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nback)

        // setSupportActionBar(toolbar)
        quitButton.setOnClickListener { view -> this.endGame(view) }

        // this.nback = NBack({ char: Char -> letterView.text = char.toString() })
        matchButton.setOnClickListener { this.nback.match() }
    }

    fun endGame(view: View) {
        // startTwoBackButton.text = "foo"
        val intent = Intent(this, ResultActivity::class.java)
        this.startActivity(intent)
    }




    fun start() {

    }
}
