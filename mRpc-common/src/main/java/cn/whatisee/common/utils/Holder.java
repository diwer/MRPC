package cn.whatisee.common.utils;

/**
 * Created by ming on 2016/12/29.
 */
public class Holder<T> {
    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    private volatile T value;

}
