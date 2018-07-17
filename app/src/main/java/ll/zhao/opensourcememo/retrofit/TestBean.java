package ll.zhao.opensourcememo.retrofit;

import java.util.Map;

import ll.zhao.opensourcememo.retrofit.response.BaseResponse;

public class TestBean extends BaseResponse{

    private Map<String,String> result;
    private String tmall;


    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }

    public String getTmall() {
        return tmall;
    }

    public void setTmall(String tmall) {
        this.tmall = tmall;
    }

    @Override
    public String toString() {
        StringBuffer resultStr = new StringBuffer("");
        for (String key:result.keySet()) {
            resultStr.append(key);
            resultStr.append(":");
            resultStr.append(result.get(key));
            resultStr.append("\n");
        }
        resultStr.append("tmall:");
        resultStr.append(tmall);
        return resultStr.toString();
    }
}
