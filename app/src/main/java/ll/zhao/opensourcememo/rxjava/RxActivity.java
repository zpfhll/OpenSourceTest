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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ll.zhao.opensourcememo.Constrants;
import ll.zhao.opensourcememo.R;
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
    private Observable observable;
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
    public void login(View view){
        ApiAccess apiAccess = new ApiAccess();
        SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();
        Observable<Response<Ap0002>> observable = apiAccess.login();
        observable.compose(ResponseTransformer.handleResult())
                .compose(schedulerProvider.applySchedulers())
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
        Observable<Response<Ap0016>> observable2 = apiAccess.setRegistion();
        observable2.compose(ResponseTransformer.handleResult())
                .compose(schedulerProvider.applySchedulers())
                .subscribe(result -> {
                    Log.e(TAG, "result2:" +result.toString());
                    editText.setText(result.toString() + "--->throwable2");
                },throwable -> {
                    ApiServerError apiServerError = (ApiServerError)throwable;
                    Log.e(TAG, "throwable2:" +apiServerError.getCode());
                    Log.e(TAG, "throwable2:" +apiServerError.getMessage());
                    ResponseBody errorResponse = apiServerError.getErrorBody();
                    if(errorResponse != null){
                        Log.e(TAG, "throwable2:" +errorResponse);
                        editText.setText(errorResponse.string() + "--->throwable2");
                    }
                });
    }
}
