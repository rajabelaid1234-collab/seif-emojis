package com.seif.emojis.keyboard
import android.inputmethodservice.InputMethodService
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*
import android.text.TextWatcher
import android.text.Editable

class SeifKeyboardService : InputMethodService() {
 private val faces=listOf("⊂（♡⌂♡）⊃","⊂(◉‿◉)つ","⊂(・﹏・⊂)","ლ(・ヮ・ლ)","(つ◉益◉)つ","⊂(・ヮ・⊂)","⊂(・▽・⊂)","(っಠ‿ಠ)っ","ʕっ•ᴥ•ʔっ","（っ・∀・）っ","(づ｡◕‿‿◕｡)づ","(｡♥‿♥｡)","(≧▽≦)","(＾▽＾)","(づ￣ ³￣)づ","(•̀ᴗ•́)و","(ಥ﹏ಥ)","(¬‿¬)","(ง'̀-'́)ง","(╯°□°）╯︵ ┻━┻","¯\\_(ツ)_/¯","ಠ_ಠ","(ノಠ益ಠ)ノ彡┻━┻","(｡•́︿•̀｡)","(๑˃ᴗ˂)ﻭ","(✿◠‿◠)","(っ˘ω˘ς )","٩(◕‿◕｡)۶","ヽ(・∀・)ﾉ","(☞ﾟヮﾟ)☞","☜(ﾟヮﾟ☜)","(๑•̀ㅂ•́)و✧","(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧","(￣▽￣)","(｡•̀ᴗ-)✧","ʕ•ᴥ•ʔ","ฅ^•ﻌ•^ฅ","(=^･ω･^=)","(ᵔᴥᵔ)","(•ө•)♡")
 private val groups=linkedMapOf(
 "😀 وجوه ومشاعر" to "😀 😃 😄 😁 😆 😅 😂 🤣 🥲 ☺️ 😊 😇 🙂 🙃 😉 😌 😍 🥰 😘 😗 😙 😚 😋 😛 😝 😜 🤪 🤨 🧐 🤓 😎 🥸 🤩 🥳 😏 😒 😞 😔 😟 😕 🙁 ☹️ 😣 😖 😫 😩 🥺 😢 😭 😤 😠 😡 🤬 🤯 😳 🥵 🥶 😱 😨 😰 😥 😓 🤗 🤔 🫣 🤭 🫢 🫡 🤫 🫠 🤥 😶 😐 🫤 😑 😬 🙄 😯 😦 😧 😮 😲 🥱 😴 🤤 😪 😮‍💨 😵 😵‍💫 🤐 🥴 🤢 🤮 🤧 😷 🤒 🤕 🤑 🤠 😈 👿 👹 👺 💀 ☠️ 👻 👽 👾 🤖 💩",
 "❤️ قلوب وحب" to "❤️ 🧡 💛 💚 💙 💜 🖤 🤍 🤎 🩷 🩵 🩶 💔 ❤️‍🔥 ❤️‍🩹 ❣️ 💕 💞 💓 💗 💖 💘 💝 💟 ♥️ 💌 💋 💯 💢 💥 💫 💦 💨 💬 💭 💤",
 "👋 أيدي وجسم" to "👋 🤚 🖐️ ✋ 🖖 🫱 🫲 🫳 🫴 👌 🤌 🤏 ✌️ 🤞 🫰 🤟 🤘 🤙 👈 👉 👆 🖕 👇 ☝️ 🫵 👍 👎 ✊ 👊 🤛 🤜 👏 🙌 🫶 👐 🤲 🤝 🙏 ✍️ 💅 🤳 💪 🦾 🦿 🦵 🦶 👂 🦻 👃 🧠 🫀 🫁 🦷 🦴 👀 👁️ 👅 👄 🫦",
 "👨 أشخاص" to "👶 🧒 👦 👧 🧑 👱 👨 🧔 👩 🧓 👴 👵 🙍 🙎 🙅 🙆 💁 🙋 🧏 🙇 🤦 🤷 👮 🕵️ 💂 🥷 👷 🫅 🤴 👸 👳 🧕 🤵 👰 🤰 🫃 🫄 🤱 👼 🎅 🤶 🦸 🦹 🧙 🧚 🧛 🧜 🧝 🧞 🧟 💆 💇 🚶 🧍 🧎 🏃 💃 🕺 🧖 🧗 🏇 🏂 🏄 🚣 🏊 🚴 🚵 🤸 🤼 🤽 🤾 🤹 🧘 🛀 🛌",
 "🐶 حيوانات" to "🐵 🐒 🦍 🦧 🐶 🐕 🦮 🐩 🐺 🦊 🦝 🐱 🐈 🦁 🐯 🐅 🐆 🐴 🫎 🫏 🐎 🦄 🦓 🦌 🦬 🐮 🐂 🐃 🐄 🐷 🐖 🐗 🐏 🐑 🐐 🐪 🐫 🦙 🦒 🐘 🦣 🦏 🦛 🐭 🐁 🐀 🐹 🐰 🐇 🐿️ 🦫 🦔 🦇 🐻 🐻‍❄️ 🐨 🐼 🦥 🦦 🦨 🦘 🦡 🐾 🦃 🐔 🐓 🐣 🐤 🐥 🐦 🐧 🕊️ 🦅 🦆 🦢 🦉 🦤 🪶 🦩 🦚 🦜 🪽 🐸 🐊 🐢 🦎 🐍 🐲 🐉 🦕 🦖 🐳 🐋 🐬 🦭 🐟 🐠 🐡 🦈 🐙 🐚 🪸 🪼 🦀 🦞 🦐 🦑 🦪 🐌 🦋 🐛 🐜 🐝 🪲 🐞 🦗 🪳 🕷️ 🕸️ 🦂 🦟 🪰 🪱 🦠",
 "🌿 طبيعة" to "💐 🌸 💮 🪷 🏵️ 🌹 🥀 🌺 🌻 🌼 🌷 🪻 🌱 🪴 🌲 🌳 🌴 🌵 🌾 🌿 ☘️ 🍀 🍁 🍂 🍃 🍄 🪨 🌍 🌎 🌏 🌐 🌑 🌒 🌓 🌔 🌕 🌖 🌗 🌘 🌙 🌚 🌛 🌜 🌝 🌞 🪐 ⭐ 🌟 🌠 🌌 ☀️ 🌤️ ⛅ 🌥️ ☁️ 🌦️ 🌧️ ⛈️ 🌩️ 🌨️ ❄️ ☃️ ⛄ 🌬️ 🌪️ 🌫️ 🌈 ☔ 💧 💦 🌊",
 "🍎 طعام وشراب" to "🍏 🍎 🍐 🍊 🍋 🍌 🍉 🍇 🍓 🫐 🍈 🍒 🍑 🥭 🍍 🥥 🥝 🍅 🫒 🥑 🍆 🥔 🥕 🌽 🌶️ 🫑 🥒 🥬 🥦 🧄 🧅 🥜 🫘 🍞 🥐 🥖 🫓 🥨 🥯 🥞 🧇 🧀 🍖 🍗 🥩 🥓 🍔 🍟 🍕 🌭 🥪 🌮 🌯 🫔 🥙 🧆 🥚 🍳 🥘 🍲 🫕 🥣 🥗 🍿 🧈 🧂 🥫 🍱 🍘 🍙 🍚 🍛 🍜 🍝 🍠 🍢 🍣 🍤 🍥 🍡 🥟 🥠 🥡 🍦 🍧 🍨 🍩 🍪 🎂 🍰 🧁 🥧 🍫 🍬 🍭 🍮 🍯 🍼 🥛 ☕ 🫖 🍵 🍶 🍾 🍷 🍸 🍹 🍺 🍻 🥂 🥃 🥤 🧋 🧃 🧉 🧊",
 "⚽ رياضة وفنون" to "⚽ 🏀 🏈 ⚾ 🥎 🎾 🏐 🏉 🥏 🎱 🪀 🏓 🏸 🏒 🏑 🥍 🏏 🪃 🥅 ⛳ 🪁 🏹 🎣 🤿 🥊 🥋 🎽 🛹 🛼 🛷 ⛸️ 🥌 🎿 ⛷️ 🏂 🏋️ 🤼 🤸 ⛹️ 🤺 🤾 🏌️ 🏇 🧘 🏄 🏊 🚣 🧗 🚵 🚴 🏆 🥇 🥈 🥉 🏅 🎖️ 🎫 🎟️ 🎪 🤹 🎭 🩰 🎨 🎬 🎤 🎧 🎼 🎹 🥁 🪘 🎷 🎺 🪗 🎸 🪕 🎻 🪈 🎲 ♟️ 🎯 🎳 🎮 🕹️ 🧩 🪄 🪅 🪩 🎰",
 "🚗 سفر وأماكن" to "🚗 🚕 🚙 🚌 🚎 🏎️ 🚓 🚑 🚒 🚐 🛻 🚚 🚛 🚜 🦽 🦼 🛴 🚲 🛵 🏍️ 🛺 🚨 🚔 🚘 🚖 🚡 🚠 🚃 🚋 🚞 🚝 🚄 🚅 🚈 🚂 🚆 🚇 🚊 🚉 ✈️ 🛫 🛬 🛩️ 💺 🚁 🚀 🛸 🚢 🛥️ 🛳️ ⛴️ 🚤 ⚓ 🛟 ⛽ 🚧 🚦 🚥 🗺️ 🗿 🗽 🗼 🏰 🏯 🏟️ 🎡 🎢 🎠 ⛲ 🏖️ 🏝️ 🏜️ 🌋 ⛰️ 🏔️ 🗻 🏕️ ⛺ 🛖 🏠 🏡 🏘️ 🏭 🏢 🏬 🏥 🏦 🏨 🏪 🏫 💒 🏛️ ⛪ 🕌 🛕 🕍 ⛩️ 🕋",
 "💡 أشياء" to "⌚ 📱 📲 💻 🖥️ 🖨️ ⌨️ 🖱️ 🖲️ 💽 💾 💿 📀 📼 📷 📸 📹 🎥 📽️ 🎞️ 📞 ☎️ 📟 📠 📺 📻 🎙️ 🎚️ 🎛️ 🧭 ⏱️ ⏲️ ⏰ 🕰️ ⌛ ⏳ 📡 🔋 🪫 🔌 💡 🔦 🕯️ 🪔 🧯 💸 💵 💴 💶 💷 🪙 💰 💳 🧾 💎 🧰 🔧 🔨 ⚒️ 🛠️ ⛏️ 🪚 🔩 ⚙️ 🧱 ⛓️ 🧲 🧹 🧺 🧻 🚽 🚿 🛁 🧼 🪥 🧽 🛒",
 "🔣 رموز" to "☮️ ✝️ ☪️ 🕉️ ☸️ ✡️ 🔯 🕎 ☯️ ☦️ 🛐 ⛎ ♈ ♉ ♊ ♋ ♌ ♍ ♎ ♏ ♐ ♑ ♒ ♓ 🆔 ⚛️ ☢️ ☣️ 📴 📳 🈶 🈚 🈸 🈺 🈷️ ✴️ 🆚 💮 🉐 ㊙️ ㊗️ 🈴 🈵 🈹 🈲 🅰️ 🅱️ 🆎 🆑 🅾️ 🆘 ❌ ⭕ 🛑 ⛔ 🚫 💯 ♨️ 🚷 🚯 🚳 🚱 🔞 📵 🚭 ❗ ❕ ❓ ❔ ‼️ ⁉️ ⚠️ 🚸 🔱 ⚜️ 🔰 ♻️ ✅ 🈯 💹 ❇️ ✳️ ❎ 🌐 💠 🌀 💤 ♿ 🅿️ 🈳 🛂 🛃 🛄 🛅 🔃 🔄 🔙 🔚 🔛 🔜 🔝 ⬆️ ↗️ ➡️ ↘️ ⬇️ ↙️ ⬅️ ↖️ ↕️ ↔️ ↩️ ↪️ ⤴️ ⤵️ 🔀 🔁 🔂 ▶️ ⏩ ⏭️ ⏯️ ◀️ ⏪ ⏮️ 🔼 ⏫ 🔽 ⏬ ⏹️ ⏺️ ⏏️ 📶 🛜 ♀️ ♂️ ⚧️ ⚕️ ♾️ ➕ ➖ ➗ ✖️ 🟰 💲 💱 ™️ ©️ ®️ 〰️ ➰ ➿ 🔟 🔢 #️⃣ *️⃣ 0️⃣ 1️⃣ 2️⃣ 3️⃣ 4️⃣ 5️⃣ 6️⃣ 7️⃣ 8️⃣ 9️⃣ 🔠 🔡 🔤 🆒 🆓 🆕 🆖 🆗"
 )
 private var tab="😀 إيموجي"
 private var query=""
 private lateinit var body:LinearLayout
 override fun onCreateInputView():View {
  val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setBackgroundColor(Color.rgb(18,24,43));setPadding(5,4,5,4)}
  val header=LinearLayout(this).apply{gravity=Gravity.CENTER_VERTICAL}
  header.addView(TextView(this).apply{text="✨ SEIF EMOJIS";textSize=15f;typeface=Typeface.DEFAULT_BOLD;setTextColor(Color.rgb(72,226,194));setPadding(7,4,7,4)},LinearLayout.LayoutParams(0,-2,1f))
  header.addView(Button(this).apply{text="⌄";setOnClickListener{requestHideSelf(0)}});root.addView(header)
  val search=EditText(this).apply{hint="🔎 ابحث عن إيموجي أو وجه";singleLine=true;textSize=14f;setTextColor(Color.WHITE);setHintTextColor(Color.LTGRAY)}
  root.addView(search)
  val tabs=LinearLayout(this).apply{gravity=Gravity.CENTER}
  listOf("😀 إيموجي","٩٩ وجوه","♡ رموز").forEach{t->tabs.addView(Button(this).apply{text=t;textSize=11f;isAllCaps=false;setOnClickListener{tab=t;render()}},LinearLayout.LayoutParams(0,-2,1f))}
  root.addView(tabs)
  val scroll=ScrollView(this);body=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL};scroll.addView(body);root.addView(scroll,LinearLayout.LayoutParams(-1,0,1f))
  search.addTextChangedListener(object:TextWatcher{override fun beforeTextChanged(s:CharSequence?,start:Int,count:Int,after:Int){};override fun onTextChanged(s:CharSequence?,start:Int,before:Int,count:Int){query=s?.toString()?.trim()?: "";render()};override fun afterTextChanged(s:Editable?){}})
  render();return root
 }
 private fun render(){
  if(!::body.isInitialized)return
  body.removeAllViews();val q=query.lowercase()
  if(tab=="٩٩ وجوه"){grid(body,faces.filter{q.isEmpty()||it.contains(q)});return}
  if(tab=="♡ رموز"){val s="♡ ♥ ❤ ❣ ❥ ღ ❦ ❧ ✨ ⭐ 🌟 💫 🔥 ⚡ ☀ ☁ ☂ ☃ ☾ ☽ ♠ ♣ ♦ ♪ ♫ ♬ ✓ ✔ ✕ ✖ ✗ ★ ☆ ☎ ☮ ☯ ⚠ ⚜ ☑ ☐ ➜ ➤ ➵ ➸ ➺ ➻ ➼ ➔ ➡ ⇢ ⇧ ⇩ ⇦ ↔ ↕ ↗ ↘ ↙ ↖ ∞ ∴ ∵ ≈ ≠ ≤ ≥ ± × ÷ √ ∑ ∆ π Ω α β γ δ λ ∫ ∩ ∪ ⊂ ⊃ ⊆ ⊇ ∈ ∉ ∧ ∨ ¬ ∀ ∃ ∅ ∇ ∂ ∝ ∠ ⊥".split(" ");grid(body,s.filter{q.isEmpty()||it.contains(q)});return}
  groups.forEach{(name,raw)->val all=raw.split(Regex("\\s+")).filter{it.isNotBlank()};val list=all.filter{q.isEmpty()||it.contains(q)||name.lowercase().contains(q)};if(list.isNotEmpty()){body.addView(TextView(this).apply{text=name;textSize=13f;typeface=Typeface.DEFAULT_BOLD;setTextColor(Color.rgb(72,226,194));setPadding(6,8,6,3)});grid(body,list)}}
 }
 private fun grid(parent:LinearLayout,items:List<String>){
  val g=GridLayout(this).apply{columnCount=7}
  items.forEach{item->val v=TextView(this).apply{text=item;textSize=if(item.length>4)18f else 25f;gravity=Gravity.CENTER;setTextColor(Color.WHITE);setBackgroundColor(Color.rgb(32,42,70));setPadding(2,8,2,8);contentDescription=item;setOnClickListener{currentInputConnection?.commitText(item,1)}}
   g.addView(v,GridLayout.LayoutParams().apply{width=0;height=GridLayout.LayoutParams.WRAP_CONTENT;columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f);setMargins(2,2,2,2)})
  };parent.addView(g)
 }
}