package stu.cn.ua.androidlab3.fragments;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import stu.cn.ua.androidlab3.QuestionViewState;
import stu.cn.ua.androidlab3.tasks.ExecutorServiceTask;
import stu.cn.ua.androidlab3.tasks.GenerateAnswerCallable;
import stu.cn.ua.androidlab3.tasks.Task;
import stu.cn.ua.androidlab3.tasks.TaskListener;
import ua.cn.stu.getvariant.RetainManager;

public class RetainFragment extends Fragment {

    public static final String TAG = RetainFragment.class.getSimpleName();

    private Task<String> currentTask;
    private QuestionStateListener listener;
    private QuestionViewState questionViewState = new QuestionViewState();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setListener(QuestionStateListener listener){
        this.listener = listener;
        if(listener != null){
            listener.onNewState(questionViewState);
        }
    }

    public void getAnswer(String question){
        currentTask = createAnswerTask(question);

        questionViewState.enabledAnswerButton = false;
        questionViewState.enabledQuestionTextView = false;
        questionViewState.showProgress = true;
        questionViewState.result = "";
        updateState();

        currentTask.execute(new TaskListener<String>() {
            @Override
            public void onSuccess(String result) {
                questionViewState.enabledAnswerButton = true;
                questionViewState.enabledQuestionTextView = true;
                questionViewState.showProgress = false;
                questionViewState.result = result;
                updateState();
            }

            @Override
            public void onError(Throwable error) {
                questionViewState.enabledAnswerButton = true;
                questionViewState.enabledQuestionTextView = true;
                questionViewState.showProgress = false;
                questionViewState.result = "";
                updateState();

                Log.e(TAG, "Error!", error);
            }
        });
    }

    private void updateState() {
        if(listener != null) {
            listener.onNewState(questionViewState);
        }
    }

    private Task<String> createAnswerTask(String question){
        return new ExecutorServiceTask<>(new GenerateAnswerCallable(question));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(currentTask != null){
            currentTask.cancel();
        }
    }

    public interface QuestionStateListener {
        void onNewState(QuestionViewState state);
    }
}
