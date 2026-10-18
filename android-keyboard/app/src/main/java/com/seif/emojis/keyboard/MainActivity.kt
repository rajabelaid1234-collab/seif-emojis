package com.seif.emojis.keyboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private val bg = Color.rgb(13, 16, 32)
    private val accent = Color.rgb(72, 226, 194)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(24), dp(20), dp(28))
            setBackgroundColor(bg)
        }
        content.addView(TextView(this).apply {
            text = "✨ SEIF EMOJIS"
            textSize = 28f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(accent)
            gravity = Gravity.CENTER
        })
        content.addView(TextView(this).apply {
            text = "إيموجيات ورموز وأدوات ممتعة — في مكان واحد"
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, dp(8), 0, dp(18))
        })
        content.addView(TextView(this).apply {
            text = "📴 الأدوات الأساسية محفوظة داخل التطبيق وتعمل دون إنترنت. تحليل تعبير الوجه مضمّن كذلك."
            textSize = 14f
            setTextColor(Color.rgb(218, 232, 239))
            setPadding(dp(14), dp(14), dp(14), dp(14))
            setBackgroundColor(Color.rgb(25, 36, 55))
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { bottomMargin = dp(18) })
        content.addView(actionButton("🚀 فتح أدوات SEIF EMOJIS", accent) {
            startActivity(Intent(this, WebActivity::class.java))
        })
        content.addView(actionButton("⌨️ تفعيل لوحة مفاتيح SEIF", Color.rgb(40, 50, 73)) {
            startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        })
        content.addView(actionButton("🔤 اختيار لوحة SEIF للكتابة", Color.rgb(40, 50, 73)) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
        })
        content.addView(TextView(this).apply {
            text = "طريقة استخدام لوحة المفاتيح\n١. فعّل لوحة SEIF من إعدادات الهاتف.\n٢. افتح واتساب أو أي تطبيق واكتب في خانة الرسالة.\n٣. اختر SEIF من زر تبديل لوحة المفاتيح.\n\nافتح «أدوات SEIF EMOJIS» لاستخدام مولّدات النصوص والرموز والكاميرا."
            textSize = 14f
            setTextColor(Color.LTGRAY)
            setPadding(dp(2), dp(18), dp(2), dp(4))
            gravity = Gravity.START
        })
        setContentView(ScrollView(this).apply {
            isFillViewport = true
            addView(content)
            setBackgroundColor(bg)
        })
    }

    private fun actionButton(label: String, color: Int, action: () -> Unit): Button =
        Button(this).apply {
            text = label
            textSize = 16f
            isAllCaps = false
            setTextColor(if (color == accent) Color.rgb(8, 24, 29) else Color.WHITE)
            setBackgroundColor(color)
            minHeight = dp(54)
            setPadding(dp(12), dp(10), dp(12), dp(10))
            setOnClickListener { action() }
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                bottomMargin = dp(10)
            }
        }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
