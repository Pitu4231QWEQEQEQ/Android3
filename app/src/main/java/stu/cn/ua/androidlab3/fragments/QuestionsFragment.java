package stu.cn.ua.androidlab3.fragments;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import stu.cn.ua.androidlab3.QuestionViewState;
import stu.cn.ua.androidlab3.R;
import stu.cn.ua.androidlab3.model.Player;
import stu.cn.ua.androidlab3.service.QuestionsService;
import stu.cn.ua.androidlab3.tasks.Task;
import stu.cn.ua.androidlab3.tasks.TaskListener;

public class QuestionsFragment extends BaseFragment implements RetainFragment.QuestionStateListener {
    private static final String TAG =
            QuestionsFragment.class.getSimpleName();
    private static final String ARG_PLAYER = "PLAYER";

    private Player player;
    private View view;

    private Button questionButton;
    private ProgressBar progress;
    private EditText questionEditText;

    private RetainFragment retainFragment;

    public static QuestionsFragment newInstance(Player player) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PLAYER, player);
        QuestionsFragment fragment = new QuestionsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questions,
                container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        questionButton = view.findViewById(R.id.questionButton);
        questionEditText = view.findViewById(R.id.questionEditText);
        progress = view.findViewById(R.id.progress);

        retainFragment = (RetainFragment) getActivity().getSupportFragmentManager().findFragmentByTag(RetainFragment.TAG);
        if (retainFragment == null) {
            retainFragment = new RetainFragment();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(retainFragment, RetainFragment.TAG)
                    .commit();
        }

        retainFragment.setListener(this);

        player = getPlayer();
        setupButtons(view);
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.cancelButton)
                .setOnClickListener(v -> {

                    getAppContract().cancel();
                });

        questionButton.findViewById(R.id.questionButton).setOnClickListener(v -> {
            String question = questionEditText.getText()+ " " + player.toString();
            retainFragment.getAnswer(question);
        });
    }

    private Player getPlayer() {
        return getArguments().getParcelable(ARG_PLAYER);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        retainFragment.setListener(null);
    }

    @Override
    public void onNewState(QuestionViewState state) {
        questionButton.setEnabled(state.enabledAnswerButton);
        questionEditText.setEnabled(state.enabledQuestionTextView);
        progress.setVisibility(state.showProgress ? View.VISIBLE : View.GONE);
        if(!state.result.equals("")){
            Toast.makeText(view.getContext(), state.result, Toast.LENGTH_SHORT).show();
        }
    }
}