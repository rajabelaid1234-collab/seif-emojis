package com.seif.emojis.keyboard

import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.media.AudioManager
import android.media.ToneGenerator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

class FloatingBubbleService : Service() {
    private lateinit var wm: WindowManager
    private var bubble: TextView? = null
    private var menu: LinearLayout? = null
    private var params: WindowManager.LayoutParams? = null
    private var expanded = false

    private fun newParams(): WindowManager.LayoutParams {
        val type = if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE
        return WindowManager.LayoutParams(-2, -2, type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT).apply { gravity = Gravity.TOP or Gravity.START; x = 20; y = 240 }
    }

    override fun onCreate() {
        super.onCreate()
        if (!android.provider.Settings.canDrawOverlays(this)) { stopSelf(); return }
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val p = newParams(); params = p
        bubble = TextView(this).apply {
            text = "😊"; textSize = 24f; gravity = Gravity.CENTER; setTextColor(Color.WHITE)
            setPadding(13, 8, 13, 8); setBackgroundColor(Color.rgb(105, 76, 205))
            contentDescription = "اسحب لتحريك زر SEIF EMOJIS، واضغط لفتح الوجوه"
            setOnTouchListener(object : View.OnTouchListener {
                var downX = 0f; var downY = 0f; var startX = 0; var startY = 0; var moved = false
                override fun onTouch(v: View, e: MotionEvent): Boolean {
                    when (e.action) {
                        MotionEvent.ACTION_DOWN -> { downX=e.rawX; downY=e.rawY; startX=p.x; startY=p.y; moved=false; return true }
                        MotionEvent.ACTION_MOVE -> {
                            val dx=(e.rawX-downX).toInt(); val dy=(e.rawY-downY).toInt()
                            if (kotlin.math.abs(dx)>6 || kotlin.math.abs(dy)>6) moved=true
                            if (moved) { p.x=(startX+dx).coerceAtLeast(0); p.y=(startY+dy).coerceAtLeast(0); try { wm.updateViewLayout(v,p) } catch (_:Exception) {} }
                            return true
                        }
                        MotionEvent.ACTION_UP -> { if (!moved) toggleMenu(); return true }
                    }
                    return false
                }
            })
        }
        wm.addView(bubble,p)
    }

    private fun swordEffect() {
        val slash = TextView(this).apply {
            text = "⚔️"
            textSize = 42f
            gravity = Gravity.CENTER
            setShadowLayer(18f, 0f, 0f, Color.rgb(120, 220, 255))
        }
        val p = WindowManager.LayoutParams(
            -2, -2,
            if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START; x = 0; y = (resources.displayMetrics.heightPixels * 0.42f).toInt() }
        try {
            wm.addView(slash, p)
            val anim = TranslateAnimation(-resources.displayMetrics.widthPixels.toFloat(), resources.displayMetrics.widthPixels.toFloat(), 0f, -resources.displayMetrics.heightPixels * 0.18f)
            anim.duration = 420
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) { try { wm.removeView(slash) } catch (_: Exception) {} }
            })
            slash.startAnimation(anim)
        } catch (_: Exception) {}
        if (getSharedPreferences("seif_settings", Context.MODE_PRIVATE).getBoolean("sword_sound", false)) {
            try {
                val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 35)
                tone.startTone(ToneGenerator.TONE_PROP_NACK, 100)
                slash.postDelayed({ tone.release() }, 180)
            } catch (_: Exception) {}
        }
    }

    private fun toggleMenu() {
        if (expanded) { removeMenu(); return }
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(7, 7, 7, 7)
            setBackgroundColor(Color.rgb(18, 24, 43))
        }
        val header = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        header.addView(TextView(this).apply {
            text = "✨ SEIF EMOJIS"
            textSize = 14f
            setTextColor(Color.rgb(72, 226, 194))
            setPadding(6, 4, 6, 4)
        }, LinearLayout.LayoutParams(0, -2, 1f))
        header.addView(Button(this).apply { text = "✕"; contentDescription = "إخفاء القائمة"; setOnClickListener { removeMenu() } })
        box.addView(header)
        box.addView(TextView(this).apply {
            text = "اضغط للنسخ، أو استخدم لوحة SEIF للإدراج المباشر"
            textSize = 11f
            setTextColor(Color.LTGRAY)
            setPadding(5, 2, 5, 6)
        })
        val textInput = EditText(this).apply {
            hint = "اكتب الكلمة أو الاسم هنا..."
            textSize = 16f
            setSingleLine(true)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            setPadding(dp(10), dp(8), dp(10), dp(8))
            setBackgroundColor(Color.rgb(35, 43, 65))
        }
        box.addView(textInput, LinearLayout.LayoutParams(dp(280), dp(48)).apply {
            setMargins(0, dp(4), 0, dp(5))
        })
        box.addView(TextView(this).apply {
            text = "اختر زخرفة للنص ثم انسخها والصقها في أي تطبيق"
            textSize = 11f
            setTextColor(Color.LTGRAY)
            setPadding(4, 2, 4, 6)
        })
        val styles = listOf(
            "꧁  النص  ꧂" to { t: String -> "꧁༺ $t ༻꧂" },
            "『 النص 』" to { t: String -> "『 $t 』" },
            "亗 النص 亗" to { t: String -> "亗 $t 亗" },
            "♛ النص ♛" to { t: String -> "♛ $t ♛" },
            "༺ النص ༻" to { t: String -> "༺ $t ༻" },
            "×͜× النص" to { t: String -> "×͜× $t ×͜×" },
            "★ النص ★" to { t: String -> "★彡 $t 彡★" },
            "♕ النص ♕" to { t: String -> "♕ $t ♕" },
            "『ツ』 النص" to { t: String -> "『ツ』$t『ツ』" },
            "• النص •" to { t: String -> "•°¯¨ $t ¨¯°•" }
        )
        val styleGrid = android.widget.GridLayout(this).apply { columnCount = 2 }
        styles.forEach { (label, transform) ->
            styleGrid.addView(Button(this).apply {
                text = label
                textSize = 13f
                isAllCaps = false
                setTextColor(Color.WHITE)
                setPadding(2, 3, 2, 3)
                setOnClickListener {
                    val raw = textInput.text?.toString().orEmpty().trim()
                    if (raw.isEmpty()) {
                        Toast.makeText(this@FloatingBubbleService, "اكتب النص أولاً", Toast.LENGTH_SHORT).show()
                    } else {
                        val decorated = transform(raw)
                        val cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cb.setPrimaryClip(ClipData.newPlainText("SEIF EMOJIS", decorated))
                        Toast.makeText(this@FloatingBubbleService, "تم نسخ النص المزخرف ✓ الصقه في التطبيق", Toast.LENGTH_SHORT).show()
                    }
                }
            }, android.widget.GridLayout.LayoutParams().apply {
                width = 0
                height = WindowManager.LayoutParams.WRAP_CONTENT
                columnSpec = android.widget.GridLayout.spec(android.widget.GridLayout.UNDEFINED, 1f)
                setMargins(2, 2, 2, 2)
            })
        }
        box.addView(styleGrid)
        box.addView(TextView(this).apply {
            text = "وجوه وأشكال سريعة — اضغط لنسخها"
            textSize = 11f
            setTextColor(Color.LTGRAY)
            setPadding(4, dp(7), 4, 3)
        })
        val choices = listOf(
            "⊂（♡⌂♡）⊃", "⊂(◉‿◉)つ", "ლ(・ヮ・ლ)", "(っಠ‿ಠ)っ",
            "ʕっ•ᴥ•ʔっ", "(づ｡◕‿‿◕｡)づ", "(≧▽≦)", "(＾▽＾)",
            "(ಥ﹏ಥ)", "(¬‿¬)", "¯\\_(ツ)_/¯", "(╯°□°）╯︵ ┻━┻",
            "꧁༺", "༻꧂", "『 』", "【 】", "乂", "彡",
            "亗", "々", "〆", "𓆩 𓆪", "ঔৣ", "×͜×"
        )
        val grid = android.widget.GridLayout(this).apply { columnCount = 4 }
        choices.forEach { face ->
            grid.addView(Button(this).apply {
                text = face
                textSize = 16f
                isAllCaps = false
                setPadding(2, 4, 2, 4)
                setOnClickListener {
                    val cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cb.setPrimaryClip(ClipData.newPlainText("SEIF EMOJIS", face))
                    Toast.makeText(this@FloatingBubbleService, "تم النسخ — الصقه في خانة الكتابة", Toast.LENGTH_SHORT).show()
                }
            }, android.widget.GridLayout.LayoutParams().apply {
                width = 0
                height = WindowManager.LayoutParams.WRAP_CONTENT
                columnSpec = android.widget.GridLayout.spec(android.widget.GridLayout.UNDEFINED, 1f)
                setMargins(2, 2, 2, 2)
            })
        }
        val scroll = android.widget.ScrollView(this).apply { addView(grid) }
        box.addView(scroll, LinearLayout.LayoutParams(dp(280), dp(150)))
        val p = newParams().apply { x = params?.x ?: 20; y = (params?.y ?: 240) + 60; flags = flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv() }
        menu = box
        try { wm.addView(box, p); expanded = true; textInput.requestFocus() } catch (_: Exception) { menu = null }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
    private fun removeMenu() { menu?.let { try { wm.removeView(it) } catch (_:Exception) {} }; menu=null; expanded=false }
    override fun onDestroy() { removeMenu(); bubble?.let { try { wm.removeView(it) } catch (_:Exception) {} }; bubble=null; super.onDestroy() }
    override fun onBind(intent: Intent?): IBinder? = null
}