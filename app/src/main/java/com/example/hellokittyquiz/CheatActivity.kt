package com.example.hellokittyquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hellokittyquiz.databinding.ActivityCheatBinding

private const val TAG = "CheatActivity";
private const val EXTRA_ANSWER_IS_TRUE = "com.example.hellokittyquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.hellokittyquiz.answer_shown"

class CheatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheatBinding
    private var isAnswerCorrect = false //initialize to false to start

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isAnswerCorrect = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        binding.showAnswerButton.setOnClickListener {
            //once they clicked the show answer button, enable cheating
            val answerText = when{
                isAnswerCorrect -> R.string.true_string
                else -> R.string.false_string
            }
            binding.answerText.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object{
        fun newIntent(packageContext: Context, isAnswerCorrect: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerCorrect)
            }
        }
    }
}