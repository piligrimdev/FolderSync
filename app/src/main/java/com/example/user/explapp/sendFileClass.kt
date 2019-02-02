package com.example.user.explapp

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import java.io.File
import java.lang.Exception
import java.net.Socket
import java.nio.charset.Charset

class sendFileClass(file : File, IP : String?, context : Context) : AsyncTask<Unit, Void, Unit>()
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