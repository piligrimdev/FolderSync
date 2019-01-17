package com.example.user.explapp

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.support.v4.provider.DocumentFile
import android.widget.EditText
import java.net.URI

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

            startService(Intent(this, MyService::class.java).putExtra("dir", rootDir + path))
        }
    }
}
