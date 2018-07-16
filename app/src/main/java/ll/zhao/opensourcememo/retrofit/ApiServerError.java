package ll.zhao.opensourcememo.retrofit;

import okhttp3.ResponseBody;

public class ApiServerError extends Exception{
    private int code;
    private String displayMessage;
    private ResponseBody errorBody;

    public ApiServerError(int code, String displayMessage,ResponseBody errorBody) {
        this.code = code;
        this.displayMessage = displayMessage;
        this.errorBody = errorBody;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public ResponseBody getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(ResponseBody errorBody) {
        this.errorBody = errorBody;
    }
}
