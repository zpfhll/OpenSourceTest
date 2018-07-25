package ll.zhao.opensourcememo.eventbus;

import android.app.Activity;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ll.zhao.opensourcememo.BaseActivity;
import ll.zhao.opensourcememo.Constrants;
import ll.zhao.opensourcememo.R;

@Route(path = Constrants.EVENT_BUS)
public class EventBusActivity extends BaseActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        textView = findViewById(R.id.textView);
    }


    public void sendEvent(View view){
            EventBusEntity eventBusEntity = new EventBusEntity(EventBusEntity.EVENT_ACTIVITY,"boom boom boom");
            EventBus.getDefault().post(eventBusEntity);
    }

    public void startSubActivity(View view){
        EventBusEntity eventBusEntity = new EventBusEntity(EventBusEntity.EVENT_SUB_ACTIVITY1,"boom1 boom1 boom1");
        EventBus.getDefault().postSticky(eventBusEntity);
        moveTo(Constrants.EVENT_BUS_SUB1,view);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reciveEvent(EventBusEntity eventBusEntity){
        if(eventBusEntity.getId().endsWith(EventBusEntity.EVENT_ACTIVITY)){
            textView.setText(eventBusEntity.getMessage());
        }else{
            textView.setText("Not EVENTBUS");
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

}
