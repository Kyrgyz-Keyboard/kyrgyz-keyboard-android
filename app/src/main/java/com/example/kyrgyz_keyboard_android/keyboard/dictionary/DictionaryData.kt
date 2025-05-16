package com.example.kyrgyz_keyboard_android.keyboard.dictionary

import com.example.kyrgyz_keyboard_android.ui.keyboard.utils.KeyboardConstants

data class DictionaryWord(
    val word: String,
    val translation: String,
    val category: String
)

fun getDictionaryWords(): List<DictionaryWord> {
    return listOf(
        // Common words
        DictionaryWord("жалпы", "в общем", KeyboardConstants.COMMON_DICT),
        DictionaryWord("баса", "кстати", KeyboardConstants.COMMON_DICT),
        DictionaryWord("кыскача", "короче", KeyboardConstants.COMMON_DICT),
        DictionaryWord("дароо", "сразу", KeyboardConstants.COMMON_DICT),
        DictionaryWord("дайыма", "всегда", KeyboardConstants.COMMON_DICT),
        DictionaryWord("улам-улам", "часто", KeyboardConstants.COMMON_DICT),
        DictionaryWord("так", "точно 1", KeyboardConstants.COMMON_DICT),
        DictionaryWord("сөзсүз", "обязательно", KeyboardConstants.COMMON_DICT),
        DictionaryWord("тамашасыз", "серьезно", KeyboardConstants.COMMON_DICT),
        DictionaryWord("өзгөчө", "особенно", KeyboardConstants.COMMON_DICT),
        DictionaryWord("албетте", "конечно", KeyboardConstants.COMMON_DICT),
        DictionaryWord("такыр", "совсем", KeyboardConstants.COMMON_DICT),
        DictionaryWord("жок дегенде", "хотя бы", KeyboardConstants.COMMON_DICT),
        DictionaryWord("адатта", "обычно", KeyboardConstants.COMMON_DICT),
        DictionaryWord("кээде", "иногда", KeyboardConstants.COMMON_DICT),
        DictionaryWord("чанда", "редко", KeyboardConstants.COMMON_DICT),
        DictionaryWord("аңгыча", "вдруг", KeyboardConstants.COMMON_DICT),
        DictionaryWord("чын эле", "точно 2", KeyboardConstants.COMMON_DICT),
        DictionaryWord("демек", "значит", KeyboardConstants.COMMON_DICT),

        // Fun words with similar spelling
        DictionaryWord("уулуу", "отравленный", KeyboardConstants.FUN_DICT),
        DictionaryWord("уулу", "сын", KeyboardConstants.FUN_DICT),
        DictionaryWord("улуу", "великий", KeyboardConstants.FUN_DICT),
        DictionaryWord("суулуу", "водный", KeyboardConstants.FUN_DICT),
        DictionaryWord("сулуу", "красивый", KeyboardConstants.FUN_DICT),
        DictionaryWord("сулу", "овес", KeyboardConstants.FUN_DICT),
        DictionaryWord("жаакка", "на пощечину", KeyboardConstants.FUN_DICT),
        DictionaryWord("жакка", "в сторону", KeyboardConstants.FUN_DICT),
        DictionaryWord("жака", "воротник", KeyboardConstants.FUN_DICT),
        DictionaryWord("кууруу", "жарить", KeyboardConstants.FUN_DICT),
        DictionaryWord("куруу", "строить", KeyboardConstants.FUN_DICT),
        DictionaryWord("куру", "сухой", KeyboardConstants.FUN_DICT),
        DictionaryWord("каала", "желать", KeyboardConstants.FUN_DICT),
        DictionaryWord("калаа", "город", KeyboardConstants.FUN_DICT),
        DictionaryWord("кала", "плата зерном", KeyboardConstants.FUN_DICT),

        // Day of Week names
        DictionaryWord("дүйшөмбү", "понедельник", KeyboardConstants.DAYS_DICT),
        DictionaryWord("шейшемби", "вторник", KeyboardConstants.DAYS_DICT),
        DictionaryWord("шаршемби", "среда", KeyboardConstants.DAYS_DICT),
        DictionaryWord("бейшемби", "четверг", KeyboardConstants.DAYS_DICT),
        DictionaryWord("жума", "пятница", KeyboardConstants.DAYS_DICT),
        DictionaryWord("ишемби", "суббота", KeyboardConstants.DAYS_DICT),
        DictionaryWord("жекшемби", "воскресенье", KeyboardConstants.DAYS_DICT),

//        // Month names
//        DictionaryWord("Жалган куран", "март - фальшивая косуля", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Чын куран", "апрель - настоящая косуля", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Бугу", "май - олень", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Кулжа", "июнь - горный баран", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Теке", "июль - горный козел", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Баш оона", "август - начало", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Аяк оона", "сентябрь - конец", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Тогуздун айы", "октябрь - 9й месяц", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Жетинин айы", "ноябрь - 7й месяц", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Бештин айы", "декабрь - 5й месяц", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Үчтүн айы", "январь - 3й месяц", KeyboardConstants.MONTH_DAY_DICT),
//        DictionaryWord("Бирдин айы", "февраль - 1й месяц", KeyboardConstants.MONTH_DAY_DICT)

        // Month names
        DictionaryWord("Жалган куран", "март", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Чын куран", "апрель", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Бугу", "май", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Кулжа", "июнь", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Теке", "июль", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Баш оона", "август", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Аяк оона", "сентябрь", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Тогуздун айы", "октябрь", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Жетинин айы", "ноябрь", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Бештин айы", "декабрь", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Үчтүн айы", "январь", KeyboardConstants.MONTH_DAY_DICT),
        DictionaryWord("Бирдин айы", "февраль", KeyboardConstants.MONTH_DAY_DICT)
    )
}