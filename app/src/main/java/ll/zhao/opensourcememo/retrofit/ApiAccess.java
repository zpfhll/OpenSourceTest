package ll.zhao.opensourcememo.retrofit;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import ll.zhao.opensourcememo.retrofit.response.Ap0002;
import ll.zhao.opensourcememo.retrofit.response.Ap0016;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiAccess {

    public static Retrofit retrofit;
    public static final int TIME_OUT = 5;

    public ApiAccess(){
        if(null == retrofit){

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
                Log.e("ApiAccess", "---->" +message);
            });
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(logging).connectTimeout(TIME_OUT, TimeUnit.SECONDS);
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://suggest.taobao.com/")
                    .client(builder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public void search(String name, Callback<ResponseBody> callback){
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> call = apiService.search(name);
        call.enqueue(callback);
    }

    public Response<ResponseBody> search(String name){
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> call = apiService.search(name);
        try {
            return call.execute();
        } catch (IOException e) {
            return null;
        }
    }

    public Observable<Response<TestBean>> searchTest(String content){
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<Response<TestBean>> observable = apiService.searchTest(content);
        return observable;
    }


    public Observable<Response<Ap0002>> login(){
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<Response<Ap0002>> observable = apiService.login("testd");
        return observable;
    }

    public Observable<Response<Ap0016>> setRegistion(){
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<Response<Ap0016>> observable = apiService.accountRefresh("005","1");
        return observable;
    }

}
