package nl.guldem.samplestorkify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import nl.guldem.storkify.Storkify

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun onResume() {
        settings_button.setOnClickListener { Storkify.openStorkiFySettings(this.applicationContext) }
        super.onResume()
    }
}

