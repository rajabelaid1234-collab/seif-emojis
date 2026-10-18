package com.seif.emojis.keyboard

import android.inputmethodservice.InputMethodService
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.media.AudioManager
import android.media.ToneGenerator

class SeifKeyboardService : InputMethodService() {
    private val faces = listOf(
        "⊂（♡⌂♡）⊃", "⊂(◉‿◉)つ", "ლ(・ヮ・ლ)", "(つ◉益◉)つ",
        "⊂(・▽・⊂)", "(っಠ‿ಠ)っ", "ʕっ•ᴥ•ʔっ", "（っ・∀・）っ",
        "(づ｡◕‿‿◕｡)づ", "(｡♥‿♥｡)", "(≧▽≦)", "(＾▽＾)",
        "(づ￣ ³￣)づ", "(•̀ᴗ•́)و", "(ಥ﹏ಥ)", "(¬‿¬)",
        "♡", "♥", "✨", "🌸", "🥺", "😂", "😍", "😎",
        "❤️", "💕", "😊", "😭", "🔥", "⭐", "🐱", "🐻",
        "(╯°□°）╯︵ ┻━┻", "¯\\_(ツ)_/¯", "(ง'̀-'́)ง", "ಠ_ಠ"
    )
    override fun onCreateInputView(): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(18, 24, 43))
            setPadding(6, 6, 6, 6)
        }
        val header = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        val title = TextView(this).apply {
            text = "✨ SEIF EMOJIS"
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(72, 226, 194))
            setPadding(8, 6, 8, 6)
        }
        val dismiss = Button(this).apply {
            text = "⌄"
            setOnClickListener { requestHideSelf(0) }
        }
        header.addView(title, LinearLayout.LayoutParams(0, -2, 1f))
        val slash = TextView(this).apply {
            text = "⚔️"
            textSize = 22f
            alpha = 0f
        }
        header.addView(slash)
        header.addView(dismiss)
        root.addView(header)
        val scroll = ScrollView(this)
        val grid = GridLayout(this).apply { columnCount = 3 }
        faces.forEach { face ->
            val b = Button(this).apply {
                text = face
                textSize = 17f
                isAllCaps = false
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.rgb(32, 42, 70))
                setOnClickListener {
                    currentInputConnection?.commitText(face, 1)
                    slash.animate().cancel()
                    slash.translationX = -90f
                    slash.alpha = 1f
                    slash.animate().translationX(90f).alpha(0f).setDuration(280).start()
                    if (getSharedPreferences("seif_settings", Context.MODE_PRIVATE).getBoolean("sword_sound", false)) {
                        try {
                            val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 30)
                            tone.startTone(ToneGenerator.TONE_PROP_NACK, 80)
                            slash.postDelayed({ tone.release() }, 140)
                        } catch (_: Exception) {}
                    }
                }
            }
            val lp = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(3, 3, 3, 3)
            }
            grid.addView(b, lp)
        }
        scroll.addView(grid)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        return root
    }
}
