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
        val box=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(7,7,7,7); setBackgroundColor(Color.rgb(18,24,43)) }
        val header=LinearLayout(this).apply { gravity=Gravity.CENTER_VERTICAL }
        header.addView(TextView(this).apply { text="SEIF EMOJIS"; setTextColor(Color.rgb(72,226,194)); setPadding(6,4,6,4) }, LinearLayout.LayoutParams(0,-2,1f))
        header.addView(Button(this).apply { text="إخفاء ✕"; setOnClickListener { stopSelf() } })
        box.addView(header)
        listOf("(＾▽＾)","(♡˙︶˙♡)","(≧▽≦)","ʕ•ᴥ•ʔ","(╥﹏╥)","❤️").forEach { face ->
            box.addView(Button(this).apply {
                text=face; textSize=18f; isAllCaps=false
                setOnClickListener {
                    val cb=getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cb.setPrimaryClip(ClipData.newPlainText("SEIF EMOJIS",face))
                    swordEffect()
                    Toast.makeText(this@FloatingBubbleService,"تم النسخ؛ الصقه في خانة الكتابة",Toast.LENGTH_SHORT).show()
                    removeMenu()
                }
            })
        }
        val p=newParams().apply { x=params?.x ?: 20; y=(params?.y ?: 240)+60 }
        menu=box
        try { wm.addView(box,p); expanded=true } catch (_:Exception) { menu=null }
    }
    private fun removeMenu() { menu?.let { try { wm.removeView(it) } catch (_:Exception) {} }; menu=null; expanded=false }
    override fun onDestroy() { removeMenu(); bubble?.let { try { wm.removeView(it) } catch (_:Exception) {} }; bubble=null; super.onDestroy() }
    override fun onBind(intent: Intent?): IBinder? = null
}