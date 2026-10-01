# Tank War

لعبة **Tank War** بنمط 2D Pixel Art، مع الحفاظ على تكوين ساحة المعركة وواجهة HUD والتحكم باللمس، وتطوير اللعبة على مراحل.

## النسخة الحالية
- رسومات Pixel Art أدق للدبابات: مسارات، برج، سبطانة، تفاصيل وإضاءات.
- جدران مبنية من نمط Brick Pixel Art وصناديق واضحة.
- **5 خرائط** مختلفة: Iron Yard، Brick Maze، Fortress، Cross Fire، Steel Citadel.
- **مراحل متدرجة**: عدد الأعداء وقوتهم يزدادان مع التقدم.
- **3 أسلحة**: Machine Gun سريع، Cannon قوي وبطيء، Rocket عالي الضرر مع انفجار أكبر.
- أعداء بثلاث فئات وسرعات/نقاط حياة مختلفة.
- انفجارات Pixel Art متعددة الجسيمات + وميض إطلاق النار.
- Power-ups للأسلحة.
- مؤثرات صوتية مولدة داخل المتصفح/Android WebView باستخدام Web Audio، بدون ملفات صوت خارجية في هذه المرحلة.
- Score / IP / Stage / Enemy HUD.
- WASD والأسهم + أزرار لمس الهاتف.
- Android WebView APK عبر GitHub Actions.

## التحكم
- الحركة: WASD أو الأسهم.
- إطلاق: Space / Enter أو زر FIRE.
- اختيار السلاح: 1 / 2 / 3.
- التقاط Power-up يبدّل السلاح تلقائياً.

## بناء APK للتجريب
افتح **Actions → Tank War Android APK → Run workflow**، وبعد نجاح البناء افتح **Artifacts → tank-war-debug-apk** وحمّل app-debug.apk.

## الخطوة التالية
إضافة ملفات مؤثرات صوتية أكثر واقعية، تحسين sprites إلى إطارات Pixel Art متحركة، إضافة أهداف/أعلام ومراحل Boss، ثم تجهيز Release APK/AAB.