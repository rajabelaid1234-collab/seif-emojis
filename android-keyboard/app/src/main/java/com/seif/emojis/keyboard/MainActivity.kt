package com.seif.emojis.keyboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Switch
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 30, 24, 30)
            setBackgroundColor(Color.rgb(13, 16, 32))
        }
        root.addView(TextView(this).apply {
            text = "✨ SEIF EMOJIS ✨"; textSize = 29f
            setTextColor(Color.rgb(72, 226, 194)); gravity = Gravity.CENTER
        })
        root.addView(TextView(this).apply {
            text = "وجوه ورموز بسرعة في التطبيقات.\nفعّل لوحة المفاتيح للكتابة مباشرة.\nالزر العائم اختياري، قابل للتحريك والإخفاء."
            textSize = 17f; setTextColor(Color.WHITE); gravity = Gravity.CENTER
            setPadding(0, 20, 0, 22)
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
        val soundSwitch = Switch(this).apply {
            text = "🔊 صوت السيف (اختياري)"
            textSize = 16f
            setTextColor(Color.WHITE)
            isChecked = getSharedPreferences("seif_settings", Context.MODE_PRIVATE).getBoolean("sword_sound", false)
            setOnCheckedChangeListener { _, enabled ->
                getSharedPreferences("seif_settings", Context.MODE_PRIVATE).edit().putBoolean("sword_sound", enabled).apply()
            }
            setPadding(0, 18, 0, 18)
        }
        root.addView(soundSwitch)
        root.addView(Button(this).apply {
            text = "3) السماح بالزر العائم"
            setOnClickListener {
                if (Settings.canDrawOverlays(this@MainActivity)) {
                    startService(Intent(this@MainActivity, FloatingBubbleService::class.java))
                } else {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                }
            }
        })
        root.addView(TextView(this).apply {
            text = "اسمح بالظهور فوق التطبيقات من إعدادات Android.\nالزر ينسخ الوجوه؛ لوحة المفاتيح تُدرجها مباشرة في خانة الكتابة."
            textSize = 14f; setTextColor(Color.LTGRAY); gravity = Gravity.CENTER
            setPadding(0, 18, 0, 0)
        })
        setContentView(root)
    }
    override fun onResume() {
        super.onResume()
        if (Settings.canDrawOverlays(this)) startService(Intent(this, FloatingBubbleService::class.java))
    }
}