package ll.zhao.opensourcememo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //DUBAGフラグによって、設定する
        ARouter.openLog();
        ARouter.openDebug();


        ARouter.init( this );
    }
}
