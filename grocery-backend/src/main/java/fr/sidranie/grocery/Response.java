package fr.sidranie.grocery;

public class Response<T> {

    private T data;

    private String message;

    public Response() {
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(String message) {
        this.message = message;
    }

    public Response(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
