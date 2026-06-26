package com.example.insectdetector.data

import com.example.insectdetector.detector.InsectInfo

object InsectDatabase {
    fun getInfo(className: String): InsectInfo {
        return when (className) {
            "Acherontia atropos" -> InsectInfo(
                name = "پروانه مرگ",
                scientificName = "Acherontia atropos",
                family = "Sphingidae",
                description = "پروانه‌ای بزرگ با طرح جمجمه روی قفسه سینه",
                habitat = "مناطق گرمسیری و نیمه‌گرمسیری",
                diet = "شهد گل‌ها و عسل",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "به دلیل صدای جیغ‌مانند و طرح جمجمه معروف است"
            )
            
            "Acherontia atropos(Larve)" -> InsectInfo(
                name = "لارو پروانه مرگ",
                scientificName = "Acherontia atropos",
                family = "Sphingidae",
                description = "لارو بزرگ با رنگ‌های زرد و آبی و شاخ دم‌دار",
                habitat = "روی گیاهان خانواده بادنجانیان",
                diet = "برگ‌های سیب‌زمینی، گوجه‌فرنگی",
                lifecycle = "مرحله لاروی 3-4 هفته",
                isDangerous = false,
                interestingFacts = "می‌تواند با حرکت دادن سر صدای کلیک ایجاد کند"
            )
            
            "Danaus plexippus" -> InsectInfo(
                name = "پروانه مونارک",
                scientificName = "Danaus plexippus",
                family = "Nymphalidae",
                description = "پروانه‌ای نارنجی با رگه‌های سیاه",
                habitat = "آمریکای شمالی و مرکزی",
                diet = "شهد گل‌ها، برگ‌های آسکلپیاس",
                lifecycle = "مهاجرت طولانی‌مسافت",
                isDangerous = false,
                interestingFacts = "مسافت مهاجرت تا 4000 کیلومتر"
            )
            
            "Vespa crabro" -> InsectInfo(
                name = "زنبور سرخ",
                scientificName = "Vespa crabro",
                family = "Vespidae",
                description = "بزرگ‌ترین زنبور اجتماعی اروپا",
                habitat = "جنگل‌ها و مناطق شهری",
                diet = "حشرات دیگر، میوه‌های رسیده",
                lifecycle = "کلونی فصلی",
                isDangerous = true,
                interestingFacts = "نیش آن دردناک است اما معمولاً تهاجمی نیست"
            )
            
            "Nezara viridula" -> InsectInfo(
                name = "سن سبز",
                scientificName = "Nezara viridula",
                family = "Pentatomidae",
                description = "حشره‌ای سبز رنگ با شکل سپر",
                habitat = "مزارع و باغ‌ها",
                diet = "شیره گیاهان",
                lifecycle = "تخم، پوره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم کشاورزی است و بوی بدی دارد"
            )
            
            else -> InsectInfo(
                name = className,
                scientificName = className,
                family = "نامشخص",
                description = "اطلاعات بیشتری موجود نیست",
                habitat = "-",
                diet = "-",
                lifecycle = "-",
                isDangerous = false,
                interestingFacts = "-"
            )
        }
    }
}