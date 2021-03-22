package stu.cn.ua.androidlab3.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import stu.cn.ua.androidlab3.R;
import stu.cn.ua.androidlab3.fragments.QuestionsFragment;

public class QuestionsService extends Service {

    private TestBinder binder = new TestBinder();
    private List<String> answers;

    private String question;


    @Override
    public void onCreate() {
        super.onCreate();
        answers = Arrays.asList(getString(R.string.yes),
                getString(R.string.likely),
                getString(R.string.may_be),
                getString(R.string.dont_kown),
                getString(R.string.unlikely),
                getString(R.string.no));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder ;
    }

    public String getAnswer(String question, QuestionsFragment questionsFragment) {
        int variant = question.hashCode() % 6;
        variant = Math.abs(variant);
        return answers.get(variant);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class TestBinder extends Binder{

        public QuestionsService getService(){
            return QuestionsService.this;
        }
    }
}
