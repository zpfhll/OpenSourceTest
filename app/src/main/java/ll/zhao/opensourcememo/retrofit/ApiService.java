package ll.zhao.opensourcememo.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import ll.zhao.opensourcememo.retrofit.response.Ap0002;
import ll.zhao.opensourcememo.retrofit.response.Ap0016;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    String POST_PATH = "api/app/oauth2/token";

    @HTTP(method = "GET",path = "sug?code=utf-8",hasBody = false)
    Call<ResponseBody> search(@Query("q") String name );


    @POST(POST_PATH)
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=UTF-8")
    Observable<Response<Ap0002>> login(@Header("X-Device-Id") String deviceId);


    @HTTP(method = "PUT",path = "api/v1/ta_sender/banks/{bank_code}",hasBody = true)
    @FormUrlEncoded
    Observable<Response<Ap0016>> accountRefresh(@Path("bank_code") String code, @Field("receiveAccountFlg") String flg);



    @HTTP(method = "PUT",path = "naigai/api/v1/ta_sender/banks/{bank_code}",hasBody = true)
    @FormUrlEncoded
    Call<ResponseBody> accountRefresh(@Path("bank_code") int code, @FieldMap Map<String,String> data);



    @GET("api/v1/account/trade_validation?samplekey=value")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=UTF-8")
    Call<ResponseBody> check(@Header("X-Device-Id") String deviceId, @QueryMap Map<String,String> data);

}
