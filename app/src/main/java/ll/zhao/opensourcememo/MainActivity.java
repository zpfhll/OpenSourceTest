package ll.zhao.opensourcememo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ll.zhao.opensourcememo.retrofit.RetrofitActivity;
import retrofit2.Retrofit;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void retrofit(View view){
            Intent intent = new Intent(this, RetrofitActivity.class);
            startActivity(intent);
    }

    public void rxjava(View view){

    }
}
