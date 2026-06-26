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
