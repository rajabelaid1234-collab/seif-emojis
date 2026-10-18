package com.seif.emojis.keyboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 36, 28, 36)
            setBackgroundColor(Color.rgb(13, 16, 32))
        }
        root.addView(TextView(this).apply {
            text = "✨ SEIF EMOJIS Keyboard ✨"
            textSize = 25f
            setTextColor(Color.rgb(72, 226, 194))
            gravity = Gravity.CENTER
        })
        root.addView(TextView(this).apply {
            text = "فعّل لوحة المفاتيح من إعدادات Android، ثم اخترها أثناء الكتابة.\n\nهذه النسخة لا تطلب اتصالًا بالإنترنت."
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 24)
        })
        root.addView(Button(this).apply {
            text = "1) تفعيل لوحة المفاتيح"
            setOnClickListener { startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) }
        })
        root.addView(Button(this).apply {
            text = "2) اختيار لوحة SEIF"
            setOnClickListener {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showInputMethodPicker()
            }
        })
        setContentView(root)
    }
}
