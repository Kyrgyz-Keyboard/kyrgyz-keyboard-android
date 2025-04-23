package com.example.kyrgyz_keyboard_android.keyboard.predictive_text

class DummyPredictiveEngine : PredictiveTextEngine {
    private val wordDatabase = mapOf(
        "к" to listOf("кат", "кел", "көр", "күн", "кыз", "кой"),
        "ке" to listOf("кел", "кет", "кеч", "кече", "келин"),
        "ка" to listOf("кат", "кайт", "кара", "калк", "кагаз"),
        "б" to listOf("бар", "бер", "бол", "бул", "бир", "баш"),
        "ба" to listOf("бар", "бат", "бала", "башта", "барак"),
        "с" to listOf("сал", "сен", "сүй", "сөз", "сыр", "сан"),
        "са" to listOf("сал", "сан", "сары", "салам", "сабак", "салкын", "саат"),
        "м" to listOf("мен", "мал", "миң", "май", "муз"),
        "ме" to listOf("мен", "мейли", "мейиз"),
        "мен" to listOf("мени", "менен", "меники", "менин"),
        "мени" to listOf("меники", "менин"),
        "ма" to listOf("мал", "май", "маани", "макал"),
        "т" to listOf("тил", "тур", "тоо", "таш", "тон")
    )

    private val nextWordDatabase = mapOf(
        "" to listOf("салам", "кыргыз", "менин", "бул"),
        "салам" to listOf("алып", "бер", "кантип", "көрүшкөнчө"),
        "көрүшкөнчө" to listOf("кой", "бол", "көр"),
        "менин" to listOf("атым", "үйүм", "жерим")
    )

    override fun getPredictions(currentText: String): List<WordPrediction> {
        val text = currentText.lowercase()

        // Exact match
        wordDatabase[text]?.let { words ->
            return words.map { WordPrediction(word = it, confidence = 1.0f) }
        }

        // If no exact match, finds the key that matches our current text
        return wordDatabase.entries
            .firstOrNull { (key, _) -> key == text }
            ?.value?.map { WordPrediction(word = it, confidence = 1.0f) }
            ?: emptyList()
    }

    override fun getNextWordPredictions(previousWords: String): List<WordPrediction> {
        val words = previousWords.trim().split(" ")
        val lastWord = words.lastOrNull() ?: ""

        return when {
            lastWord.isEmpty() -> {
                // At the start of input or after space
                nextWordDatabase[""]?.map { word ->
                    WordPrediction(word = word, confidence = 1.0f)
                } ?: emptyList()
            }
            nextWordDatabase.containsKey(lastWord.lowercase()) -> {
                // Found exact match in next word database
                nextWordDatabase[lastWord.lowercase()]!!.map { word ->
                    WordPrediction(word = word, confidence = 1.0f)
                }
            }
            else -> {
                // If no exact match in next word database, gives word predictions
                getPredictions(lastWord)
            }
        }
    }
}