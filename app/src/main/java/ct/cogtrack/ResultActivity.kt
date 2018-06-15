package ct.cogtrack

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // val score = this.intent.getSerializableExtra("score") as Result
        val score = ResultHolder.result

        var msg = ""
        for (s in score)
        {
            // msg += "%s = %f\n".format(s.first, s.second)
            msg += "${s.first} = ${s.second}\n".format(s.first, s.second)
        }
        this.resultView.text = msg

        // setSupportActionBar(toolbar)
    }
}
