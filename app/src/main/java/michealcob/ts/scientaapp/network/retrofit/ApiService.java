package michealcob.ts.scientaapp.network.retrofit;



import michealcob.ts.scientaapp.network.retrofit.response.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("discover/movie?sort_by=popularity.desc")
    Call<ApiResponse> getAllResponse(
            @Query("api_key") String apiKey
    );

    @GET("discover/movie?sort_by=popularity.desc")
    Call<ApiResponse> getLoadMoreResponse(
            @Query("api_key") String apiKey,
            @Query("page") String pages
    );


}
