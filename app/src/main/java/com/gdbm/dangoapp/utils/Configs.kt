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
    val OPTIONS_REFERENCE_SCREEN = listOf(
        MenuElement(
            name = "Hiragana",
            content = "hiragana",
            java = ReferenceActivity::class.java),
        MenuElement(
            name = "Katakana",
            content = "katakana",
            java = ReferenceActivity::class.java
        ),
        MenuElement(
            name = "Numbers",
            content = "numbers",
            isLargeContent = true,
            java = ReferenceActivity::class.java
        ),
        MenuElement(
            name = "Dates",
            content = "dates",
            isLargeContent = true,
            java = ReferenceActivity::class.java
        ),
        MenuElement(
            name = "Verbs",
            content = "verbs",
            isLargeContent = true,
            java = ReferenceActivity::class.java
        ),
        MenuElement(
            name = "Particles",
            content = "particles",
            isLargeContent = true,
            java = ReferenceActivity::class.java
        ),
        MenuElement(
            name = "Adjectives",
            content = "adjectives",
            isLargeContent = true,
            java = ReferenceActivity::class.java
        )
    )
    val OPTIONS_PRACTICE_SCREEN = listOf(
        MenuElement(
            name = "Syllabary",
            content = "syllabaryPractice",
            java = WritingPracticeActivity::class.java
        ),
        MenuElement(
            name = "Vocabulary",
            content = "vocabularyPractice",
            java = WritingPracticeActivity::class.java
        ),
        MenuElement(
            name = "Syllabary Drawing",
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
            name = "Vocabulary Drawing",
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

    val MENU_OPTIONS = OPTIONS_REFERENCE_SCREEN + OPTIONS_PRACTICE_SCREEN
    val SYLLABARY_ITEMS = listOf("hiragana","katakana")
    val VOCABULARY_ITEMS = listOf("numbers","dates")
    const val ALL_CONTENT = "allContent"
    const val CONTENT_FOR_ = "contentFor"
    const val CONFIGURATIONS_FOR_ = "configurationsFor"
    const val SYLLABARY_CONTENT = "syllabaryPractice"
    const val VOCABULARY_CONTENT = "vocabularyPractice"

    const val JAPANESE_HASH = "6c2050f8d0afa51826b04edae718b484"
}