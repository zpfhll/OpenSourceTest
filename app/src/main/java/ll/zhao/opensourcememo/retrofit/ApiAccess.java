package ll.zhao.opensourcememo.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ApiAccess {

    public static Retrofit retrofit;

    public ApiAccess(){
        if(null == retrofit){
            retrofit = new Retrofit.Builder().baseUrl("https://suggest.taobao.com/").build();
        }
    }

    public void search(String name, Callback<ResponseBody> callback){
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> call = apiService.search(name);
        call.enqueue(callback);
    }

}
