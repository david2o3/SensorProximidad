package com.example.sensorproximidad

import android.content.ContentValues.TAG
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        val proximitySensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if(proximitySensor ==null) {
            Log.e(TAG,"No tiene sensor de proximidad")
            finish()
        }

        // Create listener
        val proximitySensorListener: SensorEventListener = object : SensorEventListener {
            override
            fun onSensorChanged(sensorEvent: SensorEvent) {
                // Aqui va el codigo de lo que queremos hacer con el sensor cuand ocambie
                if(sensorEvent.values[0] < proximitySensor.maximumRange) {
                    // Detected something nearby
                    window.decorView.setBackgroundColor(Color.RED)
                    playSound()
                } else {
                    // Nothing is nearby
                    window.decorView.setBackgroundColor(Color.GREEN)
                    stopSound()
                }
            }

            override
            fun onAccuracyChanged(sensor: Sensor, i: Int) {

            }
        }

        // Register it, specifying the polling interval in
        // microseconds
        sensorManager.registerListener(
            proximitySensorListener,
            proximitySensor, 2 * 1000 * 1000
        )

    }
    //Reproducir música
    //1. - Declaramos la variable

    var mMediaPlayer: MediaPlayer? = null

    //2. - Creamos la funcion play
    fun playSound(){
        if (mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(this,R.raw.meme)
            mMediaPlayer!!.isLooping =true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    //pausar
    fun pauseSound(){
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.pause()
        }
    }

    //detener
    fun stopSound(){
        if (mMediaPlayer != null){
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    //destruir el sonido para cuando salimos de la aplicacion
    override fun onStop() {
        super.onStop()
        if (mMediaPlayer != null){
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

}