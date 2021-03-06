package ct.cogtrack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.view.menu.MenuBuilder
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: Set res/menu in menubar. Use items instead of buttons?
        setSupportActionBar(toolbar)
        // toolbar.setMenu( )
        //setSupportActionBar(main)
        startTwoBackButton.setOnClickListener { startTwoBack() }
    }

    fun startTwoBack()
    {
        val intent = Intent(this, NBackActivity::class.java)
        // TODO: Pass gameId to the intent
        intent.putExtra("gameId", 0)
        this.startActivity(intent)

    }
}
