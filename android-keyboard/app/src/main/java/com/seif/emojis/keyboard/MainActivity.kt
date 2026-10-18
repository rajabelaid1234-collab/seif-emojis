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
            text = "استخدم الإيموجيات داخل واتساب وبقية التطبيقات.\nلوحة SEIF تكتب الرمز مباشرة في خانة الرسالة.\nوالزر العائم يفتح قائمة نسخ سريعة فوق التطبيقات."
            textSize = 17f; setTextColor(Color.WHITE); gravity = Gravity.CENTER
            setPadding(0, 20, 0, 22)
        })
        root.addView(Button(this).apply {
            text = "🌐 فتح الموقع الكامل SEIF EMOJIS"
            textSize = 17f
            setOnClickListener { startActivity(Intent(this@MainActivity, WebActivity::class.java)) }
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
            text = "3) تشغيل / إعادة تشغيل الزر العائم"
            setOnClickListener {
                if (Settings.canDrawOverlays(this@MainActivity)) {
                    startService(Intent(this@MainActivity, FloatingBubbleService::class.java))
                } else {
                    startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                }
            }
        })
        root.addView(Button(this).apply {
            text = "💬 فتح واتساب"
            setOnClickListener {
                val launch = packageManager.getLaunchIntentForPackage("com.whatsapp")
                if (launch != null) startActivity(launch)
                else android.widget.Toast.makeText(this@MainActivity, "واتساب غير مثبت على هذا الجهاز", android.widget.Toast.LENGTH_LONG).show()
            }
        })
        root.addView(TextView(this).apply {
            text = "لاستخدامها في واتساب: افتح محادثة، اضغط خانة الرسالة، ثم اختر لوحة المفاتيح SEIF من زر تغيير لوحة المفاتيح في أسفل الشاشة. اضغط الرمز ليُدرج مباشرة في الرسالة.\\nإذا لم يظهر الزر العائم بعد منح الإذن، اضغط زر تشغيله هنا وأبقِ إشعار SEIF ظاهرًا."
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