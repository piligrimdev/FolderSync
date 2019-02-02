package com.example.user.explapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.io.File
import android.os.Handler

class FullDirSend_Service : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val fileList : MutableList<String?> = mutableListOf()

        File(intent?.getStringExtra("dir")).walk().forEach { fileList.add(it.toString()) }
        val IP : String? = intent?.getStringExtra("IP")

        val Runnable = Runnable { sendDir(fileList, IP) }

        val Handler : Handler = Handler()

        Handler.postDelayed(Runnable, 5000)

        return START_STICKY
    }

    fun sendDir(fileList : MutableList<String?>, IP : String?)
    {
        for (x in fileList)
        {
            try{
                val task : sendFileClass = sendFileClass(File(x), IP, applicationContext)
                task.execute()
            }
            catch(e : Exception) {
                val s = e.message
                print(s)
            }
        }
    }
}
