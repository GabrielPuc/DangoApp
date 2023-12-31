package com.gdbm.dangoapp.config

import com.gdbm.dangoapp.activities.DrawPracticeActivity
import com.gdbm.dangoapp.activities.ReferenceActivity
import com.gdbm.dangoapp.activities.WritingPracticeActivity

object Configs{
    const val ENDPOINT = ""
    data class MenuElement(
        val name: String,
        val content: String? = null,
        val isLargeContent: Boolean = false,
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
            java = ReferenceActivity::class.java
        ),
        MenuElement(
            name = "Dates Reference",
            content = "dates",
            java = ReferenceActivity::class.java
        ),
        MenuElement(
            name = "Syllabary Practice",
            content = "syllabary_practice",
            java = WritingPracticeActivity::class.java
        ),
        MenuElement(
            name = "Drawing Practice",
            java = DrawPracticeActivity::class.java
        )
    )
    const val ALL_CONTENT = "allContent"
    const val HIRAGANA = "hiragana"
    const val KATAKANA = "katakana"
    const val NUMBERS = "numbers"
}