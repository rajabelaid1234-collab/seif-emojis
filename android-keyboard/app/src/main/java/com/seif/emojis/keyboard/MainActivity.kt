package com.seif.emojis.keyboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.net.Uri
import android.app.AlertDialog
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
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
        content.addView(infoCard("📴 بدون إنترنت", "إيموجيات ووجوه وزخرفة نصوص وألعاب واسم مخفي ومفضلة. تحليل الوجه موجود في قسم الإنترنت."), match())
        content.addView(actionButton("📴 فتح الأدوات دون إنترنت", accent) { startActivity(Intent(this, WebActivity::class.java).putExtra(WebActivity.EXTRA_MODE, WebActivity.MODE_OFFLINE)) })
        content.addView(infoCard("🌐 مع الإنترنت", "افتح الموقع الكامل كما يظهر أونلاين، بما في ذلك أدواته وأزرار التنزيل والمحتوى المتصل."), match())
        content.addView(actionButton("🌐 فتح الموقع الكامل أونلاين", Color.rgb(38, 51, 78)) { startActivity(Intent(this, WebActivity::class.java).putExtra(WebActivity.EXTRA_MODE, WebActivity.MODE_ONLINE)) })
        content.addView(TextView(this).apply { text = "⌨️ لوحة المفاتيح"; textSize = 19f; typeface = Typeface.DEFAULT_BOLD; setTextColor(accent); setPadding(0, dp(18), 0, dp(4)) })
        content.addView(actionButton("١. تفعيل لوحة SEIF", Color.rgb(38, 51, 78)) { startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) })
        content.addView(actionButton("٢. اختيار لوحة SEIF للكتابة", Color.rgb(38, 51, 78)) { (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker() })
        content.addView(TextView(this).apply { text = "اختر «بدون إنترنت» عند عدم توفر البيانات، أو «مع الإنترنت» لفتح نسخة الموقع الكاملة. لا تحتاج إلى تسجيل دخول."; textSize = 13f; setTextColor(Color.LTGRAY); setPadding(0, dp(16), 0, 0) })
        content.addView(actionButton("🔄 فحص تحديث التطبيق", Color.rgb(38, 51, 78)) { checkForUpdate(manual = true) })
        content.addView(TextView(this).apply { text = "ℹ️ الأدوات المحلية والألعاب الأساسية مضمّنة. ميزات الإنترنت تُفتح منفصلة ولا تُحمّل الموقع داخل النسخة المحلية."; textSize = 12f; setTextColor(Color.LTGRAY); setPadding(0, dp(10), 0, dp(4)) })
        setContentView(ScrollView(this).apply { isFillViewport = true; addView(content); setBackgroundColor(bg) })
        checkForUpdate(manual = false)
    }

    private fun checkForUpdate(manual: Boolean) {
        Thread {
            var latestTag: String? = null
            var releaseUrl: String? = null
            try {
                val connection = (URL("https://api.github.com/repos/rajabelaid1234-collab/seif-emojis/releases/latest").openConnection() as HttpURLConnection).apply {
                    connectTimeout = 5000
                    readTimeout = 5000
                    setRequestProperty("Accept", "application/vnd.github+json")
                }
                try {
                    if (connection.responseCode in 200..299) {
                        val json = JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
                        latestTag = json.optString("tag_name").takeIf { it.isNotBlank() }
                        releaseUrl = json.optString("html_url").takeIf { it.startsWith("https://") }
                    }
                } finally { connection.disconnect() }
            } catch (_: Exception) { /* Offline mode must continue to work without network. */ }
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                val current = packageManager.getPackageInfo(packageName, 0).versionName ?: "0"
                val latestVersion = latestTag?.removePrefix("android-keyboard-v") ?: ""
                if (latestVersion.isNotBlank() && latestVersion != current && latestVersion.matches(Regex("[0-9]+(\\.[0-9]+)*"))) {
                    AlertDialog.Builder(this)
                        .setTitle("✨ تحديث جديد متوفر")
                        .setMessage("الإصدار المثبّت: $current\nالإصدار الجديد: $latestVersion\nهل تريد فتح صفحة تنزيل التحديث؟")
                        .setPositiveButton("تنزيل التحديث") { _, _ ->
                            val target = releaseUrl ?: "https://github.com/rajabelaid1234-collab/seif-emojis/releases/latest"
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(target)))
                        }
                        .setNegativeButton("لاحقًا", null)
                        .show()
                } else if (manual) {
                    AlertDialog.Builder(this)
                        .setTitle("فحص التحديثات")
                        .setMessage(if (latestTag == null) "لم نتمكن من فحص التحديث. تحقق من الإنترنت وحاول مجددًا." else "أنت تستخدم أحدث إصدار متاح ($current).")
                        .setPositiveButton("حسنًا", null).show()
                }
            }
        }.start()
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
