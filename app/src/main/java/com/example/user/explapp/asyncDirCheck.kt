package com.example.user.explapp

import android.os.AsyncTask
import java.io.File
import java.net.Socket
import java.util.*
import java.nio.charset.Charset


/*
fun startCli(file : File) : Unit
{
    val client = Socket("192.168.0.105", 8080)
    val outStream = client.getOutputStream()
    val inStream = Scanner(client.getInputStream())


    val data  = file.readBytes()
    val len = data.size

    var info : String = "{'name' : '${file.name}', 'date' : '', 'tags': '', 'len': '$len'}@"

    for(i in info.length..1024)
        info += "0"

    outStream.write(info.toByteArray(Charset.defaultCharset()))

    outStream.write(data)

    client.close()
}
*/