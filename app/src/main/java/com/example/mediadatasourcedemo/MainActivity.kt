package com.example.mediadatasourcedemo

import android.content.Context
import android.media.AudioManager
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    var mediaPlayer: MediaPlayer? = null
    var myMediaDataSource: MyMediaDataSource? = null

    class MyMediaDataSource(context: Context) : MediaDataSource() {
        val TAG = "MyMediaDataSource"
        var audiobuffer: ByteArray? = null

        init {
            val iS: InputStream = context.resources.openRawResource(R.raw.flowers)
            val length: Int = iS.available()
            audiobuffer = ByteArray(length)
            iS.read(audiobuffer)
        }

        override fun close() {
            Log.w(TAG, "close")
            TODO("Not yet implemented")
        }

        override fun readAt(position: Long, buffer: ByteArray?, offset: Int, size: Int): Int {
            Log.w(TAG, "readAt")
            val length: Int = audiobuffer!!.size
            if (position >= length) {
                return -1 // -1 indicates EOF
            }
            var newSize = size
            if (position + size > length) {
                newSize = newSize - ((position.toInt() + size) - length)
            }
            System.arraycopy(audiobuffer, position.toInt(), buffer, offset, newSize)
            return newSize
        }

        override fun getSize(): Long {
            Log.w(TAG, "getSize")
            return audiobuffer!!.size.toLong()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myMediaDataSource = MyMediaDataSource(this)
        mediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(myMediaDataSource)
            prepare()
            start()
        }
//        mediaPlayer = MediaPlayer.create(this, R.raw.flowers)
//        mediaPlayer!!.start()

    }
}