package com.example.hellokittyquiz

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hellokittyquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity";

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val cheatLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        //handle whatever the result is
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
        if (!quizViewModel.currentQuestionAnswer) {
            binding.trueButton.visibility = View.INVISIBLE;
        } else {
            binding.falseButton.visibility = View.INVISIBLE;
        }//end else
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        binding.trueButton.setOnClickListener{ view: View ->
            checkAnswer(true)
        }//end true button
        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }
        //onset listener for the next button, i.e. what happens if you press the next button
        binding.nextButton.setOnClickListener {
            //toggle to next question on click
            quizViewModel.moveToNext()
            updateQuestion()
            if (binding.trueButton.visibility == View.INVISIBLE) {
                binding.trueButton.visibility = View.VISIBLE
            } else if (binding.falseButton.visibility == View.INVISIBLE) {
                binding.falseButton.visibility = View.VISIBLE
            }
        }

        binding.cheatButton.setOnClickListener {
            val answer = quizViewModel.currentQuestionAnswer;
            quizViewModel.setCheatStatus()
            val intent = CheatActivity.newIntent(this@MainActivity, answer)
            cheatLauncher.launch(intent)
        }

        binding.questionTextView.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
       }
        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() is called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() is called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() is called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() is called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy is called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnswer:Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.ifCheated() -> R.string.judgement_string
            userAnswer == correctAnswer -> R.string.correct_string
            else -> R.string.incorrect_string
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}
