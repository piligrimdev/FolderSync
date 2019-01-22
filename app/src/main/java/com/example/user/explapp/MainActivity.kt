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

import android.widget.Toast
import android.widget.EditText
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams

import java.net.URI

public class MainActivity : AppCompatActivity() {

    private val rootDir : String = "/storage/emulated/0/"
    private var currentIP : String? = "192.168.0.105"

    private lateinit var dirsScrollLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        dirsScrollLayout = findViewById(R.id.dirsLayout) as LinearLayout
    }

    fun changeIp(view : View)
    {
        val editText = findViewById(R.id.edit_maesage) as EditText
        if (editText.text.toString() != "") {
            currentIP = editText.text.toString()
            startService(Intent(this, MyService::class.java).putExtra("IP", currentIP))
        }
        else {
            currentIP = null
            val toast = Toast.makeText(applicationContext, "IP Changed to default",
                    Toast.LENGTH_SHORT)
            toast.show()
            startService(Intent(this, MyService::class.java))
        }

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

            val layParams : LayoutParams = LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, 1.0f)
            layParams.setMargins(10, 10, 10, 10)

            with(newDirView) {
                layoutParams = layParams
                text = path
                textSize = 18.0f
                setBackgroundColor(Color.parseColor("#bdbdbd"))
            }

            dirsScrollLayout.addView(newDirView)
            dirsScrollLayout.invalidate()

            val intent : Intent = Intent(this, MyService::class.java)
            intent.putExtra("dir", rootDir + path)
            intent.putExtra("IP", currentIP)

            startService(intent)
        }
    }
}
