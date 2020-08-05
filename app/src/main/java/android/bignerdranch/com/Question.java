package android.bignerdranch.com;

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mHasCheated;

    public Question(int textResId, boolean answerTrue, boolean hasCheated) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mHasCheated = hasCheated;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }


    public boolean isHasCheated() {
        return mHasCheated;
    }

    public void setHasCheated(boolean hasCheated) {
        mHasCheated = hasCheated;
    }
}
