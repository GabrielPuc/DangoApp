package com.gdbm.dangoapp.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdbm.dangoapp.managers.ContentManager
import com.gdbm.dangoapp.model.JapaneseWord
import com.google.mlkit.vision.text.TextRecognizer

class ContentTrainingViewModel : ViewModel() {

    val selectedWords = mutableListOf<JapaneseWord>()
    private val previousWord = ArrayDeque(listOf<JapaneseWord>())
    private lateinit var currentSymbol:JapaneseWord
    private val selectedGroups = mutableListOf<String>()
    private val selectedMainGroups = mutableListOf<String>()
    private lateinit var textRecognizer : TextRecognizer
    lateinit var contentManager:ContentManager
    val drawingPair = MutableLiveData<Pair<Bitmap,JapaneseWord>>()

    fun setCurrentContentManager(contentManager: ContentManager){
        this.contentManager = contentManager
    }

    fun setDefault(){
        selectedGroups.clear()
        selectedGroups.add("VOCALS")
        selectedMainGroups.add("Hiragana")
    }

    fun setDefaultSingle(){
        selectedGroups.clear()
        contentManager.getHiraganaGroups().iterator().forEach {
            selectedGroups.add(it.key)
        }
    }

    fun setDefaultGeneral(){
        selectedMainGroups.clear()
        selectedMainGroups.add("Hiragana")
    }

    fun setDefaultVocabulary(){
        selectedMainGroups.clear()
        selectedMainGroups.add("Vocabulary")
    }

    fun setWords(){
        selectedWords.clear()
        selectedGroups.iterator().forEach {
            selectedWords.addAll(contentManager.get(it))
        }
    }


    fun setGeneralWords(){
        selectedWords.clear()
        selectedMainGroups.iterator().forEach {
            when(it){
                "Hiragana" -> {selectedWords.addAll(contentManager.getHiraganaGroups().values.flatten())}
                "Katakana" -> {selectedWords.addAll(contentManager.getKatakanaGroups().values.flatten())}
            }
            //selectedWords.addAll(contentManager.get(it))
        }
    }

    fun setVocabulary(){
        selectedWords.clear()
        selectedWords.addAll(contentManager.getVocabularyGroups().values.flatten())
    }

    fun getRandomWord() : JapaneseWord {
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

    fun getRandomWordWithOptions() : List<JapaneseWord> {
        return selectedWords.asSequence().shuffled().take(4).toList().shuffled()
    }

    fun handleSelection(correct:String, selected:String):Boolean{
        if(correct == selected){
            return true
        }else{
            return false
        }
    }

    fun addToSelected(group: String){
        selectedGroups.add(group)
    }

    fun removeFromSelected(group: String){
        selectedGroups.remove(group)
    }

    fun addGeneralToSelected(group: String){
        selectedMainGroups.add(group)
    }

    fun removeGeneralFromSelected(group: String){
        selectedMainGroups.remove(group)
    }

    fun containsGroup(group: String):Boolean{
        return selectedGroups.contains(group)
    }

    fun containsMainGroup(group: String):Boolean{
        return selectedMainGroups.contains(group)
    }

    fun analyzeImage(drawing: Bitmap, symbol: JapaneseWord){
        drawingPair.value = Pair(drawing,symbol)
    }


    companion object {
        const val DEFAULT = 0
        const val CORRECT = 1
        const val WRONG = 2
        const val ERROR = 3
    }


}
