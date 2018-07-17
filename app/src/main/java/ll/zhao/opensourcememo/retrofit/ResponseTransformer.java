package ll.zhao.opensourcememo.retrofit;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import ll.zhao.opensourcememo.retrofit.response.BaseResponse;
import retrofit2.Response;

public class ResponseTransformer {

    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;

    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;

    /**
     * 协议错误
     */
    public static final int HTTP_ERROR = 1003;

    public static <T> ObservableTransformer<? super Response<? extends Serializable>, ? extends Serializable> handleResult() {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<T>())
                .flatMap(new ResponseFunction<T>());
    }


    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends Response<? extends Serializable>>> {

        @Override
        public ObservableSource<? extends Response<? extends Serializable>> apply(Throwable e){
            ApiServerError ex;
            if (e instanceof JsonParseException
                    || e instanceof JSONException
                    || e instanceof ParseException) {
                //解析错误
                ex = new ApiServerError(PARSE_ERROR, e.getMessage(),null);
            } else if (e instanceof ConnectException) {
                //网络错误
                ex = new ApiServerError(NETWORK_ERROR, e.getMessage(),null);
            } else if (e instanceof UnknownHostException || e instanceof SocketTimeoutException|| e instanceof TimeoutException) {
                //连接错误
                ex = new ApiServerError(NETWORK_ERROR, e.getMessage(),null);
            } else {
                //未知错误
                ex = new ApiServerError(UNKNOWN, e.getMessage(),null);
            }
            return Observable.error(ex);
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<Response<? extends Serializable>, ObservableSource<? extends Serializable>> {

        @Override
        public ObservableSource<? extends Serializable> apply(Response<? extends Serializable> tResponse){
            int code = tResponse.code();
            String message = tResponse.message();
            if (code == 200) {
                return Observable.just(tResponse.body());
            } else {
                return Observable.error(new ApiServerError(code,message,tResponse.errorBody()));
            }
        }
    }

}
