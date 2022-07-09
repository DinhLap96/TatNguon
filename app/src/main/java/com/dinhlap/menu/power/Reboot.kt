package com.dinhlap.menu.power

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import java.lang.Exception

class Reboot : AppCompatActivity() {

    val key = "key"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        setContentView(R.layout.activity_reboot)
        getReboot(intent.getStringExtra(key).toString())

    }

    private fun getReboot (reboot :String){
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                val otherStrings = arrayOf( "su", "-c",reboot)
                var proc = Runtime.getRuntime().exec(otherStrings)
                proc.waitFor();
                finish();
            } catch (ex : Exception) {
                ex.printStackTrace();
            }
        }, 1000)
    }

    private fun fullScreen(){
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = ( View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

}