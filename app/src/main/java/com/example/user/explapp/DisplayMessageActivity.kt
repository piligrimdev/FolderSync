package com.example.user.explapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DisplayMessageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_display_message)

        val intent : Intent = getIntent()
        val msg : String = intent.getStringExtra("EXTRA_MESSAGE")

        var txtView = TextView(this)
        txtView.setTextSize(40.toFloat())
        txtView.setText(msg)

        setContentView(txtView)
    }
}
