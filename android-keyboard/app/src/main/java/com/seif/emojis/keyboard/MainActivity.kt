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
    private val bg = Color.rgb(11, 16, 32)
    private val accent = Color.rgb(94, 234, 212)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(24), dp(18), dp(22))
            setBackgroundColor(bg)
        }
        content.addView(TextView(this).apply { text = "✨ SEIF EMOJIS"; textSize = 28f; typeface = Typeface.DEFAULT_BOLD; setTextColor(accent); gravity = Gravity.CENTER })
        content.addView(TextView(this).apply { text = "اختَر ما يناسبك — تصميم أسهل للهاتف"; textSize = 15f; setTextColor(Color.WHITE); gravity = Gravity.CENTER; setPadding(0, dp(8), 0, dp(18)) })
        content.addView(infoCard("📴 بدون إنترنت", "إيموجيات ووجوه وزخرفة نصوص واسم مخفي ومفضلة. تحليل تعبير الوجه يعمل بالنموذج المضمّن."), match())
        content.addView(actionButton("📴 فتح الأدوات دون إنترنت", accent) { startActivity(Intent(this, WebActivity::class.java).putExtra(WebActivity.EXTRA_MODE, WebActivity.MODE_OFFLINE)) })
        content.addView(infoCard("🌐 مع الإنترنت", "افتح الموقع الكامل كما يظهر أونلاين، بما في ذلك أدواته وأزرار التنزيل والمحتوى المتصل."), match())
        content.addView(actionButton("🌐 فتح الموقع الكامل أونلاين", Color.rgb(38, 51, 78)) { startActivity(Intent(this, WebActivity::class.java).putExtra(WebActivity.EXTRA_MODE, WebActivity.MODE_ONLINE)) })
        content.addView(TextView(this).apply { text = "⌨️ لوحة المفاتيح"; textSize = 19f; typeface = Typeface.DEFAULT_BOLD; setTextColor(accent); setPadding(0, dp(18), 0, dp(4)) })
        content.addView(actionButton("١. تفعيل لوحة SEIF", Color.rgb(38, 51, 78)) { startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) })
        content.addView(actionButton("٢. اختيار لوحة SEIF للكتابة", Color.rgb(38, 51, 78)) { (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker() })
        content.addView(TextView(this).apply { text = "اختر «بدون إنترنت» عند عدم توفر البيانات، أو «مع الإنترنت» لفتح نسخة الموقع الكاملة. لا تحتاج إلى تسجيل دخول."; textSize = 13f; setTextColor(Color.LTGRAY); setPadding(0, dp(16), 0, 0) })
        setContentView(ScrollView(this).apply { isFillViewport = true; addView(content); setBackgroundColor(bg) })
    }
    private fun infoCard(title: String, description: String) = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL; setPadding(dp(14), dp(12), dp(14), dp(12)); setBackgroundColor(Color.rgb(21, 29, 50))
        addView(TextView(this@MainActivity).apply { text = title; textSize = 17f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.WHITE) })
        addView(TextView(this@MainActivity).apply { text = description; textSize = 13f; setTextColor(Color.rgb(190, 204, 224)); setPadding(0, dp(5), 0, 0) })
    }
    private fun match() = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { bottomMargin = dp(10) }
    private fun actionButton(label: String, color: Int, action: () -> Unit): Button = Button(this).apply {
        text = label; textSize = 15f; isAllCaps = false; setTextColor(if (color == accent) Color.rgb(8, 24, 29) else Color.WHITE); setBackgroundColor(color); minHeight = dp(52); setPadding(dp(10), dp(8), dp(10), dp(8)); setOnClickListener { action() }
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { bottomMargin = dp(9) }
    }
    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
