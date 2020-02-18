package nl.guldem.storkify

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK

class Storkify() {

    companion object {
        fun openStorkiFySettings(context: Context) {
            val intent = Intent().apply {
                setClass(context, Class.forName("nl.guldem.storkify.ui.StorkifyActivity"))
                flags = FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
}