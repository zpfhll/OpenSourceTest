package ll.zhao.opensourcememo.retrofit;

import okhttp3.ResponseBody;

public class ApiServerError extends Exception{
    private int code;
    private String tag;
    private String displayMessage;
    private ResponseBody errorBody;

    public ApiServerError(String tag,int code, String displayMessage,ResponseBody errorBody) {
        this.code = code;
        this.tag = tag;
        this.displayMessage = displayMessage;
        this.errorBody = errorBody;
    }

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
