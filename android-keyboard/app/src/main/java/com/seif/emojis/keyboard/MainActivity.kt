package com.seif.emojis.keyboard

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {
    private val bg = Color.rgb(11, 16, 32)
    private val card = Color.rgb(21, 29, 50)
    private val accent = Color.rgb(94, 234, 212)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(18), dp(12), dp(18))
            setBackgroundColor(bg)
        }
        content.addView(TextView(this).apply {
            text = "✨ SEIF EMOJIS"
            textSize = 27f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(accent)
            gravity = Gravity.CENTER
        })
        content.addView(TextView(this).apply {
            text = "ألعاب وأدوات إيموجي — واجهة سهلة للهاتف"
            textSize = 14f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(dp(6), dp(6), dp(6), dp(18))
        })

        val tiles = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        tiles.addView(tile("📴\nدون إنترنت") {
            startActivity(Intent(this, WebActivity::class.java).putExtra(WebActivity.EXTRA_MODE, WebActivity.MODE_OFFLINE))
        }, weighted())
        tiles.addView(tile("🌐\nأونلاين") {
            startActivity(Intent(this, WebActivity::class.java).putExtra(WebActivity.EXTRA_MODE, WebActivity.MODE_ONLINE))
        }, weighted())
        tiles.addView(tile("⌨️\nلوحة SEIF") { showKeyboardSetup() }, weighted())
        content.addView(tiles)

        content.addView(actionButton("😊 تشغيل زر إيموجي عائم", Color.rgb(105, 76, 205)) {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                AlertDialog.Builder(this)
                    .setTitle("إذن الزر العائم")
                    .setMessage("يلزم السماح لـ SEIF EMOJIS بالظهور فوق التطبيقات حتى يبقى زر 😊 ظاهرًا. هذا الإذن اختياري؛ لوحة المفاتيح تعمل بدونه.")
                    .setPositiveButton("فتح الإعدادات") { _, _ ->
                        startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                    }
                    .setNegativeButton("إلغاء", null).show()
            } else {
                startService(Intent(this, FloatingBubbleService::class.java))
                android.widget.Toast.makeText(this, "زر SEIF العائم يعمل. اضغط وجهًا لنسخه ثم الصقه، أو استخدم لوحة SEIF للإدراج المباشر.", android.widget.Toast.LENGTH_LONG).show()
            }
        }, match(dp(12)))

        content.addView(infoCard(
            "📴 النسخة المحلية",
            "ألعاب الإيموجي، الزخرفة، الوجوه، الرموز والاسم المخفي. هذه الأدوات الأساسية تعمل دون بيانات بعد تثبيت التطبيق."
        ), match(dp(12)))
        content.addView(infoCard(
            "🌐 النسخة المتصلة",
            "الموقع الكامل مع أدوات الإنترنت والميزات التي تحتاج إلى ملفات خارجية. زر تنزيل التطبيق يظهر في الموقع أونلاين فقط."
        ), match(dp(12)))

        content.addView(actionButton("🔄 فحص تحديث التطبيق", Color.rgb(38, 51, 78)) {
            checkForUpdate(manual = true)
        }, match(dp(8)))

        content.addView(TextView(this).apply {
            text = "ملاحظة: أندرويد لا يسمح للتطبيق بتفعيل لوحة المفاتيح أو منح إذن الظهور فوق التطبيقات سرًا. ستحتاج إلى تأكيد تفعيل لوحة SEIF مرة واحدة من إعدادات النظام."
            textSize = 12f
            setTextColor(Color.rgb(190, 204, 224))
            setPadding(dp(4), dp(6), dp(4), dp(4))
        })
        setContentView(ScrollView(this).apply {
            isFillViewport = true
            addView(content)
            setBackgroundColor(bg)
        })
    }

    private fun showKeyboardSetup() {
        AlertDialog.Builder(this)
            .setTitle("⌨️ إعداد لوحة SEIF")
            .setMessage("الخطوة الأولى: فعّل SEIF EMOJIS في إعدادات لوحة المفاتيح.\n\nالخطوة الثانية: ارجع إلى هنا واضغط اختيار لوحة SEIF. بعد ذلك ستعمل داخل معظم التطبيقات التي تقبل الكتابة.")
            .setPositiveButton("١. تفعيل اللوحة") { _, _ ->
                startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
            }
            .setNeutralButton("٢. اختيار اللوحة") { _, _ ->
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
            }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    private fun checkForUpdate(manual: Boolean) {
        Thread {
            var latestTag: String? = null
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
                    }
                } finally { connection.disconnect() }
            } catch (_: Exception) { }
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                val current = packageManager.getPackageInfo(packageName, 0).versionName ?: "0"
                val latestVersion = latestTag?.removePrefix("android-keyboard-v") ?: ""
                if (latestVersion.isNotBlank() && latestVersion != current && latestVersion.matches(Regex("[0-9]+(\\.[0-9]+)*"))) {
                    AlertDialog.Builder(this)
                        .setTitle("✨ تحديث جديد متوفر")
                        .setMessage("الإصدار المثبّت: $current\nالإصدار الجديد: $latestVersion\nهل تريد فتح صفحة تنزيل التحديث؟")
                        .setPositiveButton("تنزيل التحديث") { _, _ ->
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rajabelaid1234-collab/seif-emojis/releases/latest/download/app-debug.apk")))
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

    private fun tile(label: String, action: () -> Unit): Button = Button(this).apply {
        text = label
        textSize = 13f
        isAllCaps = false
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        setBackgroundColor(Color.rgb(38, 51, 78))
        minHeight = dp(74)
        setPadding(dp(3), dp(8), dp(3), dp(8))
        setOnClickListener { action() }
    }

    private fun weighted() = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
        setMargins(dp(3), 0, dp(3), dp(12))
    }

    private fun infoCard(title: String, description: String) = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(13), dp(12), dp(13), dp(12))
        setBackgroundColor(card)
        addView(TextView(this@MainActivity).apply {
            text = title
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
        })
        addView(TextView(this@MainActivity).apply {
            text = description
            textSize = 13f
            setTextColor(Color.rgb(190, 204, 224))
            setPadding(0, dp(5), 0, 0)
        })
    }

    private fun actionButton(label: String, color: Int, action: () -> Unit): Button = Button(this).apply {
        text = label
        textSize = 14f
        isAllCaps = false
        setTextColor(Color.WHITE)
        setBackgroundColor(color)
        minHeight = dp(48)
        setOnClickListener { action() }
    }

    private fun match(bottom: Int = 0) = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply { bottomMargin = bottom }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
