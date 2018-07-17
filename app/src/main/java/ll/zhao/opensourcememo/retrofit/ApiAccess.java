package ll.zhao.opensourcememo.retrofit;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
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
    public static final String TAG = "------>";
    public ApiAccess(){
        if(null == retrofit){

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
                Log.e("ApiAccess", "---->" +message);
            });
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(logging).connectTimeout(TIME_OUT, TimeUnit.SECONDS);
//            retrofit = new Retrofit.Builder()
//                    .baseUrl("https://suggest.taobao.com/")
//                    .baseUrl("http://172.23.17.155:8080/naigai/")
//                    .client(builder.build())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();

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

    @SuppressLint("CheckResult")
    public SelfObservable searchTest(String content,String tag){
        ApiService apiService = retrofit.create(ApiService.class);
        SelfObservable selfObservable = new SelfObservable(tag,apiService.searchTest(content));
        return selfObservable;
    }


    public Observable<Response<Ap0002>> login(String deviceID){
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<Response<Ap0002>> observable = apiService.login(deviceID);
        return observable;
    }

    public Observable<Response<Ap0016>> setRegistion(String baankCode){
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<Response<Ap0016>> observable = apiService.accountRefresh(baankCode,"1");
        return observable;
    }

    @SuppressLint("CheckResult")
    public void sendApiAsynTest(List<SelfObservable> ls,RetrofitCallBack callBack){
        List<Object> resultList = new ArrayList<>();
        SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();
        for (SelfObservable selfObservable:ls){
            selfObservable.getObservable().compose(schedulerProvider.applySchedulers())
                    .compose(new ResponseTransformer().handleResult(selfObservable.getTag()))
                    .subscribe(result -> {
                        resultList.add(result);
                        Log.e(TAG, "result:" + result.toString());
                        if(resultList.size() == ls.size()){
                            callBack.result(resultList);
                        }
                    }, throwable -> {
                        resultList.add(throwable);
                        Log.e(TAG, "throwable:" + throwable.toString());
                        if(resultList.size() == ls.size()){
                            callBack.result(resultList);
                        }
                    });
        }
    }


    @SuppressLint("CheckResult")
    public void sendApiArrayTest(List<SelfObservable> ls,RetrofitCallBack callBack){
        SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();
        List<Object> resultList = new ArrayList<>();
        Observable.fromIterable(ls)
                .concatMap(
                        request -> {
                            return request.getObservable()
                                    .compose(schedulerProvider.applySchedulers())
                                    .compose(new ResponseTransformer().handleResult(request.getTag()));
                        }
                ).subscribe(
                        result -> {
                            resultList.add(result);
                            if(resultList.size() == ls.size()) {
                                callBack.result(resultList);
                            }
                        }, throwable -> {
                            resultList.add(throwable);
                            callBack.result(resultList);
                        }
                );
    }


    @SuppressLint("CheckResult")
    public void sendApiArray(List<Observable> ls,RetrofitCallBack callBack){
        SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();
        List<Object> resultList = new ArrayList<>();
        Observable.fromIterable(ls)
                .concatMap(
                        request -> request
                        )
                .compose(schedulerProvider.applySchedulers())
                .compose(new ResponseTransformer().handleResult())
                .subscribe(
                result -> {
                    resultList.add(result);
                    if(resultList.size() == ls.size()) {
                        callBack.result(resultList);
                    }
                }, throwable -> {
                    resultList.add(throwable);
                    if(resultList.size() == ls.size()){
                        callBack.result(resultList);
                    }
                }
        );
    }

    @SuppressLint("CheckResult")
    public Observable sendApiArray(List<Observable> ls){
        SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();
        List<Object> resultList = new ArrayList<>();
        Observable observable = Observable.fromIterable(ls).concatMap(request -> request)
                .compose(schedulerProvider.applySchedulers())
                .compose(new ResponseTransformer().handleResult());

        return observable;

    }

    public Observable sendSingleApi(Observable api){
        SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();
        return api.compose(schedulerProvider.applySchedulers())
                .compose(new ResponseTransformer().handleResult());
    }

    @SuppressLint("CheckResult")
    public void sendApiAsyn(List<Observable> ls,RetrofitCallBack callBack){
        List<Object> resultList = new ArrayList<>();
        for (Observable observable:ls){
            sendSingleApi(observable)
                    .subscribe(result -> {
                resultList.add(result);
                Log.e(TAG, "result:" + result.toString());
                if(resultList.size() == ls.size()){
                    callBack.result(resultList);
                }
            }, throwable -> {
                resultList.add(throwable);
                Log.e(TAG, "throwable:" + throwable.toString());
                if(resultList.size() == ls.size()){
                    callBack.result(resultList);
                }
            });
        }
    }

}
