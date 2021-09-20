package com.example.common.bo;

import lombok.Getter;

@Getter
public class BussinessBo<T> {

    private boolean status;
    private int code;
    private String message;
    private T data;

    public BussinessBo() {
        this(true);
    }

    public BussinessBo(boolean status) {
        this.status = status;
    }

    public BussinessBo(int code, String message) {
        this.code = code;
        this.message = message;
        this.status = false;
    }

    public BussinessBo(int code, String message, T data) {
        this.status = false;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BussinessBo(T data) {
        this.data = data;
        this.status = true;
    }

    public boolean status() {
        return status;
    }

    public static BussinessBo success() {
        return new BussinessBo();
    }

    public static BussinessBo error() {
        return new BussinessBo(false);
    }

    public static BussinessBo error(int code, String message) {
        return new BussinessBo(code, message);
    }

    public static <T> BussinessBo error(int code, String message, T data) {
        return new BussinessBo(code, message, data);
    }

    public static <T> BussinessBo<T> success(T data) {
        return new BussinessBo(data);
    }
}
