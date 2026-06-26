# 🐛 Insect Detector - تشخیص حشرات با هوش مصنوعی

<div align="center">

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Min SDK](https://img.shields.io/badge/Min%20SDK-24-blue)
![Model](https://img.shields.io/badge/Model-YOLOv8-orange)
![Classes](https://img.shields.io/badge/Classes-67-red)

**اپلیکیشن اندروید تشخیص ۶۷ گونه حشره با استفاده از مدل YOLOv8 و TensorFlow Lite**

</div>

## 📱 ویژگی‌ها

✅ تشخیص ۶۷ گونه حشره با دقت بالا  
✅ استفاده از دوربین گوشی  
✅ انتخاب تصویر از گالری  
✅ نمایش درصد اطمینان تشخیص  
✅ اطلاعات کامل هر حشره (نام علمی، خانواده، زیستگاه، تغذیه)  
✅ هشدار برای حشرات خطرناک  
✅ رابط کاربری فارسی  
✅ اجرای آفلاین (بدون نیاز به اینترنت)  

## 🎯 گونه‌های قابل تشخیص

- Acherontia atropos (پروانه مرگ)
- Danaus plexippus (پروانه مونارک)
- Vespa crabro (زنبور سرخ)
- Nezara viridula (سن سبز)
- و ۶۳ گونه دیگر...

## 🛠️ تکنولوژی‌های استفاده‌شده

| تکنولوژی | کاربرد |
|----------|--------|
| Kotlin | زبان برنامه‌نویسی |
| YOLOv8 | مدل تشخیص شیء |
| TensorFlow Lite | اجرای مدل روی موبایل |
| CameraX | مدیریت دوربین |
| Material Design | رابط کاربری |

## 📥 دانلود

### روش 1: دانلود APK
از بخش [Releases](../../releases) آخرین نسخه APK را دانلود کنید.

### روش 2: ساخت از سورس
```bash
git clone https://github.com/USERNAME/InsectDetector.git
cd InsectDetector
./gradlew assembleDebug