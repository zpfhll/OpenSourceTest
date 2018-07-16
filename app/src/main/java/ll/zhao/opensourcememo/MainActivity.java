package ll.zhao.opensourcememo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

import ll.zhao.opensourcememo.retrofit.RetrofitActivity;
import retrofit2.Retrofit;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void retrofit(View view){
//            Intent intent = new Intent(this, RetrofitActivity.class);
//            startActivity(intent);
        moveTo(Constrants.RETROFIT,view);
    }

    public void rxjava(View view){
//            Intent intent = new Intent(this, RetrofitActivity.class);
//            startActivity(intent);
        moveTo(Constrants.RXJAVA,view);
    }


    private void moveTo(String path,View v){
        ActivityOptionsCompat compat = ActivityOptionsCompat.
                makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
        ARouter.getInstance().build(path).withOptionsCompat(compat).navigation();
    }
}
