package com.example.user.explapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import java.io.File
import java.lang.Exception
import java.net.Socket
import java.nio.charset.Charset
import java.util.*


class MyService : Service() {

    private lateinit var mRunnable : Runnable
    private var mIP : String? = null

    private val mHandler : Handler = Handler()
    private var fileLists : MutableMap<String?, MutableList<String>> = mutableMapOf()
    private val dirList : MutableList<String?> = mutableListOf()

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        mRunnable = Runnable { checkDir() }

        if ( intent?.getStringExtra("dir") != null ) {
            val fileList: MutableList<String> = mutableListOf()
            File(intent?.getStringExtra("dir")).walk().forEach { fileList.add(it.toString()) }
            fileLists[intent?.getStringExtra("dir")] = fileList

            dirList.add(intent?.getStringExtra("dir"))
        }

        mIP = intent?.getStringExtra("IP")

        mHandler.postDelayed(mRunnable, 5000)

        return START_STICKY
    }

    fun checkDir() : Unit {
        for (root in dirList) {

            var fileListNew: MutableList<String> = mutableListOf()

            File(root).walk().forEach { fileListNew.add(it.toString()) }

            if (fileLists[root] == fileListNew) {
                //mHandler.postDelayed(mRunnable, 5000)
                continue
            }
            else {
                val set1: MutableSet<String> = mutableSetOf()
                val set2: MutableSet<String> = mutableSetOf()

                for (x in fileLists[root].orEmpty())
                    set1.add(x)
                for (x in fileListNew)
                    set2.add(x)

                val newFiles = set2.minus(set1)

                for (x in newFiles) {
                    try{
                        val task: sendFileClass = sendFileClass(File(x), mIP, applicationContext)
                        task.execute()
                    } catch (e: Exception) {
                        val s = e.message
                        print(s)
                    }
                }

                fileLists[root] = fileListNew
                //mHandler.postDelayed(mRunnable, 5000)
            }
        }
        mHandler.postDelayed(mRunnable, 5000)
    }
    private class sendFileClass(file : File, IP : String?, context : Context) : AsyncTask<Unit, Void, Unit>()
    {
        private val mFile = file
        private val mIP = IP
        private val mContext = context
        private var state : String? = null

        override fun doInBackground(vararg params: Unit?) : Unit {
            try {
                val client : Socket
                if (mIP == null)
                    client = Socket("192.168.0.105", 8080)
                else
                    client = Socket(mIP, 8080)

                val outStream = client.getOutputStream()

                val data = mFile.readBytes()
                val len = mFile.length()

                var info: String = "{'name' : '${mFile.name}', 'date' : '', 'tags': '', 'len': '$len'}@"

                for (i in info.length..1024)
                    info += "0"

                outStream.write(info.toByteArray(Charset.defaultCharset()))

                outStream.write(data)

                client.close()

                state = "File ${mFile.name} sended!"
            }
            catch(ex : Exception) { print(ex.message); state = ex.message }
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            val toast = Toast.makeText(mContext, state, Toast.LENGTH_SHORT).show()
        }
    }
}

