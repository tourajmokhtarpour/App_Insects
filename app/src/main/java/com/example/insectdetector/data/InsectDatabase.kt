package com.example.insectdetector.data

import com.example.insectdetector.detector.InsectInfo

object InsectDatabase {
    fun getInfo(className: String): InsectInfo {
        return when (className) {
            "Acherontia atropos" -> InsectInfo(
                name = "پروانه مرگ",
                scientificName = "Acherontia atropos",
                family = "Sphingidae (شب‌پره‌های اسپفنگس)",
                description = "بزرگ‌ترین شب‌پره اروپا با طرح جمجمه انسان روی پشت سینه",
                habitat = "اروپا، آفریقا، خاورمیانه",
                diet = "شهد گل‌ها، عسل زنبورها",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "می‌تواند با ارتعاش بدن صدایی شبیه جیغ تولید کند و به کندوی زنبور حمله کند"
            )
            
            "Acherontia atropos(Larve)" -> InsectInfo(
                name = "لارو پروانه مرگ",
                scientificName = "Acherontia atropos",
                family = "Sphingidae (شب‌پره‌های اسپفنگس)",
                description = "لارو بزرگ سبز یا زرد با خطوط آبی و شاخ دم‌دار",
                habitat = "روی گیاهان خانواده Solanaceae (بادنجانیان)",
                diet = "برگ سیب‌زمینی، گوجه‌فرنگی، تنباکو",
                lifecycle = "مرحله لاروی 3-4 هفته",
                isDangerous = false,
                interestingFacts = "با حرکت دادن آرواره‌ها صدای کلیک تولید می‌کند"
            )
            
            "Acrosternum millierei" -> InsectInfo(
                name = "سن سبز مدیترانه‌ای",
                scientificName = "Acrosternum millierei",
                family = "Pentatomidae (سن‌های سپردار)",
                description = "سنی سبز رنگ با شکل سپری و اندازه متوسط",
                habitat = "مناطق مدیترانه‌ای، باغ‌ها و مزارع",
                diet = "شیره گیاهان، میوه‌ها",
                lifecycle = "تخم، پوره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم درختان میوه در مناطق گرمسیری"
            )
            
            "Agrilus hastulifer" -> InsectInfo(
                name = "سوسک شاخدار باریک",
                scientificName = "Agrilus hastulifer",
                family = "Buprestidae (سوسک‌های جواهری)",
                description = "سوسک کوچک فلزی با رنگ سبز-مسی",
                habitat = "جنگل‌های برگ‌ریز",
                diet = "لارو از چوب درختان بلوط تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "لاروها در چوب درخت تونل حفر می‌کنند"
            )
            
            "Anarsia lineatella" -> InsectInfo(
                name = "بید شاخه‌خوار هلو",
                scientificName = "Anarsia lineatella",
                family = "Gelechiidae",
                description = "بید کوچک با بال‌های خاکستری و خطوط تیره",
                habitat = "باغ‌های میوه، به ویژه هلو",
                diet = "لارو از جوانه‌ها و میوه‌ها تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم درختان هسته‌دار در سراسر جهان"
            )
            
            "Anoplophora chinensis" -> InsectInfo(
                name = "سوسک شاخدار بلند آسیایی",
                scientificName = "Anoplophora chinensis",
                family = "Cerambycidae (سوسک‌های شاخدار)",
                description = "سوسک بزرگ سیاه با لکه‌های سفید و شاخک‌های بلند",
                habitat = "آسیای شرقی، اروپا (گونه مهاجم)",
                diet = "لارو از چوب درختان پهن‌برگ تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "یکی از خطرناک‌ترین آفات چوب در جهان، گونه قرنطینه‌ای"
            )
            
            "Apantesis vittata" -> InsectInfo(
                name = "شب‌پره راه‌راه",
                scientificName = "Apantesis vittata",
                family = "Erebidae (آرکتیینا)",
                description = "شب‌پره متوسط با بال‌های زرد و خطوط سیاه",
                habitat = "آمریکای شمالی، مناطق جنگلی",
                diet = "لارو از گیاهان علفی تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لاروها موهای محافظ دارند"
            )
            
            "Arctia caja(Adult)" -> InsectInfo(
                name = "شب‌پره خرس بزرگ",
                scientificName = "Arctia caja",
                family = "Erebidae (آرکتیینا)",
                description = "شب‌پره بزرگ با بال‌های قهوه‌ای و لکه‌های سفید",
                habitat = "اروپا، آسیا، آمریکای شمالی",
                diet = "لارو از گیاهان مختلف تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لاروها به کرم‌های پشمالو معروف هستند"
            )
            
            "Arctia caja(Larve)" -> InsectInfo(
                name = "لارو شب‌پره خرس بزرگ",
                scientificName = "Arctia caja",
                family = "Erebidae (آرکتیینا)",
                description = "لارو سیاه پوشیده از موهای قهوه‌ای بلند",
                habitat = "چمنزارها، باغ‌ها",
                diet = "گیاهان علفی مختلف",
                lifecycle = "مرحله لاروی چند ماه",
                isDangerous = false,
                interestingFacts = "موهای آن می‌تواند در برخی افراد حساسیت ایجاد کند"
            )
            
            "Argema mittrei" -> InsectInfo(
                name = "پروانه ابریشمی ماداگاسکار",
                scientificName = "Argema mittrei",
                family = "Saturniidae (پروانه‌های ابریشمی)",
                description = "بزرگ‌ترین پروانه شب‌پره اروپا با دم‌های بلند و رنگ زرد درخشان",
                habitat = "جنگل‌های بارانی ماداگاسکار",
                diet = "بالغ بدون دهان، لارو از درختان بومی",
                lifecycle = "تخم، لارو، شفیره، بالغ (کوتاه)",
                isDangerous = false,
                interestingFacts = "عمر بالغ فقط چند روز است و غذا نمی‌خورد"
            )
            
            "Argema mittrei(Larve)" -> InsectInfo(
                name = "لارو پروانه ابریشمی ماداگاسکار",
                scientificName = "Argema mittrei",
                family = "Saturniidae (پروانه‌های ابریشمی)",
                description = "لارو سبز بزرگ با غده‌های کوچک",
                habitat = "جنگل‌های ماداگاسکار",
                diet = "برگ درختان بومی",
                lifecycle = "مرحله لاروی چند ماه",
                isDangerous = false,
                interestingFacts = "لاروها برای پیله‌سازی ابریشم تولید می‌کنند"
            )
            
            "Attacus atlas" -> InsectInfo(
                name = "پروانه اطلس",
                scientificName = "Attacus atlas",
                family = "Saturniidae (پروانه‌های ابریشمی)",
                description = "یکی از بزرگ‌ترین پروانه‌های جهان با بال‌های قرمز-قهوه‌ای و سر شبیه مار",
                habitat = "جنگل‌های گرمسیری آسیا",
                diet = "بالغ بدون دهان، لارو از درختان مختلف",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "پهنای بال تا 25 سانتی‌متر، سر لارو شبیه مار برای ترساندن شکارچیان"
            )
            
            "Cabera variolaria" -> InsectInfo(
                name = "شب‌پره هندسی",
                scientificName = "Cabera variolaria",
                family = "Geometridae (شب‌پره‌های هندسی)",
                description = "شب‌پره کوچک با بال‌های سفید و نقاط سیاه",
                habitat = "اروپا، جنگل‌ها و باغ‌ها",
                diet = "لارو از برگ درختان تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لاروها به کرم‌های اینچ‌ورم معروف هستند"
            )
            
            "Cerambyx cerdo" -> InsectInfo(
                name = "سوسک شاخدار بزرگ بلوط",
                scientificName = "Cerambyx cerdo",
                family = "Cerambycidae (سوسک‌های شاخدار)",
                description = "سوسک بزرگ قهوه‌ای-سیاه با شاخک‌های بسیار بلند",
                habitat = "جنگل‌های بلوط اروپا و خاورمیانه",
                diet = "لارو از چوب درختان پیر بلوط تغذیه می‌کند",
                lifecycle = "تخم، لارو (3-4 سال)، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "گونه محافظت‌شده در اروپا، لارو تا 5 سال رشد می‌کند"
            )
            
            "Cerroneuroterus lanuginosus" -> InsectInfo(
                name = "زنبور پشمی",
                scientificName = "Cerroneuroterus lanuginosus",
                family = "Tenthredinidae (زنبورهای اره‌ای)",
                description = "زنبور کوچک پوشیده از موهای زرد-نارنجی",
                habitat = "اروپا، مناطق جنگلی",
                diet = "لارو از برگ درختان تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "ظاهر پشمالو و جذاب"
            )
            
            "Cryptolaemus montrouzieri" -> InsectInfo(
                name = "کفشدوزک شکارگر",
                scientificName = "Cryptolaemus montrouzieri",
                family = "Coccinellidae (کفشدوزک‌ها)",
                description = "کفشدوزک قهوه‌ای-سفید شبیه به شپشک آردآلود",
                habitat = "مناطق گرمسیری و نیمه‌گرمسیری",
                diet = "شپشک‌های آردآلود و سایر آفات",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "عامل کنترل بیولوژیکی مهم علیه شپشک‌ها"
            )
            
            "Curculio glandium" -> InsectInfo(
                name = "خرطومی بلوط",
                scientificName = "Curculio glandium",
                family = "Curculionidae (سوسک‌های خرطومی)",
                description = "سوسک کوچک قهوه‌ای با خرطوم بلند",
                habitat = "جنگل‌های بلوط",
                diet = "لارو از بلوط تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "ماده با خرطوم در بلوط سوراخ ایجاد کرده و تخم می‌گذارد"
            )
            
            "Cydia latiferreana" -> InsectInfo(
                name = "بید میوه بلوط",
                scientificName = "Cydia latiferreana",
                family = "Tortricidae",
                description = "بید کوچک با بال‌های خاکستری-قهوه‌ای",
                habitat = "جنگل‌های بلوط",
                diet = "لارو از بلوط تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لارو در داخل بلوط زندگی می‌کند"
            )
            
            "Cydia pomonella" -> InsectInfo(
                name = "کرم سیب",
                scientificName = "Cydia pomonella",
                family = "Tortricidae",
                description = "بید کوچک با بال‌های خاکستری و رگه‌های مسی",
                habitat = "باغ‌های سیب و گلابی در سراسر جهان",
                diet = "لارو از میوه سیب، گلابی، گلابی تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "یکی از مهم‌ترین آفات میوه‌های هسته‌دار در جهان"
            )
            
            "Danaus plexippus" -> InsectInfo(
                name = "پروانه مونارک",
                scientificName = "Danaus plexippus",
                family = "Nymphalidae",
                description = "پروانه معروف نارنجی با رگه‌های سیاه و حاشیه سفید",
                habitat = "آمریکای شمالی، مهاجر به مکزیک",
                diet = "لارو از گیاه آسکلپیاس، بالغ از شهد گل‌ها",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "مهاجرت سالانه تا 4000 کیلومتر، سمی برای پرندگان"
            )
            
            "Deilephila elpenor" -> InsectInfo(
                name = "شب‌پره فیل صورتی",
                scientificName = "Deilephila elpenor",
                family = "Sphingidae (شب‌پره‌های اسپفنگس)",
                description = "شب‌پره زیبا با رنگ صورتی و زیتونی",
                habitat = "اروپا، آسیا، باغ‌ها",
                diet = "لارو از گل مغربی و فوشیا",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لارو دارای طرح چشم روی بدن برای ترساندن شکارچیان"
            )
            
            "Dicranura ulmi" -> InsectInfo(
                name = "شب‌پره دمدار نارون",
                scientificName = "Dicranura ulmi",
                family = "Notodontidae",
                description = "شب‌پره با بال‌های خاکستری و دم‌های باریک",
                habitat = "اروپا، جنگل‌های نارون",
                diet = "لارو از برگ نارون تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "بال‌های عقبی دارای دم‌های باریک است"
            )
            
            "Dicycla oo" -> InsectInfo(
                name = "شب‌پره حرف یونانی",
                scientificName = "Dicycla oo",
                family = "Noctuidae (شب‌پره‌های جغد)",
                description = "شب‌پره با علامت OO روی بال‌ها",
                habitat = "اروپا، جنگل‌ها",
                diet = "لارو از درختان پهن‌برگ",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "علامت OO روی بال‌ها مشخصه این گونه است"
            )
            
            "Dinoptera collaris" -> InsectInfo(
                name = "سوسک شاخدار کوچک",
                scientificName = "Dinoptera collaris",
                family = "Cerambycidae (سوسک‌های شاخدار)",
                description = "سوسک کوچک سیاه با طوق قرمز",
                habitat = "اروپا، روی گل‌ها",
                diet = "بالغ از گرده و گلبرگ",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "اغلب روی گل‌های سفید دیده می‌شود"
            )
            
            "Diprion pini" -> InsectInfo(
                name = "زنبور اره‌ای کاج",
                scientificName = "Diprion pini",
                family = "Diprionidae (زنبورهای اره‌ای)",
                description = "زنبور اره‌ای شبیه زنبور با بدن سبز",
                habitat = "جنگل‌های کاج اروپا و آسیا",
                diet = "لارو از سوزن کاج تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم جنگل‌های کاج، می‌تواند درختان را لخت کند"
            )
            
            "Epicometis hirta" -> InsectInfo(
                name = "سوسک گل‌خوار",
                scientificName = "Epicometis hirta",
                family = "Scarabaeidae (سوسک‌های اسکاراب)",
                description = "سوسک متوسط سیاه با موهای خاکستری",
                habitat = "اروپا، مزارع و باغ‌ها",
                diet = "بالغ از گل‌ها و گرده",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت گل‌های میوه‌ها و انگور"
            )
            
            "Euproctis chrysorrhoea" -> InsectInfo(
                name = "شب‌پره قهوه‌ای دم‌طلایی",
                scientificName = "Euproctis chrysorrhoea",
                family = "Erebidae (لیپیدوپترا)",
                description = "شب‌پره سفید با دسته موی قهوه‌ای-طلایی در انتهای بدن",
                habitat = "اروپا، آسیا، آمریکای شمالی",
                diet = "لارو از برگ درختان مختلف",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "موهای لارو می‌تواند درماتیت شدید ایجاد کند"
            )
            
            "Gypsonoma aceriana" -> InsectInfo(
                name = "بید جوانه‌خوار افرا",
                scientificName = "Gypsonoma aceriana",
                family = "Tortricidae",
                description = "بید کوچک با بال‌های قهوه‌ای روشن",
                habitat = "اروپا، روی درختان افرا",
                diet = "لارو از جوانه‌های افرا تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لارو در جوانه‌ها زندگی می‌کند"
            )
            
            "Harpyia milhauseri" -> InsectInfo(
                name = "شب‌پره هارپی",
                scientificName = "Harpyia milhauseri",
                family = "Notodontidae",
                description = "شب‌پره متوسط با بال‌های خاکستری و طرح خاص",
                habitat = "اروپا، جنگل‌های برگ‌ریز",
                diet = "لارو از برگ درختان پهن‌برگ",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لارو دارای شاخک‌های قرمز در انتهای بدن"
            )
            
            "Hesperophanes sericeus" -> InsectInfo(
                name = "سوسک شاخدار ابریشمی",
                scientificName = "Hesperophanes sericeus",
                family = "Cerambycidae (سوسک‌های شاخدار)",
                description = "سوسک بزرگ با بدن پوشیده از موهای ابریشمی قهوه‌ای",
                habitat = "اروپای جنوبی، جنگل‌های بلوط",
                diet = "لارو از چوب درختان پهن‌برگ",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "ظاهر زیبا و ابریشمی"
            )
            
            "Hyles lineata" -> InsectInfo(
                name = "شب‌پره خط‌دار",
                scientificName = "Hyles lineata",
                family = "Sphingidae (شب‌پره‌های اسپفنگس)",
                description = "شب‌پره متوسط با بال‌های قهوه‌ای و خطوط سفید",
                habitat = "آمریکای شمالی و جنوبی",
                diet = "لارو از گیاهان مختلف",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لارو می‌تواند رنگ‌های مختلفی داشته باشد"
            )
            
            "Hylesinus varius" -> InsectInfo(
                name = "سوسک پوست‌خوار زبان‌گنجشک",
                scientificName = "Hylesinus varius",
                family = "Curculionidae (سوسک‌های پوست‌خوار)",
                description = "سوسک کوچک قهوه‌ای تیره",
                habitat = "اروپا، روی درختان زبان‌گنجشک",
                diet = "لارو از زیر پوست درخت تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم درختان زبان‌گنجشک"
            )
            
            "Lachnus roboris" -> InsectInfo(
                name = "شته بلوط",
                scientificName = "Lachnus roboris",
                family = "Aphididae (شته‌ها)",
                description = "شته بزرگ قهوه‌ای با پاهای بلند",
                habitat = "روی درختان بلوط",
                diet = "شیره گیاهی",
                lifecycle = "تخم، پوره، بالغ",
                isDangerous = false,
                interestingFacts = "یکی از بزرگ‌ترین شته‌ها"
            )
            
            "Lampetis mimosa" -> InsectInfo(
                name = "سوسک جواهری",
                scientificName = "Lampetis mimosa",
                family = "Buprestidae (سوسک‌های جواهری)",
                description = "سوسک کوچک فلزی با رنگ‌های درخشان",
                habitat = "مناطق مدیترانه‌ای",
                diet = "لارو از چوب درختان",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "رنگ فلزی زیبا"
            )
            
            "Lyctus brunneus" -> InsectInfo(
                name = "سوسک پودرچوب",
                scientificName = "Lyctus brunneus",
                family = "Bostrichidae (سوسک‌های پودرچوب)",
                description = "سوسک کوچک قهوه‌ای باریک",
                habitat = "سراسر جهان، در چوب ساختمان",
                diet = "لارو از چوب سخت تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم چوب در ساختمان‌ها و مبلمان"
            )
            
            "Macroglossum stellatarum" -> InsectInfo(
                name = "شب‌پره بال‌شفاف",
                scientificName = "Macroglossum stellatarum",
                family = "Sphingidae (شب‌پره‌های اسپفنگس)",
                description = "شب‌پره کوچک با بال‌های قهوه‌ای و پرواز شبیه مرغ مگس‌خوار",
                habitat = "اروپا، آسیا، شمال آفریقا",
                diet = "بالغ از شهد گل‌ها، لارو از گل مغربی",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "در پرواز جلوی گل معلق می‌ماند و خرطوم بلند دارد"
            )
            
            "Metamasius hemipterus" -> InsectInfo(
                name = "خرطومی نیشکر",
                scientificName = "Metamasius hemipterus",
                family = "Curculionidae (سوسک‌های خرطومی)",
                description = "سوسک خرطومی با بدن سیاه و قرمز",
                habitat = "مناطق گرمسیری، مزارع نیشکر",
                diet = "لارو از ساقه نیشکر تغذیه می‌کند",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم نیشکر در مناطق گرمسیری"
            )
            
            "Nezara viridula" -> InsectInfo(
                name = "سن سبز",
                scientificName = "Nezara viridula",
                family = "Pentatomidae (سن‌های سپردار)",
                description = "سن سبز رنگ با شکل سپری",
                habitat = "سراسر جهان، مزارع و باغ‌ها",
                diet = "شیره گیاهان مختلف",
                lifecycle = "تخم، پوره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم کشاورزی، بوی بدی هنگام دفاع منتشر می‌کند"
            )
            
            "Nycteola asiatica" -> InsectInfo(
                name = "شب‌پره آسیایی",
                scientificName = "Nycteola asiatica",
                family = " Nolidae",
                description = "شب‌پره کوچک خاکستری",
                habitat = "آسیا، اروپا",
                diet = "لارو از برگ درختان",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "فعالیت شبانه"
            )
            
            "Opodiphthera astrophela" -> InsectInfo(
                name = "پروانه ابریشمی استرالیایی",
                scientificName = "Opodiphthera astrophela",
                family = "Saturniidae (پروانه‌های ابریشمی)",
                description = "پروانه بزرگ با بال‌های قهوه‌ای و لکه‌های چشمی",
                habitat = "استرالیا، جنگل‌های بارانی",
                diet = "لارو از درختان بومی",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لکه‌های چشمی روی بال‌ها برای ترساندن شکارچیان"
            )
            
            "Opodiphthera eucalypti" -> InsectInfo(
                name = "پروانه ابریشمی اکالیپتوس",
                scientificName = "Opodiphthera eucalypti",
                family = "Saturniidae (پروانه‌های ابریشمی)",
                description = "پروانه بزرگ با بال‌های قهوه‌ای و نارنجی",
                habitat = "استرالیا، جنگل‌های اکالیپتوس",
                diet = "لارو از برگ اکالیپتوس",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لارو بزرگ و سبز رنگ"
            )
            
            "Osphranteria coerulescens" -> InsectInfo(
                name = "سوسک چوب آبی",
                scientificName = "Osphranteria coerulescens",
                family = "Cerambycidae (سوسک‌های شاخدار)",
                description = "سوسک متوسط با رنگ آبی فلزی",
                habitat = "استرالیا",
                diet = "لارو از چوب درختان",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "رنگ آبی فلزی زیبا"
            )
            
            "Otiorhynchus sulcatus" -> InsectInfo(
                name = "خرطومی شیاردار",
                scientificName = "Otiorhynchus sulcatus",
                family = "Curculionidae (سوسک‌های خرطومی)",
                description = "سوسک خرطومی سیاه بدون بال با شیارهای روی بدن",
                habitat = "سراسر جهان، باغ‌ها",
                diet = "بالغ از برگ، لارو از ریشه",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم گیاهان زینتی و میوه‌ای، همه ماده هستند"
            )
            
            "Palpita unionalis" -> InsectInfo(
                name = "بید یاس",
                scientificName = "Palpita unionalis",
                family = "Crambidae",
                description = "بید کوچک با بال‌های سفید شفاف",
                habitat = "مدیترانه، باغ‌های زیتون",
                diet = "لارو از برگ زیتون و یاس",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم درختان زیتون"
            )
            
            "Papilio glaucus" -> InsectInfo(
                name = "پروانه دم‌چلچله‌ای زرد",
                scientificName = "Papilio glaucus",
                family = "Papilionidae (پروانه‌های دم‌چلچله‌ای)",
                description = "پروانه بزرگ زرد با خطوط سیاه و دم‌های باریک",
                habitat = "آمریکای شمالی، جنگل‌ها و باغ‌ها",
                diet = "لارو از برگ درختان خانواده Lauraceae",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لارو دارای طرح چشم مار برای دفاع"
            )
            
            "Platypus cylindrus" -> InsectInfo(
                name = "سوسک پلاتیپوس",
                scientificName = "Platypus cylindrus",
                family = "Curculionidae (سوسک‌های آمبروزیا)",
                description = "سوسک استوانه‌ای قهوه‌ای",
                habitat = "اروپا، جنگل‌های بلوط",
                diet = "لارو از چوب درختان",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "ناقل قارچ‌های بیماری‌زا برای درختان"
            )
            
            "Psalmocharias alhageos" -> InsectInfo(
                name = "زنجره خرخر",
                scientificName = "Psalmocharias alhageos",
                family = "Cicadidae (زنجره‌ها)",
                description = "حشره بزرگ با صدای آواز بلند",
                habitat = "خاورمیانه، آسیای مرکزی",
                diet = "شیره گیاهان",
                lifecycle = "تخم، پوره (زیر زمین)، بالغ",
                isDangerous = false,
                interestingFacts = "نر با اندام صوتی آواز تولید می‌کند"
            )
            
            "Rhagoletis pomonella" -> InsectInfo(
                name = "مگس سیب",
                scientificName = "Rhagoletis pomonella",
                family = "Tephritidae (مگس‌های میوه)",
                description = "مگس کوچک با بال‌های شفاف و نوارهای سیاه",
                habitat = "آمریکای شمالی، باغ‌های سیب",
                diet = "لارو از گوشت میوه سیب",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم سیب در آمریکای شمالی"
            )
            
            "Saturnia pavonia" -> InsectInfo(
                name = "پروانه ابریشمی کوچک",
                scientificName = "Saturnia pavonia",
                family = "Saturniidae (پروانه‌های ابریشمی)",
                description = "پروانه متوسط قهوه‌ای با لکه‌های چشمی روی بال‌ها",
                habitat = "اروپا، جنگل‌ها و باغ‌ها",
                diet = "لارو از برگ درختان مختلف",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "ماده فرمون قوی برای جذب نر از فاصله چند کیلومتری تولید می‌کند"
            )
            
            "Schinia arcigera" -> InsectInfo(
                name = "شب‌پره گل‌خوار",
                scientificName = "Schinia arcigera",
                family = "Noctuidae (شب‌پره‌های جغد)",
                description = "شب‌پره کوچک با رنگ‌های متنوع",
                habitat = "آمریکای شمالی",
                diet = "لارو از گل گیاهان",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لارو در داخل گل زندگی می‌کند"
            )
            
            "Sirex noctilio" -> InsectInfo(
                name = "زنبور چوب‌خوار کاج",
                scientificName = "Sirex noctilio",
                family = "Siricidae (زنبورهای چوب)",
                description = "زنبور بزرگ با بدن فلزی آبی-سیاه",
                habitat = "اروپا، آسیا، استرالیا (گونه مهاجم)",
                diet = "لارو از چوب کاج",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم جنگل‌های کاج، ماده قارچ سمی را به درخت تزریق می‌کند"
            )
            
            "Smerinthus ocellata" -> InsectInfo(
                name = "شب‌پره چشم‌دار",
                scientificName = "Smerinthus ocellata",
                family = "Sphingidae (شب‌پره‌های اسپفنگس)",
                description = "شب‌پره قهوه‌ای با لکه‌های چشمی بزرگ قرمز روی بال‌های عقبی",
                habitat = "اروپا، آسیا",
                diet = "لارو از برگ درختان بید و توس",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لکه‌های چشمی برای ترساندن پرندگان"
            )
            
            "Sphrageidus similis" -> InsectInfo(
                name = "شب‌پره شبیه",
                scientificName = "Sphrageidus similis",
                family = "Lymantriidae",
                description = "شب‌پره متوسط با بال‌های سفید",
                habitat = "آسیا",
                diet = "لارو از برگ درختان",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "ظاهر ساده و بی‌آلایش"
            )
            
            "Spodoptera exigua" -> InsectInfo(
                name = "کرم برگ‌خوار چغندر",
                scientificName = "Spodoptera exigua",
                family = "Noctuidae (شب‌پره‌های جغد)",
                description = "بید متوسط با بال‌های خاکستری-قهوه‌ای",
                habitat = "سراسر جهان، مزارع",
                diet = "لارو از برگ و میوه گیاهان مختلف",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم چندین محصول کشاورزی، مقاومت به سموم"
            )
            
            "Spoladea recurvalis" -> InsectInfo(
                name = "بید برگ‌خوار",
                scientificName = "Spoladea recurvalis",
                family = "Crambidae",
                description = "بید با بال‌های سفید و نوارهای قهوه‌ای",
                habitat = "مناطق گرمسیری و نیمه‌گرمسیری",
                diet = "لارو از برگ گیاهان مختلف",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم سبزیجات"
            )
            
            "Stromatium auratum" -> InsectInfo(
                name = "سوسک شاخدار طلایی",
                scientificName = "Stromatium auratum",
                family = "Cerambycidae (سوسک‌های شاخدار)",
                description = "سوسک بزرگ با رنگ طلایی فلزی",
                habitat = "استرالیا",
                diet = "لارو از چوب درختان",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "رنگ طلایی درخشان"
            )
            
            "Synanthedon pyri" -> InsectInfo(
                name = "بید شاخه‌خوار گلابی",
                scientificName = "Synanthedon pyri",
                family = "Sesiidae (بیدهای شیشه‌بال)",
                description = "بید با بال‌های شفاف شبیه زنبور",
                habitat = "اروپا، باغ‌های گلابی",
                diet = "لارو از زیر پوست درخت گلابی",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "ظاهر شبیه زنبور برای فریب شکارچیان"
            )
            
            "Tabanus atratus" -> InsectInfo(
                name = "مگس اسب سیاه",
                scientificName = "Tabanus atratus",
                family = "Tabanidae (مگس‌های اسب)",
                description = "مگس بزرگ سیاه با چشمان بزرگ",
                habitat = "سراسر جهان، نزدیک دام‌ها",
                diet = "ماده از خون دام‌ها",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "نیش دردناک، ناقل بیماری‌ها"
            )
            
            "Tortrix viridana" -> InsectInfo(
                name = "بید سبز بلوط",
                scientificName = "Tortrix viridana",
                family = "Tortricidae",
                description = "بید کوچک با بال‌های سبز زیتونی",
                habitat = "اروپا، جنگل‌های بلوط",
                diet = "لارو از برگ و بلوط",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "رنگ سبز زیبا"
            )
            
            "Tyria jacobaeae" -> InsectInfo(
                name = "شب‌پره کرمی جاکوبیا",
                scientificName = "Tyria jacobaeae",
                family = "Erebidae (آرکتیینا)",
                description = "شب‌پره با بال‌های سیاه و زرد",
                habitat = "اروپا، علفزارها",
                diet = "لارو از گیاه جاکوبیا (سمی)",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "لارو سم گیاه میزبان را جذب کرده و برای پرندگان سمی می‌شود"
            )
            
            "Tyria jacobaeae(Adult)" -> InsectInfo(
                name = "شب‌پره کرمی جاکوبیا بالغ",
                scientificName = "Tyria jacobaeae",
                family = "Erebidae (آرکتیینا)",
                description = "شب‌پره بالغ با بال‌های سیاه و نوارهای زرد",
                habitat = "اروپا، علفزارها",
                diet = "شهد گل‌ها",
                lifecycle = "مرحله بالغ",
                isDangerous = false,
                interestingFacts = "رنگ هشداردهنده برای شکارچیان"
            )
            
            "Vanessa atalanta" -> InsectInfo(
                name = "پروانه آتالانتا",
                scientificName = "Vanessa atalanta",
                family = "Nymphalidae",
                description = "پروانه زیبا با بال‌های سیاه، نارنجی و سفید",
                habitat = "سراسر جهان",
                diet = "بالغ از شهد گل‌ها و میوه‌های رسیده",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "مهاجر، در زمستان به مناطق گرمسیری می‌رود"
            )
            
            "Vespa crabro" -> InsectInfo(
                name = "زنبور سرخ اروپایی",
                scientificName = "Vespa crabro",
                family = "Vespidae",
                description = "بزرگ‌ترین زنبور اجتماعی اروپا با رنگ قرمز-قهوه‌ای",
                habitat = "اروپا، آسیا، آمریکای شمالی",
                diet = "حشرات دیگر، میوه‌های رسیده",
                lifecycle = "کلونی فصلی",
                isDangerous = true,
                interestingFacts = "نیش دردناک اما معمولاً تهاجمی نیست"
            )
            
            "Vespula maculifrons" -> InsectInfo(
                name = "زنبور زرد پیشانی‌خال‌دار",
                scientificName = "Vespula maculifrons",
                family = "Vespidae",
                description = "زنبور زرد و سیاه با خال روی پیشانی",
                habitat = "آمریکای شمالی",
                diet = "حشرات، شهد، زباله‌های انسانی",
                lifecycle = "کلونی فصلی",
                isDangerous = true,
                interestingFacts = "تهاجمی و نیش دردناک"
            )
            
            "Xanthogaleruca luteola" -> InsectInfo(
                name = "سوسک برگ‌خوار نارون",
                scientificName = "Xanthogaleruca luteola",
                family = "Chrysomelidae (سوسک‌های برگ‌خوار)",
                description = "سوسک کوچک زرد-قهوه‌ای",
                habitat = "اروپا، آمریکای شمالی",
                diet = "لارو و بالغ از برگ نارون",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "آفت مهم درختان نارون"
            )
            
            "Xylocopa valga" -> InsectInfo(
                name = "زنبور نجار",
                scientificName = "Xylocopa valga",
                family = "Apidae (زنبورهای عسل)",
                description = "زنبور بزرگ سیاه با بال‌های بنفش",
                habitat = "مدیترانه، خاورمیانه",
                diet = "شهد و گرده گل‌ها",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = false,
                interestingFacts = "در چوب تونل حفر می‌کند، گرده‌افشان مهم"
            )
            
            "Yponomeuta padella" -> InsectInfo(
                name = "بید تارتن ارغوانی",
                scientificName = "Yponomeuta padella",
                family = "Yponomeutidae",
                description = "بید کوچک سفید با نقاط سیاه",
                habitat = "اروپا، آسیا",
                diet = "لارو از برگ درختان آلبالو و پرنوس",
                lifecycle = "تخم، لارو، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "لارو تارهای بزرگی روی درخت می‌سازد"
            )
            
            "Yponomeuta padella(Larve)" -> InsectInfo(
                name = "لارو بید تارتن ارغوانی",
                scientificName = "Yponomeuta padella",
                family = "Yponomeutidae",
                description = "لارو کوچک سفید با سر سیاه",
                habitat = "روی درختان آلبالو و پرنوس",
                diet = "برگ درختان",
                lifecycle = "مرحله لاروی",
                isDangerous = true,
                interestingFacts = "به‌صورت گروهی زندگی و تار می‌سازد"
            )
            
            "Zeuzera pyrina" -> InsectInfo(
                name = "بید چوب‌خوار",
                scientificName = "Zeuzera pyrina",
                family = "Cossidae (بیدهای چوب‌خوار)",
                description = "بید بزرگ سفید با نقاط آبی-سیاه",
                habitat = "اروپا، آسیا، باغ‌ها",
                diet = "لارو از چوب درختان مختلف",
                lifecycle = "تخم، لارو (2-3 سال)، شفیره، بالغ",
                isDangerous = true,
                interestingFacts = "لارو تونل‌های بزرگ در چوب حفر می‌کند"
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
