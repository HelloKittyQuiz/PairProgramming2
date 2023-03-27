package com.example.hellokittyquiz

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.hellokittyquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity";

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        result ->
        //handle whatever the result is
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
        val answer = quizViewModel.currentQuestionAnswer;
        var temp = answer;

        if (!temp) {
            binding.trueButton.visibility = View.INVISIBLE;
        } else {
            binding.falseButton.visibility = View.INVISIBLE;
        }//end else
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        // what happen if you click on those buttons
        binding.trueButton.setOnClickListener{ view: View ->
            // Do something if you click on true button
            // have a correct toast that pops up
            checkAnswer(true)
            //answered[current_index%question_bank.size] = true;
            //disableButtons()
        }//end true button

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            //answered[current_index%question_bank.size] = true;
            //disableButtons()
        }

        //onset listener for the next button, i.e. what happens if you press the next button
        binding.nextButton.setOnClickListener {
            //toggle to next question on click
            quizViewModel.moveToNext()
            /*if (!answered[current_index]){
                enableButtons()
            }*/
            updateQuestion()
            if (binding.trueButton.visibility == View.INVISIBLE){
                binding.trueButton.visibility = View.VISIBLE
            }
            else if (binding.falseButton.visibility == View.INVISIBLE){
                binding.falseButton.visibility = View.VISIBLE
            }
        }

        binding.cheatButton.setOnClickListener {
            val answer = quizViewModel.currentQuestionAnswer;
            quizViewModel.setCheatStatus()
            val intent = CheatActivity.newIntent(this@MainActivity, answer)
            cheatLauncher.launch(intent)
            //startActivity(intent)
            //start cheat activity
            //val intent = Intent(this, CheatActivity::class.java)
        }

        binding.questionTextView.setOnClickListener{
            //toggle to next question on click
            quizViewModel.moveToNext()
            /*if (!answered[current_index]){
                enableButtons()
            }*/
            updateQuestion()
        }

        binding.prevButton.setOnClickListener {
            //toggle to previous question on click
            quizViewModel.moveToPrev()
            /*if (!answered[current_index]){
                enableButtons()
            }*/
            updateQuestion()
       }
        //this will get you the id for the current question in the question array
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
        //Log.d(TAG, "" + quizViewModel.currentQuestionCheated)
        val messageResId = when {
            quizViewModel.ifCheated() -> R.string.judgement_string
            userAnswer == correctAnswer -> R.string.correct_string
            else -> R.string.incorrect_string
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        //Snackbar.make(findViewById(android.R.id.content), messageResId, BaseTransientBottomBar.LENGTH_SHORT).show()
        /*if (current_index == (question_bank.size-1)){
            val totalScore = score*100/ question_bank.size
            Toast.makeText(this, "${totalScore}%", Toast.LENGTH_SHORT).show()
        }*/
    }
    /*private fun disableButtons(){
        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false
    }
    private fun enableButtons(){
        binding.trueButton.isEnabled = true
        binding.falseButton.isEnabled = true
    }*/
}
