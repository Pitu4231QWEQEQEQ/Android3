package stu.cn.ua.androidlab3.contract;

public interface ResponseListener<T> {
    void onResults(T results);
}
