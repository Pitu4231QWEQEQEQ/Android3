package stu.cn.ua.androidlab3.tasks;

import android.os.Handler;
import android.os.Looper;


public abstract class BaseTask<T> implements Task<T> {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private TaskListener<T> taskListener;

    private boolean executed;
    private boolean canceled;

    @Override
    public void execute(TaskListener<T> listener) {
        if(executed) throw new RuntimeException("Task has benn already executed");
        if(canceled) return;
        executed = true;
        this.taskListener = listener;

        start();
    }

    @Override
    public void cancel() {
        if(!canceled){
            canceled = true;
            taskListener = null;
            onCanceled();
        }
    }

    protected final void publishSuccess(T result){
        runOnMAinThread(() -> {
            if(taskListener != null){
                taskListener.onSuccess(result);
                taskListener = null;
            }
        });
    }

    protected final void publishError(Throwable error){
        runOnMAinThread(() -> {
            if(taskListener != null){
                taskListener.onError(error);
                taskListener = null;
            }
        });
    }

    private void runOnMAinThread(Runnable action){
        if(Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()){
            action.run();
        } else {
            HANDLER.post(action);
        }
    }

    protected abstract void start();
    protected abstract void onCanceled();
}
