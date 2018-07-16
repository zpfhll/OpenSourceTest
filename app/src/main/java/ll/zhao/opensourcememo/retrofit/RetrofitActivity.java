package ll.zhao.opensourcememo.retrofit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;

import java.io.IOException;

import ll.zhao.opensourcememo.Constrants;
import ll.zhao.opensourcememo.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Route(path = Constrants.RETROFIT)
public class RetrofitActivity extends Activity {
        private TextView content;
        private EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        content = findViewById(R.id.content);
        search = findViewById(R.id.editText);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) { //判断标志位
                case 1:
                    content.setText(msg.obj.toString());
                    break;
            }
        }
    };


    public void search(View view){
        String searchContent = search.getText().toString();
        ApiAccess apiAccess = new ApiAccess();
        apiAccess.search(searchContent, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        TestBean testBean = gson.fromJson(result,TestBean.class);
                        Message msg =Message.obtain();
                        msg.obj = testBean;
                        msg.what=1;   //标志消息的标志
                        handler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }





}
