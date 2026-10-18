package com.seif.emojis.keyboard
import android.app.Activity
import android.content.Context
import android.content.Intent
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
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState)
  val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;gravity=Gravity.CENTER;setPadding(24,30,24,30);setBackgroundColor(Color.rgb(13,16,32))}
  root.addView(TextView(this).apply{text="✨ SEIF EMOJIS ✨";textSize=29f;setTextColor(Color.rgb(72,226,194));gravity=Gravity.CENTER})
  root.addView(TextView(this).apply{text="لوحة إيموجي عادية داخل لوحة المفاتيح، بدون زر عائم أو صلاحية الظهور فوق التطبيقات. اختَر الإيموجي أو الوجه ليُكتب مباشرة في الرسالة. فيها مجموعات كثيرة وبحث.";textSize=17f;setTextColor(Color.WHITE);gravity=Gravity.CENTER;setPadding(0,20,0,22)})
  root.addView(Button(this).apply{text="🌐 فتح الموقع الكامل SEIF EMOJIS";setOnClickListener{startActivity(Intent(this@MainActivity,WebActivity::class.java))}})
  root.addView(Button(this).apply{text="1) تفعيل لوحة المفاتيح";setOnClickListener{startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))}})
  root.addView(Button(this).apply{text="2) اختيار لوحة SEIF";setOnClickListener{(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()}})
  root.addView(TextView(this).apply{text="طريقة الاستخدام: فعّل لوحة SEIF من إعدادات الهاتف. افتح واتساب أو أي تطبيق، اضغط خانة الكتابة، ثم اختر SEIF من زر تبديل لوحة المفاتيح. استخدم البحث والتبويبات للإيموجيات والوجوه والرموز.";textSize=14f;setTextColor(Color.LTGRAY);gravity=Gravity.CENTER;setPadding(0,18,0,0)})
  setContentView(root)
 }
}