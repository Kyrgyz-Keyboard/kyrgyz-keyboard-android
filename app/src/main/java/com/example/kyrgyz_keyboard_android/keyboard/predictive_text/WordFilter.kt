package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

object WordFilter {
    private val offensiveWords = mutableSetOf<String>(
        "киргизия"
    )
    
    private val offensiveRoots = mutableSetOf<String>(
        "бля", "сука", "хуй", "нахум", "сиги", "сиге",
        "астаф", "машал", "инша", "алла",
        "киргизск", "киргизк"
    )

    private val specialReplacements = mapOf(
        "киргизия" to listOf("Кыргыз Республикасы", "Кыргызстан"),

        "киргизск" to listOf("кыргызский"),
        "киргизски" to listOf("кыргызский"),
        "киргизский" to listOf("кыргызский"),

        "бля" to listOf("Сөгүнбө"),
        "блят" to listOf("Сөгүнбө"),
        "блять" to listOf("Сөгүнбө"),
        "хуй" to listOf("Сөгүнбө"),
        "сиги" to listOf("Сөгүнбө"),
        "сиге" to listOf("Сөгүнбө")
    )

    fun filterPredictions(input: String, predictions: List<String>): List<String> {
        val lowerInput = input.lowercase().trim()

        specialReplacements[lowerInput]?.let { replacements ->
            return replacements
        }

        if (lowerInput == "киргизи" || lowerInput == "киргизия") {
            specialReplacements["киргизия"]?.let { replacements ->
                return replacements
            }
        }
        
        return predictions.filter { prediction ->
            !isOffensive(prediction)
        }
    }

    private fun isOffensive(word: String): Boolean {
        val lowerWord = word.lowercase()
        
        if (offensiveWords.contains(lowerWord)) {
            return true
        }
        
        return offensiveRoots.any { root ->
            lowerWord.contains(root)
        }
    }

    fun addOffensiveWord(word: String) {
        offensiveWords.add(word.lowercase())
    }

    fun addOffensiveRoot(root: String) {
        offensiveRoots.add(root.lowercase())
    }

    fun removeOffensiveWord(word: String) {
        offensiveWords.remove(word.lowercase())
    }

    fun removeOffensiveRoot(root: String) {
        offensiveRoots.remove(root.lowercase())
    }

    fun getOffensiveWords(): Set<String> = offensiveWords.toSet()

    fun getOffensiveRoots(): Set<String> = offensiveRoots.toSet()
}