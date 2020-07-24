package android.bignerdranch.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "android.bignerdranch.com.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "android.bignerdranch.com.geoquiz.answer_is_shown";
    //Rotation save state
    //Create a constant that will be the key for the key-value pair that will be stored in the bundle
    private static final String KEY_IS_ANSWER_SHOWN = "isCheater";

    //Create a value to log the activity lifecycle
    private static final String TAG = "CheatActivity";


    private boolean mIsAnswerShown;
    private boolean mAnswerIsTrue; //value returned from the extra in onCreate(Bundle) is stored here


    private TextView mAnswerTextView;
    private Button mShowAnswerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IS_ANSWER_SHOWN)) {
                mIsAnswerShown = savedInstanceState.getBoolean(KEY_IS_ANSWER_SHOWN);
            }
        }

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
                mIsAnswerShown = true;
                setAnswerShownResult(mIsAnswerShown);
                mShowAnswerButton.setEnabled(false);
            }
        });

        if (mIsAnswerShown) {
            showAnswer();
            mShowAnswerButton.setEnabled(false);
            setAnswerShownResult(mIsAnswerShown);
        }
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        mIsAnswerShown = isAnswerShown;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mIsAnswerShown);
        setResult(RESULT_OK, data);
    }


    /**
     * Method to save the state of mAnswerIsTrue through phone rotation states
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "cheatOnSaveInstanceState");
        savedInstanceState.putBoolean(KEY_IS_ANSWER_SHOWN, mIsAnswerShown);
    }

    /*
     * Display the answer
     */
    private void showAnswer() {
        if(mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
    }

}