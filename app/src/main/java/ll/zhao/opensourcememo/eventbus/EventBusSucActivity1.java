package ll.zhao.opensourcememo.eventbus;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import ll.zhao.opensourcememo.BaseActivity;
import ll.zhao.opensourcememo.Constrants;
import ll.zhao.opensourcememo.R;
import ll.zhao.opensourcememo.retrofit.ApiAccess;
import okhttp3.ResponseBody;
import retrofit2.Response;

@Route(path = Constrants.EVENT_BUS_SUB1)
public class EventBusSucActivity1 extends BaseActivity {
    private TextView textView;
    private String TAG = "------>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event_bus_suc1);
        textView = findViewById(R.id.textView2);
        super.onCreate(savedInstanceState);
    }


    public void back(View view){
//        moveTo(Constrants.EVENT_BUS,view);
        EventBus.getDefault().post("鞋");
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void reciveEvent(EventBusEntity eventBusEntity){
        if(eventBusEntity.getId().endsWith(EventBusEntity.EVENT_SUB_ACTIVITY1)){
            textView.setText(eventBusEntity.getMessage());
        }
        if(eventBusEntity.getId().endsWith(EventBusEntity.EVENT_ACTIVITY)){
            textView.setText("this not sub！");
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent1(String text) {
//        Log.d(TAG, "onMessageEvent1 : String");
//    }
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void onMessageEvent2(String text) {
//        Log.d(TAG, "onMessageEvent2 : String" );
//    }
//
//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onMessageEvent3(String text) {
//        Log.d(TAG, "onMessageEvent3 : String" );
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent4(String text) {
        Log.d(TAG, "onMessageEvent4 : String" );
        ApiAccess apiAccess = new ApiAccess();
        Response<ResponseBody> response =  apiAccess.search(text);
        Log.d(TAG, "onMessageEvent4 : "+response );
        try {
            EventBusEntity eventBusEntity = new EventBusEntity(EventBusEntity.EVENT_SUB_ACTIVITY1,response.body().string());
            EventBus.getDefault().post(eventBusEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
//    public void onMessageEvent4(Integer text) {
//        Log.d(TAG, "onMessageEvent4 : Integer" );
//    }

}
