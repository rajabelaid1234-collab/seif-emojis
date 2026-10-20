package com.seif.emojis.keyboard

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Build
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
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(14), dp(24), dp(14), dp(18))
            setBackgroundColor(bg)
        }
        content.addView(TextView(this).apply {
            text = "✨ SEIF EMOJIS"
            textSize = 29f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(accent)
            gravity = Gravity.CENTER
        })
        content.addView(TextView(this).apply {
            text = "اختر ما تريد — واجهة بسيطة ومناسبة للهاتف"
            textSize = 14f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(dp(4), dp(8), dp(4), dp(20))
        })
        val choices = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        choices.addView(tile("🎮\nالألعاب\n\nتحديات تعمل دون إنترنت", Color.rgb(25, 94, 80)) { openOffline("games") }, weighted())
        choices.addView(tile("😊\nالإيموجي\n\nرموز وزخرفة وكيبورد", Color.rgb(67, 52, 125)) { showEmojiOptions() }, weighted())
        content.addView(choices)
        content.addView(actionButton("🌐 الموقع أونلاين + التنزيل", Color.rgb(33, 95, 128)) {
            startActivity(Intent(this, WebActivity::class.java).putExtra(WebActivity.EXTRA_MODE, WebActivity.MODE_ONLINE))
        }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54)).apply {
            setMargins(dp(3), dp(6), dp(3), dp(10))
        })
        content.addView(TextView(this).apply {
            text = "📴 الألعاب والرموز الأساسية مضمّنة داخل التطبيق. استخدم «أونلاين» للموقع الكامل والميزات التي تحتاج الإنترنت."
            textSize = 12f
            setTextColor(Color.rgb(190, 204, 224))
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(10), dp(8), dp(10))
        })
        setContentView(ScrollView(this).apply { isFillViewport = true; addView(content); setBackgroundColor(bg) })
    }

    private fun openOffline(section: String) {
        startActivity(Intent(this, WebActivity::class.java).putExtra(WebActivity.EXTRA_MODE, WebActivity.MODE_OFFLINE).putExtra(WebActivity.EXTRA_SECTION, section))
    }
    private fun showEmojiOptions() {
        AlertDialog.Builder(this).setTitle("😊 قسم الإيموجي")
            .setItems(arrayOf("فتح الرموز والزخرفة دون إنترنت", "إعداد لوحة مفاتيح SEIF", "تشغيل الزر العائم 😊")) { _, which ->
                when (which) { 0 -> openOffline("emoji"); 1 -> showKeyboardSetup(); 2 -> startFloatingButton() }
            }.setNegativeButton("إلغاء", null).show()
    }
    private fun showKeyboardSetup() {
        AlertDialog.Builder(this).setTitle("⌨️ إعداد لوحة SEIF")
            .setMessage("فعّل SEIF EMOJIS من إعدادات لوحة المفاتيح، ثم ارجع واخترها من مبدّل لوحات المفاتيح. بعد ذلك تعمل داخل التطبيقات التي تقبل إدخال النص.")
            .setPositiveButton("تفعيل لوحة المفاتيح") { _, _ -> startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) }
            .setNeutralButton("اختيار اللوحة") { _, _ -> (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker() }
            .setNegativeButton("إلغاء", null).show()
    }
    private fun startFloatingButton() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            AlertDialog.Builder(this).setTitle("إذن الزر العائم")
                .setMessage("اسمح لـ SEIF EMOJIS بالظهور فوق التطبيقات إذا أردت الزر العائم. الإذن اختياري؛ لوحة المفاتيح تعمل بدونه.")
                .setPositiveButton("فتح إعداد الإذن") { _, _ -> startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))) }
                .setNegativeButton("ليس الآن", null).show()
        } else startService(Intent(this, FloatingBubbleService::class.java))
    }
    private fun tile(label: String, color: Int, action: () -> Unit): Button = Button(this).apply {
        text = label; textSize = 14f; isAllCaps = false; gravity = Gravity.CENTER; setTextColor(Color.WHITE)
        background = GradientDrawable().apply { setColor(color); cornerRadius = dp(20).toFloat(); setStroke(dp(1), Color.rgb(72, 86, 124)) }; minHeight = dp(138); setPadding(dp(5), dp(12), dp(5), dp(12)); setOnClickListener { action() }
    }
    private fun actionButton(label: String, color: Int, action: () -> Unit): Button = Button(this).apply {
        text = label; textSize = 15f; isAllCaps = false; setTextColor(Color.WHITE); background = GradientDrawable().apply { setColor(color); cornerRadius = dp(16).toFloat() }; minHeight = dp(50); setOnClickListener { action() }
    }
    private fun weighted() = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply { setMargins(dp(4), 0, dp(4), dp(12)) }
    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
