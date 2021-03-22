package stu.cn.ua.androidlab3.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import stu.cn.ua.androidlab3.R;

public class GenerateAnswerCallable implements Callable<String> {

    private String question;

    private List<String> answers = Arrays.asList(
            "Так",
            "Цілком вірогідно",
            "Можливо",
            "Не знаю",
            "Малоймовірно",
            "Ні");

    public GenerateAnswerCallable(String question){
        this.question = question;
    }

    @Override
    public String call() throws Exception {
        int variant = question.hashCode() % 6;
        variant = Math.abs(variant);
        Thread.sleep(3000);
        return answers.get(variant);
    }
}
