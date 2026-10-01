# Tank War

لعبة Tank War بنمط 2D Pixel Art مستوحاة من تكوين المرجع المرفق، مع تطوير تدريجي للرسومات واللعب.

## النسخة الحالية
- Pixel Art محسّن للدبابات والمباني والجدران.
- حركة الدبابة وإطلاق النار.
- أعداء يتحركون ويطلقون النار.
- اصطدامات، نقاط، حياة، مستويات.
- مؤثرات إطلاق وانفجار Pixel Art.
- تحكم WASD والأسهم.
- أزرار لمس للهاتف.
- Android WebView APK جاهز للبناء عبر GitHub Actions.

## بناء APK للتجريب
افتح تبويب Actions في GitHub ثم:
1. اختر Tank War Android APK.
2. اضغط Run workflow.
3. انتظر حتى يصبح البناء Success.
4. افتح workflow run ثم قسم Artifacts.
5. حمّل tank-war-debug-apk واستخرج app-debug.apk وثبته على الهاتف.

GitHub Actions يحفظ ملفات البناء كـ artifacts بعد انتهاء الـ workflow.

## المرحلة التالية
تحسين الـ sprites إلى Pixel Art أكثر تفصيلاً، خرائط متعددة، انفجارات متحركة، مؤثرات صوتية، أهداف/علم، Power-ups، شاشة البداية وGame Over، ثم تجهيز Release APK/AAB.
