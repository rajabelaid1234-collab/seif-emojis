from pathlib import Path

html = r'''<!DOCTYPE html>
<html lang="ar" dir="rtl">
<head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="theme-color" content="#17152b">
<title>SEIF EMOJIS — مكتبة الإيموجيات النصية</title>
<style>
:root{--bg:#0d1020;--panel:#181d34;--panel2:#222944;--line:#343d5c;--text:#f7f7ff;--muted:#b8c0dc;--a:#a78bfa;--b:#5eead4;--pink:#f9a8d4}
*{box-sizing:border-box}body{margin:0;background:radial-gradient(ellipse at 50% -10%,#38275b 0,transparent 50%),var(--bg);color:var(--text);font-family:Tahoma,Arial,sans-serif;min-height:100vh}
.wrap{width:min(1180px,94%);margin:auto;padding:22px 0 45px}header{text-align:center;padding:20px 8px 25px}
.logo{font-size:clamp(2.1rem,6vw,3.8rem);font-weight:900;margin:0;background:linear-gradient(90deg,var(--b),#c4b5fd,var(--pink));color:transparent;background-clip:text;-webkit-background-clip:text}
header p{color:var(--muted);line-height:1.9;margin:10px 0}.tagline{display:inline-block;border:1px solid #55507b;border-radius:100px;padding:6px 13px;color:#e9ddff;font-size:.85rem;margin-top:8px}
.tabs,.categorybar{display:flex;gap:8px;flex-wrap:wrap;justify-content:center;margin:12px 0 18px}
button,.tab,.catbtn{font:inherit;cursor:pointer;border-radius:12px;padding:10px 14px;border:1px solid var(--line);background:var(--panel2);color:var(--text);transition:.15s}
button:hover,.tab:hover,.catbtn:hover{filter:brightness(1.13);transform:translateY(-1px)}
.tab.active,.catbtn.active,.primary{background:linear-gradient(110deg,#8b5cf6,#6558ee);border-color:transparent;color:white}
.panel{background:rgba(24,29,52,.94);border:1px solid var(--line);border-radius:20px;padding:20px;margin-bottom:17px;box-shadow:0 16px 42px #0002}
h2{font-size:1.25rem;margin:0 0 15px}.muted,.small{color:var(--muted);font-size:.88rem;line-height:1.8}
.searchrow{display:flex;gap:9px;flex-wrap:wrap;margin-bottom:14px}
input,textarea,select{font:inherit;color:var(--text);background:#10152a;border:1px solid var(--line);border-radius:12px;padding:12px;width:100%;outline:none}
input:focus,textarea:focus,select:focus{border-color:var(--a)}.searchrow input{flex:1;min-width:190px}
.emoji-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(170px,1fr));gap:10px}
.card{position:relative;min-height:102px;display:flex;flex-direction:column;align-items:center;justify-content:center;gap:9px;text-align:center;background:linear-gradient(145deg,#242b49,#171b31);border:1px solid var(--line);border-radius:15px;padding:15px 10px;cursor:pointer;overflow-wrap:anywhere;white-space:pre-wrap}
.card:hover{border-color:#9e8bfa;box-shadow:0 5px 22px #8b5cf622}.face{font-size:1.22rem;line-height:1.6;direction:ltr;unicode-bidi:plaintext}.label{color:var(--muted);font-size:.78rem}
.mini{position:absolute;top:5px;left:5px;padding:3px 7px;border-radius:7px;font-size:.72rem;background:#3d2540;color:#ffd6ec}
.mini.del{background:#4a1f31;color:#fecdd3}
.formgrid{display:grid;grid-template-columns:1fr 1fr;gap:12px}.field{display:flex;flex-direction:column;gap:7px}.field label{color:var(--muted);font-size:.9rem}
.actions{display:flex;gap:8px;flex-wrap:wrap;margin-top:12px}.secondary{background:var(--panel2)}.danger{background:#4a1f31;color:#fecdd3}
.notice{min-height:24px;color:var(--b);margin-top:10px;font-size:.9rem}.hidden{display:none!important}
.result{background:#10152a;border:1px solid var(--line);padding:15px;border-radius:14px;margin-top:14px;font-size:1.25rem;line-height:2;white-space:pre-wrap;overflow-wrap:anywhere;direction:auto}
.statrow{display:flex;gap:8px;flex-wrap:wrap;margin:0 0 16px}.stat{background:#11162b;border:1px solid var(--line);border-radius:10px;padding:7px 11px;color:var(--muted);font-size:.85rem}
footer{text-align:center;color:#8e95b6;font-size:.83rem;padding:12px}
@media(max-width:620px){.wrap{width:94%;padding-top:8px}.panel{padding:14px}.formgrid{grid-template-columns:1fr}.emoji-grid{grid-template-columns:repeat(auto-fill,minmax(140px,1fr))}.face{font-size:1.05rem}.logo{font-size:2.25rem}}
</style>
</head>
<body><div class="wrap">
<header><h1 class="logo">✨ SEIF EMOJIS ✨</h1><p>مكتبتك الخاصة للإيموجيات النصية والرموز والزخارف</p><span class="tagline">انسخ بنقرة • صنّف بطريقتك • اصنع مجموعتك</span></header>
<nav class="tabs">
<button class="tab active" data-view="gallery">🎨 المكتبة</button><button class="tab" data-view="convert">🔤 تحويل الكلمات</button><button class="tab" data-view="manage">➕ أضف إيموجياتك</button>
</nav>
<section id="gallery" class="panel">
<h2>🎭 مكتبة الإيموجيات</h2><div class="statrow"><span class="stat" id="countStat">0 شكل</span><span class="stat">اضغط على أي بطاقة لنسخها</span></div>
<div class="searchrow"><input id="search" type="search" placeholder="ابحث عن شكل أو كلمة أو رمز..."><button id="copyVisible" class="secondary">نسخ المعروض</button></div>
<div id="categories" class="categorybar"></div><div id="grid" class="emoji-grid"></div><p class="small">الأشكال المحفوظة محليًا تبقى في هذا المتصفح. صدّر نسخة احتياطية من قسم الإضافة حتى لا تفقد مجموعتك.</p>
</section>
<section id="convert" class="panel hidden"><h2>🔤 تحويل الكلمات إلى إيموجياتك</h2>
<div class="field"><label for="textToConvert">اكتب جملة</label><textarea id="textToConvert" rows="3" placeholder="مثال: أحبك وأرسل لك عناقًا"></textarea></div>
<div class="actions"><button id="doConvert" class="primary">حوّل الكلمات ✨</button><button id="clearConvert" class="secondary">مسح</button></div>
<div id="convertBox" class="hidden"><div id="convertOutput" class="result"></div><div class="actions"><button id="copyOutput" class="primary">نسخ النتيجة</button></div></div>
<p class="small">التحويل يعتمد على الكلمات التي تربطها أنت بالأشكال عند إضافتها. لا يغيّر الكلمات التي لم تحدد لها إيموجيًا.</p>
</section>
<section id="manage" class="panel hidden"><h2>➕ إضافة شكل أو رمز جديد</h2>
<div class="formgrid">
<div class="field"><label for="symbol">الشكل النصي / الإيموجي</label><textarea id="symbol" rows="2" placeholder="مثال: (づ ◕‿◕ )づ"></textarea></div>
<div class="field"><label for="title">الاسم الذي سيظهر</label><input id="title" placeholder="مثال: عناق لطيف"></div>
<div class="field"><label for="category">القسم</label><input id="category" list="categoryOptions" placeholder="مثال: العناق"><datalist id="categoryOptions"></datalist></div>
<div class="field"><label for="keywords">الكلمات المرتبطة (بفاصلة)</label><input id="keywords" placeholder="عناق، حضن، اشتياق"></div>
<div class="field" style="grid-column:1/-1"><label for="phrase">عبارة كاملة للنسخ (اختياري)</label><input id="phrase" placeholder="مثال: HUG FOR YOU~╰(*´︶`*)╯"></div>
</div>
<div class="actions"><button id="add" class="primary">حفظ في المكتبة</button><button id="clearForm" class="secondary">تفريغ الحقول</button><button id="backup" class="secondary">تصدير نسخة احتياطية</button><label class="tab" for="restore">استيراد نسخة</label><input id="restore" type="file" accept=".json,application/json" class="hidden"></div>
<p class="small">يمكنك إضافة مئات الأشكال على دفعات باستيراد ملف JSON. هذا الإصدار يحفظ البيانات على جهازك فقط، ولا يشارك إضافاتك تلقائيًا مع الزوار الآخرين.</p><div id="notice" class="notice" role="status"></div>
</section>
<footer>SEIF EMOJIS 💜 — مكتبة نصية من اختيارك</footer>
</div>
<script>
(()=>{
const KEY='seif-emojis-library-v2';
const seed=[
['عناق','⊂（♡⌂♡）⊃','عناق حب','عناق،حضن،حب'],['عناق','⊂(◉‿◉)つ','عناق سعيد','عناق،سعادة'],['عناق','⊂(・﹏・⊂)','عناق حزين','عناق،حزن'],['عناق','ლ(・ヮ・ლ)','تعال إلى هنا','عناق،تعال'],['عناق','(つ◉益◉)つ','عناق حماسي','عناق،حماس'],['عناق','⊂(・ヮ・⊂)','عناق لطيف','عناق،حضن'],['عناق','⊂(・▽・⊂)','عناق فرح','عناق،فرح'],['عناق','(っಠ‿ಠ)っ','عناق مشاكس','عناق،مزاح'],['عناق','ʕっ•ᴥ•ʔっ','عناق دب','عناق،دب'],['عناق','（っ・∀・）っ','عناق ودود','عناق،ود'],['عناق','ლ(・﹏・ლ)','أحتاج عناقًا','عناق،حزن'],['عناق','(づ￣ ³￣)づ','قبلة وعناق','عناق،قبلة،حب'],['عناق','(つ´∀｀)つ','عناق كبير','عناق،حضن'],['عناق','(っ´▽｀)っ','عناق مبتسم','عناق،ابتسامة'],['عناق','(つ≧▽≦)つ','عناق سعيد جدًا','عناق،فرح'],['عناق','(づ ◕‿◕ )づ','عناق لطيف','عناق،حب'],['عناق','(⊃｡•́‿•̀｡)⊃','عناق مواساة','عناق،مواساة'],['عناق','(っ*´∀｀*)っ','عناق محبة','عناق،حب'],['عناق','⊂(´・ω・｀⊂)','عناق هادئ','عناق،هدوء'],['عناق','⊂((・▽・))⊃','عناق مفتوح','عناق،حضن'],
['عناق مزدوج','（＾▽＾（＾▽＾＊）','عناق شخصين','عناق،أصدقاء'],['عناق مزدوج','(✿˶◕‿◕˶人◕ᴗ◕✿)','صداقة لطيفة','صداقة،عناق'],['عناق مزدوج','(●´Д｀人´Д｀●)','معًا','صداقة،حب'],['عناق مزدوج','ヽ(°∀°人´ヮ´)ﾉ','فرحة معًا','فرح،أصدقاء'],['عناق مزدوج','(￣∇￣人￣∇￣)','ابتسامتان','أصدقاء،سعادة'],['عناق مزدوج','(*´-ω・｀人´・ω-｀*)','عناق دافئ','عناق،حب'],['عناق مزدوج','（　＾＾）人（＾＾　）','صديقان','صداقة،أصدقاء'],['عناق مزدوج','(((*°▽°*)八(*°▽°*)))♪','احتفال الأصدقاء','أصدقاء،احتفال'],['عناق مزدوج','☆-(ノﾟДﾟ)八(ﾟДﾟ )ノ','تحية الأصدقاء','أصدقاء،تحية'],['عناق مزدوج','(☆⊙∇⊙)爻(⊙∇⊙☆)','مواجهة لطيفة','أصدقاء'],
['قبلات','(づ￣ ³￣)づ','أرسل قبلة','قبلة،حب،بوس'],['قبلات','⁽˙³˙⁾','بوس','قبلة،بوس'],['قبلات','( ˘ ³˘)♥','قبلة حب','قبلة،حب'],['قبلات','（´・｀ ）♡','قبلة صغيرة','قبلة،حب'],['قبلات','|°з°|','وجه بوس','قبلة'],['قبلات','(ΦзΦ)','بوس مضحك','قبلة،ضحك'],['قبلات','(☆´3｀)','قبلة','قبلة،بوس'],['قبلات','(´ε｀) （＾3＾）','قبلات','قبلة،حب'],['قبلات','(๑♡3♡๑)','قبلة حب','قبلة،حب'],['قبلات','(ɔˆ ³(ˆ⌣ˆc)','قبلة متبادلة','قبلة،حب'],['قبلات','(*´ -`)(´- `*)','قبلة حنونة','حب،قبلة'],['قبلات','♡(* ＾)(＾ *)♡','قبلة رومانسية','حب،قبلة'],['قبلات','(˘∀˘)/(μ‿μ) ❤','حب وقبلة','حب،قبلة'],['قبلات','(❀˙˘˙)♡(˙˘˙❀)','قبلة مزينة','حب،قبلة'],['قبلات','(^з^)-☆Chu!!','قبلة نجمة','قبلة،بوس'],['قبلات','( ˘ ³˘)❤','قبلة قلب','قبلة،حب'],['قبلات','(っ˘з(˘⌣˘ )','قبلة وعناق','قبلة،عناق'],['قبلات','(╯3╰)','وجه قبلة','قبلة'],['قبلات','٩(♡ε♡)۶','حب كثير','حب،قبلة'],['قبلات','♡ (⇀ 3 ↼)','قبلة لطيفة','قبلة،حب'],
['حب','♥‿♥','عيون حب','حب،إعجاب'],['حب','(♥‿♥)','حب','حب'],['حب','♡＾▽＾♡','قلب وفرح','حب،فرح'],['حب','(っ◔◡◔)っ ♥','أرسل لك الحب','حب'],['حب','(❣◡❣)','حب لطيف','حب'],['حب','乂❤‿❤乂','حب مزخرف','حب'],['حب','(✿ ♥‿♥)','حب وورد','حب،ورد'],['حب','(ღ˘⌣˘ღ)','محبة','حب'],['حب','('+'∀'+'●)♡','حب وابتسامة','حب'],['حب','(°◡°♡)','قلب سعيد','حب،سعادة'],['حب','(´∀｀)♡','حب وود','حب'],['حب','(๑´• .̫ •ू`๑)💓','قلب وردي','حب'],['حب','(◍•ᴗ•◍)♡ ✧*。','حب لطيف','حب،لطافة'],['حب','♡(.◜ω◝.)♡','قلبان','حب'],['حب','(⋈◍＞◡＜◍)。✧♡','حب مزين','حب'],['حب','(*ฅ́˘ฅ̀*) .｡.:*♡','حب خجول','حب،خجل'],['حب','( ˘͈ ᵕ ˘͈♡)','حب هادئ','حب'],['حب','♡ლ(๑癶ᴗ癶๑)ლ♡','حب وفرح','حب،فرح'],['حب','(≚ᄌ≚)ℒℴѵℯ❤','Love','حب،love'],['حب','♡ლ(-༗‿༗-)ლ♡','قلبان','حب'],
['فرح','＼(^o^)／','فرح واحتفال','فرح،سعادة'],['فرح','d=(´▽｀)=b','إعجاب','فرح،ممتاز'],['فرح','ヽ(＾Д＾)ﾉ','ضحكة سعيدة','فرح،ضحك'],['فرح','ヾ(≧▽≦*)o','فرح كبير','فرح،ضحك'],['فرح','(＾▽＾)','ابتسامة','فرح،ابتسامة'],['فرح','٩(◕‿◕｡)۶','سعادة','فرح،سعادة'],['فرح','ヽ(♡‿♡)ノ','فرح وحب','فرح،حب'],['فرح','(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧','سحر وفرح','فرح،نجوم'],['فرح','(≧◡≦)','ابتسامة كبيرة','فرح،ابتسامة'],['فرح','( ˶ˆᗜˆ˵ )','ابتسامة لطيفة','فرح'],['فرح','( ´ ▽ ` )b','تمام','تمام،موافق'],['فرح','(•̀ᴗ•́)و ̑̑','أستطيع فعلها','نجاح،قوة'],['فرح','(ദ്ദി˙ᗜ˙)','ممتاز','ممتاز،تمام'],['فرح','(,,> ᴗ <,,)','خجل وفرح','فرح،خجل'],['فرح','⸜(｡˃ ᵕ ˂ )⸝♡','سعادة مع قلب','فرح،حب'],
['حزن','(╥﹏╥)','بكاء','حزن،بكاء'],['حزن','(ಥ﹏ಥ)','دموع','حزن،بكاء'],['حزن','(｡•́︿•̀｡)','حزين','حزن'],['حزن','(つ﹏⊂)','أبكي','حزن،بكاء'],['حزن','(╥_╥)','دموع','حزن،بكاء'],['حزن','O﹏o','مصدوم وحزين','حزن،صدمة'],['حزن','(_　_|||)','إحباط','حزن،إحباط'],['حزن','(⇀‸↼‶)','منزعج','حزن،انزعاج'],['حزن','(｡╯︵╰｡)','حزين جدًا','حزن'],['حزن','╥﹏╥','دموع','حزن'],['حزن','( ᵒ̴̶̷̥́ _ᵒ̴̶̷̣̥̀ )♡','حزن مع حب','حزن،حب'],
['غضب','(╬ Ò﹏Ó)','غضب','غضب'],['غضب','(ノಠ益ಠ)ノ','غاضب جدًا','غضب'],['غضب','٩(๑`^´๑)۶','انزعاج','غضب،انزعاج'],['غضب','(눈_눈)','نظرة غضب','غضب،نظرة'],['غضب','(⇀‸↼)','متضايق','غضب،ضيق'],
['لطافة','≽^•⩊•^≼','قط لطيف','قط،لطيف'],['لطافة','(˶ᵔ ᵕ ᵔ˶)','وجه لطيف','لطيف،ابتسامة'],['لطافة','•⩊•','وجه صغير','لطيف'],['لطافة','૮₍｡´ᴖ ˔ ᴖ`｡₎ა','وجه لطيف','لطيف'],['لطافة','૮₍ ´• ˕ •` ₎ა','لطيف جدًا','لطيف'],['لطافة','ʕ•ᴥ•ʔ','دب صغير','دب،حيوان'],['لطافة','ᜊ( ‘ ⩊ ‘𖦹)ᜊ','لطافة','لطيف'],['لطافة','(˶˃ ᵕ ˂˶)','خجل لطيف','لطيف،خجل'],['لطافة','૮ ˶ᵔ ᵕ ᵔ˶ ა','وجه لطيف','لطيف'],['لطافة','(｡♥‿♥｡)','حب ولطافة','حب،لطيف'],['لطافة','(◍•ᴗ•◍)♡ ✧*。','لطيف مزخرف','لطيف،حب'],['لطافة','(๑♡⌓♡๑)','عيون حب','حب،لطيف'],['لطافة','(♡´艸`)','خجل','خجل،حب'],['لطافة','(⸝⸝๑ ̫ ๑⸝⸝⸝)','خجل لطيف','خجل'],['لطافة','(˶˃⤙˂˶)','وجه ظريف','لطيف'],['لطافة','(¬‿¬)','ابتسامة ماكرة','مكر،ابتسامة'],
['رموز','♡','قلب أبيض','قلب،حب'],['رموز','𐙚','زخرفة','زخرفة'],['رموز','𝄞','موسيقى','موسيقى'],['رموز','꧁','إطار زخرفي','زخرفة'],['رموز','✩','نجمة','نجمة'],['رموز','꧂','إطار زخرفي','زخرفة'],['رموز','♥','قلب','قلب،حب'],['رموز','❦','زخرفة نباتية','زخرفة'],['رموز','❥','قلب مزخرف','قلب'],['رموز','ღ','قلب مزخرف','قلب'],['رموز','⭑','نجمة','نجمة'],['رموز','✞︎','رمز','رمز'],['رموز','✮','نجمة','نجمة'],['رموز','࿔','زخرفة','زخرفة'],['رموز','𓆉','سلحفاة','حيوان'],['رموز','☾','قمر','قمر'],['رموز','✴︎','نجمة','نجمة'],['رموز','✰','نجمة','نجمة'],['رموز','✦','لمعة','لمعان'],['رموز','♛','تاج','فخامة'],['رموز','❀','زهرة','زهرة،ورد'],['رموز','➤','سهم','سهم'],['رموز','☯','رمز','رمز'],
['حيوانات','ʕっ•ᴥ•ʔっ','دب يعانق','دب،عناق'],['حيوانات','🐰','أرنب','أرنب'],['حيوانات','🐈‍⬛','قط أسود','قط'],['حيوانات','🐿️','سنجاب','حيوان'],['حيوانات','🐻','دب','دب'],['حيوانات','🐱','قط','قط'],['حيوانات','🪽','جناح','جناح'],
['شائعة','🌸','زهرة','زهرة'],['شائعة','😉','غمزة','غمزة'],['شائعة','☠️','جمجمة','جمجمة'],['شائعة','✨','لمعان','لمعان'],['شائعة','💫','دوخة ونجوم','نجوم'],['شائعة','🔥','نار','نار،رائع'],['شائعة','💕','قلوب','حب'],['شائعة','👑','تاج','فخامة'],['شائعة','🎵','موسيقى','موسيقى'],['شائعة','🧸','دبدوب','دب'],['شائعة','❤️','قلب أحمر','حب'],['شائعة','🥺','توسل','توسل'],['شائعة','😭','بكاء','بكاء'],['شائعة','🌹','وردة','ورد'],['شائعة','🥳','احتفال','احتفال'],['شائعة','🎥','كاميرا فيديو','تصوير'],['شائعة','💀','جمجمة','جمجمة'],['شائعة','🍒','كرز','فاكهة'],['شائعة','🍀','برسيم','حظ'],['شائعة','🌻','عباد الشمس','زهرة'],['شائعة','❄️','ثلج','شتاء'],['شائعة','😂','ضحك','ضحك'],['شائعة','💞','قلوب','حب'],['شائعة','🩷','قلب وردي','حب'],['شائعة','💜','قلب بنفسجي','حب'],['شائعة','⚠️','تحذير','تحذير'],['شائعة','🔮','كرة سحرية','سحر']
];
let data;
try{data=JSON.parse(localStorage.getItem(KEY))}catch(e){}
if(!Array.isArray(data)||!data.length)data=seed.map((x,i)=>({id:'s'+i,category:x[0],emoji:x[1],label:x[2],keywords:x[3].split(','),phrase:''}));
const $=id=>document.getElementById(id);let activeCategory='الكل';let toastTimer;
const esc=s=>String(s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
function save(){localStorage.setItem(KEY,JSON.stringify(data))}
function toast(s){let t=$('toast');if(!t){t=document.createElement('div');t.id='toast';Object.assign(t.style,{position:'fixed',bottom:'20px',left:'50%',transform:'translateX(-50%)',background:'#222944',border:'1px solid #5eead4',padding:'12px 18px',borderRadius:'12px',zIndex:1000,boxShadow:'0 8px 30px #0007',maxWidth:'90vw'});document.body.appendChild(t)}t.textContent=s;t.style.display='block';clearTimeout(toastTimer);toastTimer=setTimeout(()=>t.style.display='none',2200)}
async function copy(text){try{await navigator.clipboard.writeText(text);toast('تم النسخ بنجاح ✅')}catch(e){const t=document.createElement('textarea');t.value=text;document.body.appendChild(t);t.select();document.execCommand('copy');t.remove();toast('تم النسخ بنجاح ✅')}}
function categories(){const cats=['الكل',...new Set(data.map(x=>x.category||'متنوعة'))];$('categories').innerHTML=cats.map(c=>`<button class="catbtn ${c===activeCategory?'active':''}" data-cat="${esc(c)}">${c==='الكل'?'🌈 ':''}${esc(c)}</button>`).join('');$('categoryOptions').innerHTML=cats.filter(c=>c!=='الكل').map(c=>`<option value="${esc(c)}"></option>`).join('')}
function render(){categories();const q=$('search').value.trim().toLocaleLowerCase();const list=data.filter(x=>(activeCategory==='الكل'||x.category===activeCategory)&&[x.emoji,x.label,x.category,x.phrase,...x.keywords].join(' ').toLocaleLowerCase().includes(q));$('countStat').textContent=`${list.length} شكل`; $('grid').innerHTML=list.length?list.map(x=>`<div class="card" data-copy="${esc(x.id)}" title="اضغط لنسخ الشكل"><span class="mini del" data-del="${esc(x.id)}">حذف</span><span class="face">${esc(x.emoji)}</span><span class="label">${esc(x.label||x.category)}</span></div>`).join(''):'<div class="muted">لا توجد نتائج في هذا القسم. يمكنك إضافة أشكال من قسم الإدارة.</div>'}
document.querySelectorAll('[data-view]').forEach(b=>b.addEventListener('click',()=>{document.querySelectorAll('[data-view]').forEach(x=>x.classList.toggle('active',x===b));['gallery','convert','manage'].forEach(id=>$(id).classList.toggle('hidden',id!==b.dataset.view))}));
$('categories').addEventListener('click',e=>{const b=e.target.closest('[data-cat]');if(b){activeCategory=b.dataset.cat;render()}});
$('grid').addEventListener('click',e=>{const d=e.target.closest('[data-del]');if(d){e.stopPropagation();if(confirm('حذف هذا الشكل من مكتبتك؟')){data=data.filter(x=>x.id!==d.dataset.del);save();render()}return}const c=e.target.closest('[data-copy]');if(c){const x=data.find(y=>y.id===c.dataset.copy);if(x)copy(x.phrase||x.emoji)}});
$('search').addEventListener('input',render);$('copyVisible').addEventListener('click',()=>{const q=$('search').value.trim().toLocaleLowerCase();const list=data.filter(x=>(activeCategory==='الكل'||x.category===activeCategory)&&[x.emoji,x.label,x.category,x.phrase,...x.keywords].join(' ').toLocaleLowerCase().includes(q));copy(list.map(x=>x.phrase||x.emoji).join('\n'))});
$('add').addEventListener('click',()=>{const emoji=$('symbol').value.trim(),label=$('title').value.trim(),category=$('category').value.trim()||'متنوعة',keywords=$('keywords').value.split(/[،,]/).map(x=>x.trim()).filter(Boolean),phrase=$('phrase').value.trim();if(!emoji){$('notice').textContent='اكتب الشكل النصي أولًا.';return}if(!label&&!keywords.length){$('notice').textContent='اكتب اسمًا أو كلمة مرتبطة بالشكل.';return}data.unshift({id:'u'+Date.now()+Math.random().toString(16).slice(2),emoji,label:label||keywords[0],category,keywords,phrase});save();activeCategory=category;render();$('notice').textContent='تمت الإضافة بنجاح ✨';['symbol','title','category','keywords','phrase'].forEach(id=>$(id).value='')});
$('clearForm').addEventListener('click',()=>{['symbol','title','category','keywords','phrase'].forEach(id=>$(id).value='');$('notice').textContent=''});
$('backup').addEventListener('click',()=>{const blob=new Blob([JSON.stringify(data,null,2)],{type:'application/json'}),url=URL.createObjectURL(blob),a=document.createElement('a');a.href=url;a.download='seif-emojis-backup.json';a.click();URL.revokeObjectURL(url);$('notice').textContent='تم تنزيل النسخة الاحتياطية.'});
$('restore').addEventListener('change',async e=>{const f=e.target.files[0];if(!f)return;try{const x=JSON.parse(await f.text());if(!Array.isArray(x)||!x.every(v=>v&&typeof v.emoji==='string'))throw 0;data=x.map((v,i)=>({id:String(v.id||'i'+i+Date.now()),emoji:v.emoji,label:String(v.label||v.emoji),category:String(v.category||'متنوعة'),keywords:Array.isArray(v.keywords)?v.keywords.map(String):[],phrase:String(v.phrase||'')}));save();activeCategory='الكل';render();$('notice').textContent='تم استيراد النسخة بنجاح.'}catch(err){$('notice').textContent='الملف غير صالح.'}e.target.value=''});
function convert(text){let out=text;const pairs=[];data.forEach(x=>x.keywords.forEach(k=>{if(k.trim().length>1)pairs.push({word:k.trim(),emoji:x.emoji})}));pairs.sort((a,b)=>b.word.length-a.word.length);pairs.forEach(p=>{const escaped=p.word.replace(/[.*+?^${}()|[\]\\]/g,'\\$&');try{out=out.replace(new RegExp(escaped,'giu'),p.emoji)}catch(e){}});return out}
$('doConvert').addEventListener('click',()=>{const s=$('textToConvert').value.trim();if(!s){toast('اكتب جملة أولًا');return}$('convertOutput').textContent=convert(s);$('convertBox').classList.remove('hidden')});
$('clearConvert').addEventListener('click',()=>{$('textToConvert').value='';$('convertOutput').textContent='';$('convertBox').classList.add('hidden')});$('copyOutput').addEventListener('click',()=>copy($('convertOutput').textContent));
render();
})();
</script>
</body></html>'''

p = Path("/mnt/data/index.html")
p.write_text(html, encoding="utf-8")
print(f"تم إنشاء الموقع المحدّث: {p} ({p.stat().st_size:,} بايت)")
