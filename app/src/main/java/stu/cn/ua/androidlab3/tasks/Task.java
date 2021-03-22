package stu.cn.ua.androidlab3.tasks;

public interface Task<T> {

    void execute(TaskListener<T> listener);

    void cancel();
}
