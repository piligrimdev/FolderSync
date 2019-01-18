package com.example.user.explapp

import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.IBinder
import java.io.File
import java.lang.Exception
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class MyService : Service() {

    private lateinit var mRunnable : Runnable

    private var mHandler : Handler = Handler()
    private var fileList : MutableList<String> = mutableListOf()

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        mRunnable = Runnable { checkDir(intent?.getStringExtra("dir")) }

        File(intent?.getStringExtra("dir")).walk().forEach { fileList.add(it.toString()) }

        mHandler.postDelayed(mRunnable, 5000)

        return START_STICKY
    }

    fun checkDir(root : String?) : Unit {

        var fileListNew: MutableList<String> = mutableListOf()

        File(root).walk().forEach { fileListNew.add(it.toString()) }

        if (fileList == fileListNew)
            mHandler.postDelayed(mRunnable, 5000)
        else {
            val set1: MutableSet<String> = mutableSetOf()
            val set2: MutableSet<String> = mutableSetOf()

            for (x in fileList)
                set1.add(x)
            for (x in fileListNew)
                set2.add(x)

            val newFiles = set2.minus(set1)

            for (x in newFiles) {
                try {
                    val task: sendFileClass = sendFileClass(File(x))
                    task.execute()
                }
                catch(e : Exception)
                {
                    val s = e.message
                    print(s)
                }
            }

            fileList = fileListNew
            mHandler.postDelayed(mRunnable,5000)
        }
    }

    private class sendFileClass(file : File) : AsyncTask<Unit, Void, Unit>()
    {
        private val mFile = file

        override fun doInBackground(vararg params: Unit?) : Unit {
            try {
                val client = Socket("192.168.0.105", 8080)
                val outStream = client.getOutputStream()

                val data = mFile.readBytes()
                val len = mFile.length()

                var info: String = "{'name' : '${mFile.name}', 'date' : '', 'tags': '', 'len': '$len'}@"

                for (i in info.length..1024)
                    info += "0"

                outStream.write(info.toByteArray(Charset.defaultCharset()))

                outStream.write(data)

                client.close()
            }
            catch(ex : Exception)
            {
                val s = ex.message
                print(s)
            }
        }
    }
}

