package ll.zhao.opensourcememo.rxjava;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ll.zhao.opensourcememo.Constrants;
import ll.zhao.opensourcememo.R;
import ll.zhao.opensourcememo.retrofit.request.Ap0002Request;
import ll.zhao.opensourcememo.retrofit.request.Ap0016Request;
import ll.zhao.opensourcememo.retrofit.response.Ap0002;
import ll.zhao.opensourcememo.retrofit.ApiAccess;
import ll.zhao.opensourcememo.retrofit.ApiServerError;
import ll.zhao.opensourcememo.retrofit.ResponseTransformer;
import ll.zhao.opensourcememo.retrofit.SchedulerProvider;
import ll.zhao.opensourcememo.retrofit.TestBean;
import ll.zhao.opensourcememo.retrofit.response.Ap0016;
import okhttp3.ResponseBody;
import retrofit2.Response;

@Route(path = Constrants.RXJAVA)
public class RxActivity extends Activity {
    public static final String TAG = "------>";
    private Observable<Response<ResponseBody>> observable;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);
        observable = createObservable();
        editText = findViewById(R.id.editText);
    }


    private Observable<Response<ResponseBody>> createObservable(){
        return Observable.create(new ObservableOnSubscribe<Response<ResponseBody>>() {
            @Override
            public void subscribe(ObservableEmitter<Response<ResponseBody>> emitter){

                Log.e(TAG, "Observable emit 1" + "\n");
                Response<ResponseBody> response =  new ApiAccess().search("风扇");
                emitter.onNext(response);
            }
        });
    }


    public void start(View view){
        observable.subscribe(new Observer() {

            private int i = 1;
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, "onSubscribe : " + d.isDisposed() + "\n" );
                mDisposable = d;
            }

            @Override
            public void onNext(Object o) {
                Log.e(TAG, "onNext : value : " + o.toString() + "\n" );
                i++;
                if (i == 2) {
                     //在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                    Log.e(TAG, "onNext : isDisposable : " + mDisposable.isDisposed() + "\n");
                }
                Response<ResponseBody> response = (Response<ResponseBody>)o;
                if(response.code() == 200){
                    try {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        TestBean testBean = gson.fromJson(result,TestBean.class);
                        Log.e(TAG, "onNext : testBean : " + testBean.toString() + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError : " + e.toString()+ "\n");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete" + "\n" );
            }
        });
    }

    @SuppressLint("CheckResult")
    public void searchTest(View view){
        ApiAccess apiAccess = new ApiAccess();
        SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();
        Observable.fromArray("窗帘","袜子","鞋").concatMap(searchKey ->{
            Observable<Response<TestBean>> observable = apiAccess.searchTest(searchKey);
            return  observable;
        }).compose(schedulerProvider.applySchedulers())
        .compose(ResponseTransformer.handleResult())
        .subscribe(result -> {
            Log.e(TAG, "result:" +result.toString());
            editText.setText(result.toString());
        },throwable -> {
            ApiServerError apiServerError = (ApiServerError)throwable;
                    Log.e(TAG, "throwable:" +apiServerError.getCode());
                    Log.e(TAG, "throwable:" +apiServerError.getMessage());
                    ResponseBody errorResponse = apiServerError.getErrorBody();
                    if(errorResponse != null){
                        Log.e(TAG, "throwable:" +errorResponse);
                        editText.setText(errorResponse.string());
                    }
        });
    }



    @SuppressLint("CheckResult")
    public void login(View view){
        ApiAccess apiAccess = new ApiAccess();
        Ap0002Request ap0002Request = new Ap0002Request("testD");
        Observable<Response<Ap0002>> ap0002 = apiAccess.login(ap0002Request.getDeviceID());
        Ap0016Request ap0016Request = new Ap0016Request("0005");
        Observable<Response<Ap0016>> ap0016 = apiAccess.setRegistion(ap0016Request.getBankCode());
        List<Observable> ls = new ArrayList<>();
        ls.add(ap0002);
        ls.add(ap0016);

//        apiAccess.sendSingleApi(ap0002).subscribe(result -> {
//            if (result instanceof Ap0002) {
//                Log.e(TAG, "Ap0002 result:" + result.toString());
//            } else if (result instanceof Ap0016) {
//                Log.e(TAG, "Ap0016 result:" + result.toString());
//            }
//            editText.setText(result.toString());
//        }, throwable -> {
//            ApiServerError apiServerError = (ApiServerError) throwable;
//            Log.e(TAG, "throwable:" + apiServerError.getCode());
//            Log.e(TAG, "throwable:" + apiServerError.getMessage());
//            ResponseBody errorResponse = apiServerError.getErrorBody();
//            if (errorResponse != null) {
//                Log.e(TAG, "throwable:" + errorResponse);
//                editText.setText(errorResponse.string());
//            }
//        });


//        apiAccess.sendApiArray(ls,result -> {
//            Log.e(TAG, "sendApiArray callback:");
//            for (Object object:result){
//                if (object instanceof Ap0002) {
//                    Log.e(TAG, "Ap0002 result2:" + result.toString());
//                } else if (object instanceof Ap0016) {
//                    Log.e(TAG, "Ap0016 result2:" + result.toString());
//                }else if(object instanceof ApiServerError){
//                    ApiServerError apiServerError = (ApiServerError) object;
//                    Log.e(TAG, "throwable2:" + apiServerError.getCode());
//                    Log.e(TAG, "throwable2:" + apiServerError.getMessage());
//                    ResponseBody errorResponse = apiServerError.getErrorBody();
//                    if (errorResponse != null) {
//                        Log.e(TAG, "throwable2:" + errorResponse);
//                        try {
//                            editText.setText(errorResponse.string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });


//        apiAccess.sendApiArray(ls).subscribe(result -> {
//            Log.e(TAG, "sendApiArray nocallback:");
//            if (result instanceof Ap0002) {
//                Log.e(TAG, "Ap0002 result2:" + result.toString());
//            } else if (result instanceof Ap0016) {
//                Log.e(TAG, "Ap0016 result2:" + result.toString());
//            }
//        },throwable -> {
//            ApiServerError apiServerError = (ApiServerError) throwable;
//            Log.e(TAG, "throwable2:" + apiServerError.getCode());
//            Log.e(TAG, "throwable2:" + apiServerError.getMessage());
//            ResponseBody errorResponse = apiServerError.getErrorBody();
//            if (errorResponse != null) {
//                Log.e(TAG, "throwable2:" + errorResponse);
//                try {
//                    editText.setText(errorResponse.string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });



        apiAccess.sendApiAsyn(ls,result -> {
            Log.e(TAG, "sendApiAsyn callback:");
            for (Object object:result){
                if (object instanceof Ap0002) {
                    Log.e(TAG, "Ap0002 result2:" + result.toString());
                } else if (object instanceof Ap0016) {
                    Log.e(TAG, "Ap0016 result2:" + result.toString());
                }else if(object instanceof ApiServerError){
                    ApiServerError apiServerError = (ApiServerError) object;
                    Log.e(TAG, "throwable2:" + apiServerError.getCode());
                    Log.e(TAG, "throwable2:" + apiServerError.getMessage());
                    ResponseBody errorResponse = apiServerError.getErrorBody();
                    if (errorResponse != null) {
                        Log.e(TAG, "throwable2:" + errorResponse);
                        try {
                            editText.setText(errorResponse.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        });

    }
}
