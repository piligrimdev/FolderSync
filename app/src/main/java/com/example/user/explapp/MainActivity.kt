package com.example.user.explapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.EditText

public class MainActivity : AppCompatActivity() {

    public val EXTRA_MESSAGE : String = "com.example.explapp.MESSAGE"

    private val rootDir : String = "/storage/emulated/0/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    fun sendAnsewer(view : View)
    {
        val editText = findViewById(R.id.edit_maesage) as EditText
        val msg : String = editText.text.toString()
        if(msg != "")
            startService(Intent(this, MyService::class.java).putExtra("dir", rootDir + msg))
    }
}
