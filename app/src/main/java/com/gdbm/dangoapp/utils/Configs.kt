package com.gdbm.dangoapp.utils

import com.gdbm.dangoapp.activities.DrawPracticeActivity
import com.gdbm.dangoapp.activities.ReferenceActivity
import com.gdbm.dangoapp.activities.WritingPracticeActivity

object Configs{
    const val ENDPOINT = "https://4xmbrnizekycnz7vzdi76ewyyq0ilzfh.lambda-url.us-east-2.on.aws"
    data class MenuElement(
        val name: String,
        val subtitle: String? = null,
        val content: String? = null,
        val isLargeContent: Boolean? = null,
        val experimentalEnabled:Boolean? = null,
        val java: Any
    )

    const val CURRENT_LANGUAGE = "japanese"
    val MENU_OPTIONS = listOf(
        MenuElement(
            name = "Hiragana Reference",
            content = "hiragana",
            java = ReferenceActivity::class.java),
        MenuElement(
            name = "Katakana Reference",
            content = "katakana",
            java = ReferenceActivity::class.java
        ),
        MenuElement(
            name = "Numbers Reference",
            content = "numbers",
            isLargeContent = true,
            java = ReferenceActivity::class.java
        ),
        MenuElement(
            name = "Dates Reference",
            content = "dates",
            isLargeContent = true,
            java = ReferenceActivity::class.java
        ),
        MenuElement(
            name = "Syllabary Practice",
            content = "syllabaryPractice",
            java = WritingPracticeActivity::class.java
        ),
        MenuElement(
            name = "Vocabulary Practice",
            content = "vocabularyPractice",
            java = WritingPracticeActivity::class.java
        ),
        MenuElement(
            name = "Syllabary Drawing Practice",
            content = "syllabaryPractice",
            java = DrawPracticeActivity::class.java
        ),
        MenuElement(
            name = "Syllabary Drawing Test",
            subtitle = "EXPERIMENTAL FUNCTION",
            content = "syllabaryPractice",
            experimentalEnabled = true,
            java = DrawPracticeActivity::class.java
        ),
        MenuElement(
            name = "Vocabulary Drawing Practice",
            content = "vocabularyPractice",
            java = DrawPracticeActivity::class.java
        ),
        MenuElement(
            name = "Vocabulary Drawing Test",
            subtitle = "EXPERIMENTAL FUNCTION",
            content = "vocabularyPractice",
            experimentalEnabled = true,
            java = DrawPracticeActivity::class.java
        )
    )
    val SYLLABARY_ITEMS = listOf("hiragana","katakana")
    val VOCABULARY_ITEMS = listOf("numbers","dates")
    const val ALL_CONTENT = "allContent"
    const val CONTENT_FOR_ = "contentFor"
    const val CONFIGURATIONS_FOR_ = "configurationsFor"
    const val SYLLABARY_CONTENT = "syllabaryPractice"
    const val VOCABULARY_CONTENT = "vocabularyPractice"
}