package com.android.samplequiz.model;

import javax.inject.Inject;

public class DataWrapper<T> {

    private T data;
    private int code;

    @Inject
    public DataWrapper() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "DataWrapper{" +
                "data=" + data +
                ", code=" + code +
                '}';
    }
}
