package android.bignerdranch.com;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton; // button that leads to the cheat activity
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;

    //Create a value to log the activity lifecycle
    private static final String TAG = "QuizActivity";

    //Create a constant that is the key that will be stored in the save instance bundle
    private static final String KEY_INDEX = "index";

    //Create a constant that is they key that will be stored for isCheater
    private static final String KEY_CHEAT = "isCheater";

    //Create a request code to be sent to the Cheat activity
    private static final int REQUEST_CODE_CHEAT = 0;

    //Create a new variable to hold the value that CheatActivity is passing back
    private boolean mIsCheater;

    //call new instances of the Question Class to fill the question bank
    //mQuestionBank[Index] becomes the instance of the question class.
    //So when you call mQuestionBank[index].isAnswerTrue() it moves to the Question class, grabs the method and returns the answer set in these questions.
    private  Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_ocean, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;
    private int mNumQuestionsAnswered = 0;
    private int mNumCorrect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            //Grabs the index from the savedInstanceState for KEY_INDEX when rotation occurs
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            //Grabs the boolean value from the savedInstanceState for KEY_CHEAT when rotation occurs
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEAT, false);
        }

        /*
        Cast mQuestionTextView to a TextView
         */
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Something worth remembering is 6 % mQuestionBank.length = 0 and resets the counter!
                //If the length of the question bank increases, then it should work naturally
                mCurrentIndex = (mCurrentIndex + 1)  % mQuestionBank.length;
                updateQuestion();
            }
        });

        /*
         cast trueButton to a button
         set the button to its ID
         create a listener for the widget
         */
        mTrueButton = (Button) findViewById(R.id.true_button);
//        mTrueButton = (Button)findViewById(R.id.question_text_view);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //this is the only mandatory method of the onClickListener interface!

//                Toast toast = Toast.makeText(QuizActivity.this,
//                        R.string.correct_toast,
//                        Toast.LENGTH_SHORT);
////                        toast.setGravity(Gravity.TOP, 0, 0);
//                        toast.show();
                checkAnswer(true);
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }

        });

        /*
         cast falseButton to a button
         set the button to its ID
         create a listener for the widget
         */
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast toast = Toast.makeText(QuizActivity.this,
//                        R.string.incorrect_toast,
//                        Toast.LENGTH_SHORT);
////                        toast.setGravity(Gravity.TOP, 0, 0);
//                        toast.show();
                checkAnswer(false);
                mFalseButton.setEnabled(false);
                mTrueButton.setEnabled(false);
            }
        });

        /*
         cast cheatButton to a button
         set the button to its ID
         create a listener for the widget
         Designed to move the user onto a the cheat activity
         */
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start cheat activity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue(); //this method comes from the question class.

                //An intent is an object that a component can use to communicate with the OS.
                //An activity is a component (but there are also services, broadcast receivers, and content providers)
                //Use the intent to tell the ActivityManager which activity to start
                //This is an explicit intent
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);

                //startActivity is a call onto the OS, not the activity itself through the ActivityManager.
                //a result code is always returned to the parent if the child activity was started with startActivityForResult(...)
                startActivityForResult(intent, REQUEST_CODE_CHEAT); //REQUEST_CODE_CHEAT BY DEFAULT IS 0
            }
        });

        /*
         cast member  nextButton to a button
         set the button to its ID
         create a listener for the widget
         when pressed, call updateQuestion method
         Re-enables buttons if needed
         */
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFalseButton.setEnabled(true);
                mTrueButton.setEnabled(true);
                mCurrentIndex = (mCurrentIndex + 1)  % mQuestionBank.length; //Something worth remembering is 6 % mQuestionBank.length = 0 and resets the counter!
                mIsCheater = false;
//                System.out.println(mCurrentIndex);
                updateQuestion();
            }
        });


        /*
         cast member falseButton to a button
         set the button to its ID
         create a listener for the widget
         when pressed, call updateQuestion method
         Re-enables buttons if needed
         */
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFalseButton.setEnabled(true);
                mTrueButton.setEnabled(true);

                if (mCurrentIndex == 0) {
                    mCurrentIndex = 5;
                } else {
                    mCurrentIndex = mCurrentIndex - 1;
                    mIsCheater = false;
                }
                updateQuestion();
            }
        });

        updateQuestion();
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    /*
    Overrides saveInstanceState  to write the value of mCurrentIndex to the bundle with
    the constant as its key
    Utilised in onCreate(Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex); // KEY_INDEX = "index"
        savedInstanceState.putBoolean(KEY_CHEAT, mIsCheater); //KEY_CHEAT = "isCheater"
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    /*
     Checks index of mCurrentIndex and grabs the next array value
     and sets the text to the value of that question
     */
    private void updateQuestion() {
//        Log.d(TAG, "Updating Question Text", new Exception());

        if (mNumQuestionsAnswered != 6) {
            int question = mQuestionBank[mCurrentIndex].getTextResId();
            mQuestionTextView.setText(question);

        } else {
//            Context context = getApplicationContext();
            double getPercentage = ((double)mNumCorrect/ mNumQuestionsAnswered) * 100;
            DecimalFormat numberFormat = new DecimalFormat("#");
            String percentage = numberFormat.format(getPercentage);
            Toast.makeText(this, percentage + "% correct", Toast.LENGTH_SHORT).show();
        }
    }


    /*
     Accepts a boolean variable that identifies if the user pressed TRUE or FALSE
     against the answer in the current Question object.
     Creates a toast that displays the appropriate message
     */
    private void checkAnswer(boolean userPressedTrue) {

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue(); //this method comes from the question class.
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mNumQuestionsAnswered++;
                mNumCorrect++;
            } else {
                messageResId = R.string.incorrect_toast;
                mNumQuestionsAnswered++;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }


}