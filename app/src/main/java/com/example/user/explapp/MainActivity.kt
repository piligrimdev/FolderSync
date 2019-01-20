package com.example.user.explapp

import android.app.Activity
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import android.content.Intent

import android.net.Uri

import android.provider.DocumentsContract
import android.support.v4.provider.DocumentFile
import android.util.Log

import android.graphics.Color

import android.widget.EditText
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams

import java.net.URI

public class MainActivity : AppCompatActivity() {

    public val EXTRA_MESSAGE : String = "com.example.explapp.MESSAGE"

    private val rootDir : String = "/storage/emulated/0/"

    private lateinit var dirsScrollLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        dirsScrollLayout = findViewById(R.id.dirsLayout) as LinearLayout
    }

    fun sendAnsewer(view : View)
    {
        val editText = findViewById(R.id.edit_maesage) as EditText
        val msg : String = editText.text.toString()
        if(msg != "")
            startService(Intent(this, MyService::class.java).putExtra("dir", rootDir + msg))
    }

    fun openChooseDir(view : View)
    {
        val intent : Intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, 42)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 42 && resultCode == Activity.RESULT_OK)
        {
            val uri : Uri = data!!.data
            val path : String = uri.path.split(":")[1] + "/"

            val newDirView : TextView = TextView(this)

            val layoutParams : LayoutParams = LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, 1.0f)
            layoutParams.setMargins(10, 10, 10, 10)

            newDirView.layoutParams = layoutParams
            newDirView.text = path
            newDirView.textSize = 18.0f
            newDirView.setBackgroundColor(Color.parseColor("#bdbdbd"))


            dirsScrollLayout.addView(newDirView)
            dirsScrollLayout.invalidate()

            startService(Intent(this, MyService::class.java).putExtra("dir", rootDir + path))
        }
    }
}
