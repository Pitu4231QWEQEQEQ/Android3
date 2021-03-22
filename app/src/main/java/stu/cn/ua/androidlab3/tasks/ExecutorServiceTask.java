package stu.cn.ua.androidlab3.tasks;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceTask<T> extends BaseTask<T> {

    private static final ExecutorService SERVICE = Executors.newCachedThreadPool();

    private Callable<T> callable;
    private Future<?> future;

    public ExecutorServiceTask(Callable<T> callable){
        this.callable = callable;
    }

    @Override
    protected void start() {
       future = SERVICE.submit(() -> {
           try {
               T result = callable.call();
               publishSuccess(result);
           } catch (Exception e){

               publishError(e);
           }
       });
    }

    @Override
    protected void onCanceled() {
        if(future != null){
            future.cancel(true);
            future = null;
        }
    }
}
