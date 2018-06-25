package ll.zhao.opensourcememo.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.Query;

public interface ApiService {

    @HTTP(method = "GET",path = "sug?code=utf-8",hasBody = false)
    Call<ResponseBody> search(@Query("q") String name );

}
