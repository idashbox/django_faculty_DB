package com.example.faculty_app.ui.activities.splash_screen

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.faculty_app.R
import com.example.faculty_app.ui.activities.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashText1 = findViewById<TextView>(R.id.splash_text1)
        val splashText2 = findViewById<TextView>(R.id.splash_text2)

        val shader1 = LinearGradient(
            0f, 0f, 0f, splashText1.textSize,
            intArrayOf(
                getColor(R.color.start_gradient_color),
                getColor(R.color.end_gradient_color)
            ),
            null,
            Shader.TileMode.CLAMP
        )

        val shader2 = LinearGradient(
            0f, 0f, 0f, splashText1.textSize,
            intArrayOf(
                getColor(R.color.start_gradient_color),
                getColor(R.color.end_gradient_color)
            ),
            null,
            Shader.TileMode.CLAMP
        )
        splashText1.paint.shader = shader1
        splashText2.paint.shader = shader2

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}
