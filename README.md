# 🐛 Insect Detector
## پایش و تشخیص هوشمند حشرات و آفات با الگوریتم YOLOv26_n

<div align="center">

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Min SDK](https://img.shields.io/badge/Min%20SDK-24%20(Android%207)-blue?style=for-the-badge&logo=android)
![Model](https://img.shields.io/badge/Model-YOLOv26__n-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white)
![Classes](https://img.shields.io/badge/Classes-69%20Species-E91E63?style=for-the-badge)
![Language](https://img.shields.io/badge/Language-Persian-009688?style=for-the-badge)
![License](https://img.shields.io/badge/License-Proprietary-9C27B0?style=for-the-badge)

**اپلیکیشن اندروید تشخیص ۶۹ گونه حشره با استفاده از مدل YOLOv26_n و TensorFlow Lite**  
**طراحی شده برای پژوهشگران، جنگل‌بانان و متخصصان کشاورزی**

[گزارش مشکل](../../issues) · [پیشنهاد ویژگی](../../discussions) · [مستندات](../../wiki)

</div>

---

## 📑 فهرست مطالب

- [معرفی اپلیکیشن](#-معرفی-اپلیکیشن)
- [ویژگی‌های کلیدی](#-ویژگی‌های-کلیدی)
- [تذکرات مهم](#-تذکرات-مهم-برای-کاربران)
- [محل ذخیره‌سازی داده‌ها](#-محل-ذخیره‌سازی-داده‌ها)
- [نصب و راه‌اندازی](#-نصب-و-راه‌اندازی)
- [راهنمای استفاده](#-راهنمای-استفاده)
- [گونه‌های قابل تشخیص](#-گونه‌های-قابل-تشخیص-۶۹-گونه)
- [دسته‌بندی علمی](#-دسته‌بندی-علمی-بر-اساس-خانواده)
- [حشرات خطرناک](#-حشرات-خطرناک)
- [تکنولوژی‌های استفاده‌شده](#️-تکنولوژی‌های-استفاده‌شده)
- [ساختار پروژه](#-ساختار-پروژه)
- [برنامه‌های آینده](#-برنامه‌های-آینده)
- [پشتیبانی و تماس](#-پشتیبانی-و-تماس)
- [توسعه‌دهنده](#-توسعه‌دهنده)

---

## 🎯 معرفی اپلیکیشن

اپلیکیشن **Insect Detector** یک ابزار تخصصی برای **پایش و شناسایی حشرات و آفات** است که با هدف حمایت از پژوهشگران حوزه‌های **کشاورزی، جنگل و مرتع** طراحی و توسعه یافته است. این اپلیکیشن با بهره‌گیری از الگوریتم‌های پیشرفته **هوش مصنوعی (YOLOv26_n)** و فناوری **TensorFlow Lite**، امکان شناسایی سریع و دقیق ۶۹ گونه حشره را به‌صورت **کاملاً آفلاین** روی گوشی موبایل فراهم می‌کند.

### 🎯 اهداف پروژه

- 🌿 تسریع فرآیند پایش آفات در عرصه‌های طبیعی
- 📊 جمع‌آوری داده‌های مکانی‌دار از پراکندگی حشرات
- 🔬 ارائه ابزار دقیق برای پژوهش‌های علمی
- 📱 امکان استفاده در مناطق دورافتاده بدون اینترنت
- 🗂️ مستندسازی خودکار نمونه‌های جمع‌آوری‌شده

---

## 🌟 ویژگی‌های کلیدی

<table>
<tr>
<td width="50%">

### 🎯 قابلیت‌های تشخیص
- ✅ تشخیص **۶۹ گونه حشره** با دقت بالا
- ✅ نمایش **درصد اطمینان** تشخیص
- ✅ **فیلتر هوشمند** تشخیص‌های بالای ۵۰٪
- ✅ اطلاعات کامل هر حشره

</td>
<td width="50%">

### 📱 قابلیت‌های کاربری
- 📷 عکس‌برداری با دوربین گوشی
- 🖼️ انتخاب تصویر از گالری
- 🌐 رابط کاربری **کاملاً فارسی**
- 📴 اجرای **آفلاین** (بدون اینترنت)

</td>
</tr>
<tr>
<td width="50%">

### 📍 قابلیت‌های مکانی
- 📍 ثبت خودکار **مختصات GPS**
- 🗺️ لینک مستقیم به **Google Maps**
- 📊 نمایش موقعیت روی نقشه
- 🎯 ذخیره نقاط نمونه‌برداری

</td>
<td width="50%">

### 💾 قابلیت‌های ذخیره‌سازی
- 📁 ذخیره عکس در **پوشه اختصاصی** هر گونه
- 📊 ثبت اطلاعات در فایل **CSV**
- 🔑 **کلید اصلی یکتا** برای هر رکورد
- 👤 ثبت نام کاربر

</td>
</tr>
</table>

---

## ⚠️ تذکرات مهم برای کاربران

> [!IMPORTANT]
> ### 📸 نکات طلایی عکس‌برداری
> 
> 1. **کیفیت بالا:** لطفاً از حشرات و آفات با **کیفیت عکس بالا** تصویربرداری نمایید.
> 2. **زوایای متعدد:** از هر حشره **چندین عکس از زوایای مختلف** تهیه نمایید.
> 3. **نور مناسب:** در نور کافی عکس بگیرید تا جزئیات بهتر مشخص شود.
> 4. **فاصله مناسب:** حشره را در مرکز کادر و با فاصله مناسب قرار دهید.

> [!NOTE]
> ### 📍 فعال‌سازی GPS
> در هنگام فعالیت در طبیعت، **GPS موبایل را فعال نگه دارید** تا نقاط نمونه‌برداری به‌صورت خودکار ثبت گردد. این اطلاعات برای تحلیل‌های بعدی پراکندگی حشرات بسیار ارزشمند است.

> [!TIP]
> ### 🎯 فیلتر هوشمند
> اپلیکیشن فقط تشخیص‌های با **احتمال بالای ۵۰٪** را ثبت می‌کند؛ به همین دلیل توصیه می‌شود چندین عکس با کیفیت تهیه شود تا بهترین نتیجه حاصل گردد.

> [!WARNING]
> ### 🗑️ مدیریت حافظه
> آدرس ذخیره شدن عکس‌ها در پوشه‌های مربوطه در موبایل در بخش [محل ذخیره‌سازی](#-محل-ذخیره‌سازی-داده‌ها) آمده است. **پس از مدتی نسبت به حذف فایل‌های پوشه cache اقدام نمایید.**
> 
> 🚀 **در ورژن‌های بعدی** و راه‌اندازی **سیستم پایش آفات**، این پوشه‌های ذخیره به **سرور اصلی منتقل شده** و حجم بار اضافی برای ظرفیت حافظه در موبایل شما کاهش می‌یابد.

---

## 📂 محل ذخیره‌سازی داده‌ها

### 📍 مسیر اصلی
```
/sdcard/Android/data/com.example.insectdetector/files/InsectRecords/
```

### 🗂️ ساختار پوشه‌ها
```
InsectRecords/
│
├── 📊 records.csv                       ← فایل اصلی اطلاعات
│
├── 📁 Danaus_plexippus/                 ← پوشه اختصاصی هر گونه
│   ├── Danaus_plexippus_INS_20260626_123456_7890.jpg
│   └── Danaus_plexippus_INS_20260626_145623_1234.jpg
│
├── 📁 Vespa_crabro/
│   └── Vespa_crabro_INS_20260626_150000_5678.jpg
│
└── 📁 Nezara_viridula/
    └── ...
```

### 📊 فرمت فایل CSV
```csv
ID,ClassName,Confidence,Latitude,Longitude,DateTime,UserName,ImagePath
INS_20260626_123456_7890,"Danaus plexippus",85.50,32.325674,51.654321,"2026-06-26 12:34:56","تورج مختارپور","/path/to/image.jpg"
```

### 🔑 ساختار کلید اصلی
هر رکورد دارای یک کلید اصلی یکتا به فرمت زیر است:
```
INS_YYYYMMDD_HHMMSS_XXXX
```
**مثال:** `INS_20260626_123456_7890`

این کلید برای ارتباط بین فایل CSV و تصویر ذخیره‌شده استفاده می‌شود.

---

## 📥 نصب و راه‌اندازی

### روش 1: دانلود APK (ساده‌ترین)
1. به بخش [Releases](../../releases) مراجعه کنید
2. آخرین نسخه APK را دانلود کنید
3. فایل را روی موبایل نصب کنید
4. مجوزهای لازم (دوربین، GPS، ذخیره‌سازی) را اعطا کنید

### روش 2: ساخت از سورس کد
```bash
# کلون کردن مخزن
git clone https://github.com/tourajmokhtarpour/App_Insects.git
cd App_Insects

# ساخت APK
./gradlew assembleDebug

# مسیر فایل خروجی
# app/build/outputs/apk/debug/app-debug.apk
```

### روش 3: نصب با ADB
```bash
# اتصال موبایل و نصب
adb install app/build/outputs/apk/debug/app-debug.apk

# اعطای مجوزها
adb shell pm grant com.example.insectdetector android.permission.CAMERA
adb shell pm grant com.example.insectdetector android.permission.ACCESS_FINE_LOCATION
adb shell pm grant com.example.insectdetector android.permission.ACCESS_COARSE_LOCATION
```

### 📋 پیش‌نیازها
- **اندروید 7.0 (API 24)** یا بالاتر
- حداقل **200MB فضای خالی**
- دسترسی به **دوربین** و **GPS**

---

## 📖 راهنمای استفاده

### 🚀 شروع کار
1. اپلیکیشن را باز کنید
2. در اولین اجرا، **نام خود** را وارد کنید
3. مجوزهای لازم را اعطا کنید

### 📷 عکس‌برداری از حشره
1. روی دکمه **"📷 باز کردن دوربین"** کلیک کنید
2. GPS به‌صورت خودکار فعال می‌شود
3. حشره را در مرکز کادر قرار دهید
4. دکمه عکس را بزنید
5. نتیجه تشخیص نمایش داده می‌شود

### 🖼️ انتخاب از گالری
1. روی دکمه **"🖼️ انتخاب از گالری"** کلیک کنید
2. تصویر مورد نظر را انتخاب کنید
3. نتیجه تشخیص نمایش داده می‌شود

### 📊 مشاهده نتایج
- **نام فارسی و علمی** حشره
- **درصد اطمینان** تشخیص
- **خانواده** و **زیستگاه**
- **موقعیت GPS** با لینک به Google Maps
- **کلید اصلی** ثبت

---

## 🎯 گونه‌های قابل تشخیص (۶۹ گونه)

<table>
<thead>
<tr>
<th>#</th>
<th>نام علمی</th>
<th>نام فارسی</th>
<th>خطرناک</th>
</tr>
</thead>
<tbody>
<tr><td>0</td><td>Acherontia atropos</td><td>پروانه مرگ</td><td>❌</td></tr>
<tr><td>1</td><td>Acherontia atropos(Larve)</td><td>لارو پروانه مرگ</td><td>❌</td></tr>
<tr><td>2</td><td>Acrosternum millierei</td><td>سن سبز مدیترانه‌ای</td><td>⚠️</td></tr>
<tr><td>3</td><td>Agrilus hastulifer</td><td>سوسک شاخدار باریک</td><td>⚠️</td></tr>
<tr><td>4</td><td>Anarsia lineatella</td><td>بید شاخه‌خوار هلو</td><td>⚠️</td></tr>
<tr><td>5</td><td>Anoplophora chinensis</td><td>سوسک شاخدار بلند آسیایی</td><td>⚠️</td></tr>
<tr><td>6</td><td>Apantesis vittata</td><td>شب‌پره راه‌راه</td><td>❌</td></tr>
<tr><td>7</td><td>Arctia caja(Adult)</td><td>شب‌پره خرس بزرگ (بالغ)</td><td>❌</td></tr>
<tr><td>8</td><td>Arctia caja(Larve)</td><td>شب‌پره خرس بزرگ (لارو)</td><td>❌</td></tr>
<tr><td>9</td><td>Argema mittrei</td><td>پروانه ابریشمی ماداگاسکار</td><td>❌</td></tr>
<tr><td>10</td><td>Argema mittrei(Larve)</td><td>لارو پروانه ابریشمی ماداگاسکار</td><td>❌</td></tr>
<tr><td>11</td><td>Attacus atlas</td><td>پروانه اطلس</td><td>❌</td></tr>
<tr><td>12</td><td>Cabera variolaria</td><td>شب‌پره هندسی</td><td>❌</td></tr>
<tr><td>13</td><td>Cerambyx cerdo</td><td>سوسک شاخدار بزرگ بلوط</td><td>❌</td></tr>
<tr><td>14</td><td>Cerroneuroterus lanuginosus</td><td>زنبور پشمی</td><td>❌</td></tr>
<tr><td>15</td><td>Cryptolaemus montrouzieri</td><td>کفشدوزک شکارگر</td><td>❌</td></tr>
<tr><td>16</td><td>Curculio glandium</td><td>خرطومی بلوط</td><td>❌</td></tr>
<tr><td>17</td><td>Cydia latiferreana</td><td>بید میوه بلوط</td><td>❌</td></tr>
<tr><td>18</td><td>Cydia pomonella</td><td>کرم سیب</td><td>⚠️</td></tr>
<tr><td>19</td><td>Danaus plexippus</td><td>پروانه مونارک</td><td>❌</td></tr>
<tr><td>20</td><td>Deilephila elpenor</td><td>شب‌پره فیل صورتی</td><td>❌</td></tr>
<tr><td>21</td><td>Dicranura ulmi</td><td>شب‌پره دمدار نارون</td><td>❌</td></tr>
<tr><td>22</td><td>Dicycla oo</td><td>شب‌پره حرف یونانی</td><td>❌</td></tr>
<tr><td>23</td><td>Dinoptera collaris</td><td>سوسک شاخدار کوچک</td><td>❌</td></tr>
<tr><td>24</td><td>Diprion pini</td><td>زنبور اره‌ای کاج</td><td>⚠️</td></tr>
<tr><td>25</td><td>Epicometis hirta</td><td>سوسک گل‌خوار</td><td>⚠️</td></tr>
<tr><td>26</td><td>Euproctis chrysorrhoea</td><td>شب‌پره قهوه‌ای دم‌طلایی</td><td>⚠️</td></tr>
<tr><td>27</td><td>Gypsonoma aceriana</td><td>بید جوانه‌خوار افرا</td><td>❌</td></tr>
<tr><td>28</td><td>Harpyia milhauseri</td><td>شب‌پره هارپی</td><td>❌</td></tr>
<tr><td>29</td><td>Hesperophanes sericeus</td><td>سوسک شاخدار ابریشمی</td><td>❌</td></tr>
<tr><td>30</td><td>Hyles lineata</td><td>شب‌پره خط‌دار</td><td>❌</td></tr>
<tr><td>31</td><td>Hylesinus varius</td><td>سوسک پوست‌خوار زبان‌گنجشک</td><td>⚠️</td></tr>
<tr><td>32</td><td>Lachnus roboris</td><td>شته بلوط</td><td>❌</td></tr>
<tr><td>33</td><td>Lampetis mimosa</td><td>سوسک جواهری</td><td>❌</td></tr>
<tr><td>34</td><td>Lyctus brunneus</td><td>سوسک پودرچوب</td><td>⚠️</td></tr>
<tr><td>35</td><td>Macroglossum stellatarum</td><td>شب‌پره بال‌شفاف</td><td>❌</td></tr>
<tr><td>36</td><td>Metamasius hemipterus</td><td>خرطومی نیشکر</td><td>⚠️</td></tr>
<tr><td>37</td><td>Nezara viridula</td><td>سن سبز</td><td>⚠️</td></tr>
<tr><td>38</td><td>Nycteola asiatica</td><td>شب‌پره آسیایی</td><td>❌</td></tr>
<tr><td>39</td><td>Opodiphthera astrophela</td><td>پروانه ابریشمی استرالیایی</td><td>❌</td></tr>
<tr><td>40</td><td>Opodiphthera eucalypti</td><td>پروانه ابریشمی اکالیپتوس</td><td>❌</td></tr>
<tr><td>41</td><td>Osphranteria coerulescens</td><td>سوسک چوب آبی</td><td>❌</td></tr>
<tr><td>42</td><td>Otiorhynchus sulcatus</td><td>خرطومی شیاردار</td><td>⚠️</td></tr>
<tr><td>43</td><td>Palpita unionalis</td><td>بید یاس</td><td>⚠️</td></tr>
<tr><td>44</td><td>Papilio glaucus</td><td>پروانه دم‌چلچله‌ای زرد</td><td>❌</td></tr>
<tr><td>45</td><td>Platypus cylindrus</td><td>سوسک پلاتیپوس</td><td>⚠️</td></tr>
<tr><td>46</td><td>Psalmocharias alhageos</td><td>زنجره خرخر</td><td>❌</td></tr>
<tr><td>47</td><td>Rhagoletis pomonella</td><td>مگس سیب</td><td>⚠️</td></tr>
<tr><td>48</td><td>Saturnia pavonia</td><td>پروانه ابریشمی کوچک</td><td>❌</td></tr>
<tr><td>49</td><td>Schinia arcigera</td><td>شب‌پره گل‌خوار</td><td>❌</td></tr>
<tr><td>50</td><td>Sirex noctilio</td><td>زنبور چوب‌خوار کاج</td><td>⚠️</td></tr>
<tr><td>51</td><td>Smerinthus ocellata</td><td>شب‌پره چشم‌دار</td><td>❌</td></tr>
<tr><td>52</td><td>Sphrageidus similis</td><td>شب‌پره شبیه</td><td>❌</td></tr>
<tr><td>53</td><td>Spodoptera exigua</td><td>کرم برگ‌خوار چغندر</td><td>⚠️</td></tr>
<tr><td>54</td><td>Spoladea recurvalis</td><td>بید برگ‌خوار</td><td>⚠️</td></tr>
<tr><td>55</td><td>Stromatium auratum</td><td>سوسک شاخدار طلایی</td><td>❌</td></tr>
<tr><td>56</td><td>Synanthedon pyri</td><td>بید شاخه‌خوار گلابی</td><td>⚠️</td></tr>
<tr><td>57</td><td>Tabanus atratus</td><td>مگس اسب سیاه</td><td>⚠️</td></tr>
<tr><td>58</td><td>Tortrix viridana</td><td>بید سبز بلوط</td><td>❌</td></tr>
<tr><td>59</td><td>Tyria jacobaeae</td><td>شب‌پره کرمی جاکوبیا</td><td>❌</td></tr>
<tr><td>60</td><td>Tyria jacobaeae(Adult)</td><td>شب‌پره کرمی جاکوبیا (بالغ)</td><td>❌</td></tr>
<tr><td>61</td><td>Vanessa atalanta</td><td>پروانه آتالانتا</td><td>❌</td></tr>
<tr><td>62</td><td>Vespa crabro</td><td>زنبور سرخ اروپایی</td><td>⚠️</td></tr>
<tr><td>63</td><td>Vespula maculifrons</td><td>زنبور زرد پیشانی‌خال‌دار</td><td>⚠️</td></tr>
<tr><td>64</td><td>Xanthogaleruca luteola</td><td>سوسک برگ‌خوار نارون</td><td>⚠️</td></tr>
<tr><td>65</td><td>Xylocopa valga</td><td>زنبور نجار</td><td>❌</td></tr>
<tr><td>66</td><td>Yponomeuta padella</td><td>بید تارتن ارغوانی</td><td>⚠️</td></tr>
<tr><td>67</td><td>Yponomeuta padella(Larve)</td><td>لارو بید تارتن ارغوانی</td><td>⚠️</td></tr>
<tr><td>68</td><td>Zeuzera pyrina</td><td>بید چوب‌خوار</td><td>⚠️</td></tr>
</tbody>
</table>

---

## 🧬 دسته‌بندی علمی بر اساس خانواده

### 🦋 راسته پروانه‌سانان (Lepidoptera) - ۳۵ گونه (۵۰.۷٪)

| خانواده | نام فارسی | کلاس‌ها |
|---------|-----------|---------|
| **Sphingidae** | شب‌پره‌های اسپفنگس | 0, 1, 20, 30, 35, 51 |
| **Saturniidae** | پروانه‌های ابریشمی | 9, 10, 11, 39, 40, 48 |
| **Nymphalidae** | فرچه‌پایان | 19, 61 |
| **Papilionidae** | پروانه‌های دم‌چلچله‌ای | 44 |
| **Erebidae** | خرس‌ها و ارکتیینا | 6, 7, 8, 26, 59, 60 |
| **Noctuidae** | شب‌پره‌های جغد | 22, 49, 53 |
| **Geometridae** | شب‌پره‌های هندسی | 12 |
| **Tortricidae** | بیدهای پیچ‌پیچ | 17, 18, 27, 58 |
| **Gelechiidae** | بیدهای کوچک | 4 |
| **Notodontidae** | شب‌پره‌های دندان‌دار | 21, 28 |
| **Nolidae** | بیدهای نو | 38 |
| **Crambidae** | علف‌بیدان | 43, 54 |
| **Sesiidae** | بیدهای شیشه‌بال | 56 |
| **Yponomeutidae** | بیدهای ارمنی | 66, 67 |
| **Cossidae** | بیدهای چوب‌خوار | 68 |
| **Lymantriidae** | شب‌پره‌های کاکلی | 52 |

### 🪲 راسته سوسک‌سانان (Coleoptera) - ۱۸ گونه (۲۶.۱٪)

| خانواده | نام فارسی | کلاس‌ها |
|---------|-----------|---------|
| **Cerambycidae** | سوسک‌های شاخدار | 5, 13, 23, 29, 41, 55 |
| **Curculionidae** | سوسک‌های خرطومی | 16, 31, 36, 42, 45 |
| **Buprestidae** | سوسک‌های جواهری | 3, 33 |
| **Scarabaeidae** | سوسک‌های اسکاراب | 25 |
| **Coccinellidae** | کفشدوزک‌ها | 15 |
| **Chrysomelidae** | سوسک‌های برگ‌خوار | 64 |
| **Bostrichidae** | سوسک‌های پودرچوب | 34 |

### 🐝 راسته پرده‌بالان (Hymenoptera) - ۵ گونه (۷.۲٪)

| خانواده | نام فارسی | کلاس‌ها |
|---------|-----------|---------|
| **Vespidae** | زنبورهای واقعی | 62, 63 |
| **Apidae** | زنبورهای عسل | 65 |
| **Siricidae** | زنبورهای چوب | 50 |
| **Diprionidae** | زنبورهای اره‌ای | 24 |
| **Tenthredinidae** | زنبورهای اره‌ای برگ‌خوار | 14 |

### 🪰 راسته دوبالان (Diptera) - ۲ گونه (۲.۹٪)

| خانواده | نام فارسی | کلاس‌ها |
|---------|-----------|---------|
| **Tabanidae** | مگس‌های اسب | 57 |
| **Tephritidae** | مگس‌های میوه | 47 |

### 🐛 راسته نیم‌بالان (Hemiptera) - ۳ گونه (۴.۳٪)

| خانواده | نام فارسی | کلاس‌ها |
|---------|-----------|---------|
| **Pentatomidae** | سن‌های سپردار | 2, 37 |
| **Aphididae** | شته‌ها | 32 |
| **Cicadidae** | زنجره‌ها | 46 |

### 🦗 سایر - ۶ گونه (۸.۷٪)

---

## 📊 آمار کلی

<div align="center">

| دسته | تعداد | درصد |
|------|:-----:|:-----:|
| 🦋 پروانه‌ها و شب‌پره‌ها | 35 | 50.7% |
| 🪲 سوسک‌ها | 18 | 26.1% |
| 🐝 زنبورها | 5 | 7.2% |
| 🪰 مگس‌ها | 2 | 2.9% |
| 🐛 سن‌ها و شته‌ها | 3 | 4.3% |
| 🦗 سایر | 6 | 8.7% |
| **مجموع** | **69** | **100%** |

</div>

---

## ⚠️ حشرات خطرناک

> [!CAUTION]
> **۲۷ گونه** از ۶۹ گونه قابل تشخیص، به‌عنوان **خطرناک یا آفت** طبقه‌بندی شده‌اند:

### 🐛 آفات مهم کشاورزی و جنگل
- **Acrosternum millierei** - سن سبز مدیترانه‌ای
- **Anarsia lineatella** - بید شاخه‌خوار هلو
- **Anoplophora chinensis** - سوسک شاخدار بلند آسیایی (قرنطینه‌ای)
- **Cydia pomonella** - کرم سیب
- **Diprion pini** - زنبور اره‌ای کاج
- **Euproctis chrysorrhoea** - شب‌پره قهوه‌ای دم‌طلایی (حساسیت‌زا)
- **Lyctus brunneus** - سوسک پودرچوب
- **Nezara viridula** - سن سبز
- **Otiorhynchus sulcatus** - خرطومی شیاردار
- **Palpita unionalis** - بید یاس
- **Platypus cylindrus** - سوسک پلاتیپوس
- **Rhagoletis pomonella** - مگس سیب
- **Sirex noctilio** - زنبور چوب‌خوار کاج
- **Spodoptera exigua** - کرم برگ‌خوار چغندر
- **Synanthedon pyri** - بید شاخه‌خوار گلابی
- **Xanthogaleruca luteola** - سوسک برگ‌خوار نارون
- **Zeuzera pyrina** - بید چوب‌خوار

### 🐝 حشرات نیش‌زن
- **Vespa crabro** - زنبور سرخ اروپایی
- **Vespula maculifrons** - زنبور زرد پیشانی‌خال‌دار
- **Tabanus atratus** - مگس اسب سیاه

### 🔬 سایر گونه‌های مهم
- **Agrilus hastulifer** - سوسک شاخدار باریک
- **Epicometis hirta** - سوسک گل‌خوار
- **Hylesinus varius** - سوسک پوست‌خوار زبان‌گنجشک
- **Metamasius hemipterus** - خرطومی نیشکر
- **Spoladea recurvalis** - بید برگ‌خوار
- **Yponomeuta padella** - بید تارتن ارغوانی
- **Yponomeuta padella(Larve)** - لارو بید تارتن ارغوانی

---

## 🛠️ تکنولوژی‌های استفاده‌شده

<table>
<thead>
<tr>
<th>تکنولوژی</th>
<th>نسخه</th>
<th>کاربرد</th>
</tr>
</thead>
<tbody>
<tr><td><b>Kotlin</b></td><td>1.9.22</td><td>زبان برنامه‌نویسی اصلی</td></tr>
<tr><td><b>YOLOv26_n</b></td><td>Latest</td><td>مدل تشخیص شیء</td></tr>
<tr><td><b>TensorFlow Lite</b></td><td>2.14.0</td><td>اجرای مدل روی موبایل</td></tr>
<tr><td><b>CameraX</b></td><td>1.3.1</td><td>مدیریت دوربین</td></tr>
<tr><td><b>Google Play Services</b></td><td>21.0.1</td><td>دریافت موقعیت GPS</td></tr>
<tr><td><b>Material Design</b></td><td>1.11.0</td><td>رابط کاربری مدرن</td></tr>
<tr><td><b>CSV</b></td><td>-</td><td>ذخیره‌سازی داده‌ها</td></tr>
</tbody>
</table>

---

## 📁 ساختار پروژه

```
App_Insects/
│
├── 📱 app/
│   └── src/main/
│       ├── java/com/example/insectdetector/
│       │   ├── MainActivity.kt              ← صفحه اصلی
│       │   ├── CameraActivity.kt            ← فعالیت دوربین
│       │   ├── ResultActivity.kt            ← نمایش نتایج
│       │   │
│       │   ├── detector/
│       │   │   ├── YOLODetector.kt          ← هسته تشخیص
│       │   │   ├── DetectionResult.kt       ← مدل داده تشخیص
│       │   │   └── InsectInfo.kt            ← اطلاعات حشره
│       │   │
│       │   ├── data/
│       │   │   └── InsectDatabase.kt        ← پایگاه داده حشرات
│       │   │
│       │   └── utils/
│       │       ├── Constants.kt             ← ثابت‌های برنامه
│       │       ├── ImageUtils.kt            ← ابزارهای تصویر
│       │       ├── LocationHelper.kt        ← مدیریت GPS
│       │       └── DataRecorder.kt          ← ثبت داده‌ها
│       │
│       ├── res/
│       │   ├── layout/                      ← فایل‌های XML رابط کاربری
│       │   ├── drawable/                    ← آیکون‌ها و تصاویر
│       │   └── values/                      ← رشته‌ها و رنگ‌ها
│       │
│       ├── assets/
│       │   └── best_float16.tflite          ← مدل YOLO
│       │
│       └── AndroidManifest.xml
│
├── .github/
│   └── workflows/
│       └── android.yml                      ← CI/CD GitHub Actions
│
├── build.gradle.kts                         ← پیکربندی Gradle
├── settings.gradle.kts                      ← تنظیمات پروژه
├── gradle.properties                        ← ویژگی‌های Gradle
├── README.md                                ← مستندات پروژه
└── .gitignore
```

---

## 🔮 برنامه‌های آینده

### 🚀 نسخه 2.0
- 🌐 **اتصال به سرور مرکزی** برای انتقال خودکار داده‌ها
- 📊 **داشبورد پایش آفات** تحت وب برای تحلیل داده‌ها
- 🗺️ **نقشه پراکندگی** حشرات بر اساس GPS
- 📈 **آمار و نمودارهای زمانی** تشخیص‌ها
- 🔔 **هشدار طغیان آفات** بر اساس داده‌های جمع‌آوری‌شده

### 🔬 نسخه 3.0
- 🔍 **افزایش تعداد گونه‌های قابل تشخیص** به بیش از 200 گونه
- 🌿 **افزودن اطلاعات گیاهان میزبان** و علائم آسیب
- 💊 **پیشنهاد روش‌های مبارزه** بر اساس گونه تشخیص داده شده
- 📱 **نسخه iOS** اپلیکیشن
- 🌍 **پشتیبانی چندزبانه** (انگلیسی)

### 🤝 همکاری
- 🎓 **همکاری با دانشگاه‌ها** برای بهبود مدل
- 🏛️ **یکپارچه‌سازی با سامانه‌های سازمان جنگل‌ها**
- 📡 **استفاده از اینترنت اشیاء (IoT)** برای پایش خودکار

---

## 📤 انتقال داده‌ها به کامپیوتر

### با استفاده از ADB
```bash
# کپی کل پوشه داده‌ها
adb pull /sdcard/Android/data/com.example.insectdetector/files/InsectRecords/ C:\InsectRecords\

# کپی فقط فایل CSV
adb pull /sdcard/Android/data/com.example.insectdetector/files/InsectRecords/records.csv C:\

# مشاهده محتوای CSV
adb shell cat /sdcard/Android/data/com.example.insectdetector/files/InsectRecords/records.csv
```

### دستورات مفید
```bash
# شمارش تعداد تصاویر
adb shell find /sdcard/Android/data/com.example.insectdetector/files/InsectRecords/ -name "*.jpg" | wc -l

# مشاهده حجم پوشه
adb shell du -sh /sdcard/Android/data/com.example.insectdetector/files/InsectRecords/

# مشاهده 10 رکورد آخر CSV
adb shell tail -n 10 /sdcard/Android/data/com.example.insectdetector/files/InsectRecords/records.csv
```

---

## 🤝 مشارکت

ما از پیشنهادات و همکاری شما استقبال می‌کنیم!

### 🐛 گزارش مشکل
اگر با مشکلی مواجه شدید، لطفاً در بخش [Issues](../../issues) گزارش دهید.

### 💡 پیشنهاد ویژگی
برای پیشنهاد ویژگی جدید، از بخش [Discussions](../../discussions) استفاده کنید.

### 🔀 Pull Request
برای مشارکت در توسعه:
1. Fork از مخزن
2. ایجاد Branch جدید (`git checkout -b feature/AmazingFeature`)
3. Commit تغییرات (`git commit -m 'Add some AmazingFeature'`)
4. Push به Branch (`git push origin feature/AmazingFeature`)
5. باز کردن Pull Request

---

## 📄 لایسنس

این پروژه تحت لایسنس اختصاصی **مرکز تحقیقات و آموزش کشاورزی و منابع طبیعی استان چهارمحال و بختیاری** منتشر شده است.

استفاده تجاری از این اپلیکیشن بدون اجازه کتبی ممنوع است.

---

## 📞 پشتیبانی و تماس

برای ارتباط با تیم توسعه:

| روش | آدرس |
|-----|------|
📧 **ایمیل:** `mokhtarpour.touraj@gmail.com`  

> [!NOTE]
> برای سوالات علمی و پژوهشی، لطفاً مستقیماً با توسعه‌دهنده تماس بگیرید.

---

<div align="center">

## 👨‍🔬 توسعه‌دهنده

<table>
<tr>
<td width="200">
<div align="center">
<img src="https://github.com/tourajmokhtarpour.png" width="150" style="border-radius: 50%" alt="Touraj Mokhtarpour"/>
</div>
</td>
<td>

### **تورج مختارپور**
**پژوهشگر**

🏛️ **بخش تحقیقات جنگل و مرتع**  
🌾 **مرکز تحقیقات و آموزش کشاورزی و منابع طبیعی استان چهارمحال و بختیاری**  
📧 **ایمیل:** `mokhtarpour.touraj@gmail.com`  
🌐 **GitHub:** [@tourajmokhtarpour](https://github.com/tourajmokhtarpour)

</td>
</tr>
</table>

---

### 🙏 تقدیر و تشکر

با تشکر از کلیه همکاران پژوهشی و جنگل‌بانان عزیز که در توسعه و آزمایش این اپلیکیشن مشارکت داشتند.

---

⭐ **اگر این پروژه برایتان مفید بود، یک ستاره بدهید!** ⭐

🐛 **Insect Detector** © 2026 - مرکز تحقیقات و آموزش کشاورزی و منابع طبیعی استان چهارمحال و بختیاری

</div>
