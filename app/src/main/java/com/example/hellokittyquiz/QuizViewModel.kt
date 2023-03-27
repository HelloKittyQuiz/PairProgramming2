package com.example.hellokittyquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    init {
        Log.d(TAG, "View Model instance created")
    }
    private val questionBank = listOf(Questions(R.string.question1, true, false),
        Questions(R.string.question2, false, false),
        Questions(R.string.question3, false, false),
        Questions(R.string.question4, false, false),
        Questions(R.string.question5, true, false)
    )
    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)
    var isCheater: Boolean
        get() = savedStateHandle[IS_CHEATER_KEY] ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    fun ifCheated()
    : Boolean {
        return questionBank[currentIndex].cheated
    }

    fun setCheatStatus() {
        questionBank[currentIndex].cheated = true;
    }

    fun moveToNext() {
        Log.d(TAG, "Updating question text", Exception())
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev(){
        currentIndex = (currentIndex - 1) % questionBank.size
    }
}