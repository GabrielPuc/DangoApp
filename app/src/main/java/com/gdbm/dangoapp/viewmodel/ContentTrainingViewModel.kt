package com.gdbm.dangoapp.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdbm.dangoapp.utils.Configs.SYLLABARY_CONTENT
import com.gdbm.dangoapp.utils.Configs.SYLLABARY_ITEMS
import com.gdbm.dangoapp.utils.Configs.VOCABULARY_CONTENT
import com.gdbm.dangoapp.utils.Configs.VOCABULARY_ITEMS
import com.gdbm.dangoapp.managers.ContentManager
import com.gdbm.dangoapp.model.Word
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ContentTrainingViewModel : ViewModel() {

    private val selectedWords = mutableListOf<Word>()
    private val previousWord = ArrayDeque(listOf<Word>())
    private lateinit var currentSymbol:Word
    lateinit var contentManager:ContentManager
    val drawingPair = MutableLiveData<Pair<Bitmap,Word>>()
    private val _selectedContentTypes = MutableStateFlow<List<String>>(mutableListOf())
    val selectedContentTypes: StateFlow<List<String>>
        get() = _selectedContentTypes


    fun setCurrentContentManager(contentManager: ContentManager){
        this.contentManager = contentManager
    }


    fun createWordListFor(content:String?){
        val wordList = mutableListOf<String>()
        _selectedContentTypes.value = emptyList()
        when (content){
            SYLLABARY_CONTENT -> {
                SYLLABARY_ITEMS.iterator().forEach {
                    wordList.add(it)
                    contentManager.retrieve(it)?.let { retrievedWords ->
                        selectedWords.addAll(retrievedWords)
                    }
                }
            }
            VOCABULARY_CONTENT -> {
                VOCABULARY_ITEMS.iterator().forEach {
                    wordList.add(it)
                    contentManager.retrieve(it)?.let { retrievedWords ->
                        selectedWords.addAll(retrievedWords)
                    }
                }
            }
        }
        _selectedContentTypes.value = wordList
    }

    fun setWordsFromSelectedContent(){
        selectedWords.clear()
        _selectedContentTypes.value.iterator().forEach {
            contentManager.retrieve(it)?.let { retrievedWords ->
                selectedWords.addAll(retrievedWords)
            }
        }
    }

    fun getRandomWord() : Word {
        currentSymbol = selectedWords.random()
        while (previousWord.contains(currentSymbol)) {
            currentSymbol = selectedWords.random()
        }
        previousWord.addFirst(currentSymbol)
        if(previousWord.size >= 3){
            previousWord.removeLast()
        }
        return currentSymbol
    }

    fun getRandomWordWithOptions() : List<Word> {
        return selectedWords.asSequence().shuffled().take(4).toList().shuffled()
    }

    fun addContentTypeToSelected(contentType: String){
        _selectedContentTypes.update {
            it + contentType
        }
    }

    fun removeContentTypeFromSelected(contentType: String){
        _selectedContentTypes.update {
            it.filterNot { content -> content == contentType }
        }
    }
    fun containsContentType(contentType: String):Boolean{
        return _selectedContentTypes.value.contains(contentType)
    }


    fun analyzeImage(drawing: Bitmap, symbol: Word){
        drawingPair.value = Pair(drawing,symbol)
    }


    companion object {
        const val DEFAULT = 0
        const val CORRECT = 1
        const val WRONG = 2
        const val ERROR = 3
    }


}
