package michealcob.ts.scientaapp;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import michealcob.ts.scientaapp.network.retrofit.ApiClient;
import michealcob.ts.scientaapp.network.retrofit.ApiService;
import michealcob.ts.scientaapp.network.retrofit.response.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRv;
    RecyclerView.Adapter mAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    SpinKitView spinKitView;
    ViewAdapter.ItemClickListener itemClickListener;

    public final static String KEY = "bfaf2a78e37dfcde0dcc6c1492a456bb";

    Button allBtn, favBtn, loadMoreBtn;
    MovieDB movieDB;
    int pages = 1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Ubuntu-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemClickListener = new ViewAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(Movie movie) {
                DetailsDialog dialog = new DetailsDialog();
                Bundle args = new Bundle();
                args.putParcelable("movie", movie);
                args.putString("title", movie.getTitle());
                args.putString("rating", movie.rating);
                args.putString("backdrop", movie.backdrop);
                args.putString("overview", movie.overview);
                args.putString("id", movie.movieid);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.setArguments(args);
                dialog.show(ft, "here");
            }
        };

        movieDB = new MovieDB(this);
        allBtn = findViewById(R.id.btn_all);
        favBtn = findViewById(R.id.btn_fav);
        loadMoreBtn = findViewById(R.id.btn_loadmore);
        spinKitView = findViewById(R.id.spin_kit);

        mRv = findViewById(R.id.rv_movies);
        callApi();

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi();
            }
        });

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOfflineFav();
            }
        });

        loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLoadMore();
            }
        });
    }

    private void callLoadMore() {
        spinKitView.setVisibility(View.VISIBLE);
        String pageValue = String.valueOf(pages++);
        ApiService apiService = ApiClient.getApi().create(ApiService.class);
        Call<ApiResponse> responseCall = apiService.getLoadMoreResponse(KEY, pageValue);
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                spinKitView.setVisibility(View.GONE);
                ApiResponse apiResponse = response.body();
                List<ApiResponse.Results> result = apiResponse.getResults();
                List<Movie> movieListAdd = new ArrayList<>();
                for (int i=0;i<result.size();i++){
                    ApiResponse.Results results = result.get(i);
                    Movie movie = new Movie();
                    movie.setMovieid(results.getId());
                    movie.setTitle(results.getTitle());
                    movie.setRating(results.getVote());
                    movie.setOverview(results.getOverview());
                    movie.setPosterpath(results.getPosterPath());
                    movie.setBackdrop(results.getBackdropPath());
                    movieListAdd.add(movie);
                }
                mAdapter = new ViewAdapter(getApplication(),
                        movieListAdd, itemClickListener);
                staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
                mRv.setLayoutManager(staggeredGridLayoutManager);
                mRv.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                spinKitView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext()
                        , "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callOfflineFav() {
        spinKitView.setVisibility(View.VISIBLE);
        List<Movie> movieList = movieDB.getAllMovies();
        if (movieList.isEmpty()){
            spinKitView.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No favorite added yet", Toast.LENGTH_SHORT).show();
            return;
        }
        spinKitView.setVisibility(View.INVISIBLE);
        mAdapter = new ViewAdapter(getApplication(), movieList, itemClickListener);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        mRv.setLayoutManager(staggeredGridLayoutManager);
        mRv.setAdapter(mAdapter);
    }

    private void callApi() {
        spinKitView.setVisibility(View.VISIBLE);
        ApiService apiService = ApiClient.getApi().create(ApiService.class);
        Call<ApiResponse> responseCall = apiService.getAllResponse(KEY);
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                spinKitView.setVisibility(View.INVISIBLE);
                loadMoreBtn.setVisibility(View.VISIBLE);
                ApiResponse apiResponse = response.body();
                List<ApiResponse.Results> result = apiResponse.getResults();
                List<Movie> movieListAdd = new ArrayList<>();
                for (int i=0;i<result.size();i++){
                    ApiResponse.Results results = result.get(i);
                    Movie movie = new Movie();
                    movie.setMovieid(results.getId());
                    movie.setTitle(results.getTitle());
                    movie.setRating(results.getVote());
                    movie.setOverview(results.getOverview());
                    movie.setPosterpath(results.getPosterPath());
                    movie.setBackdrop(results.getBackdropPath());
                    movieListAdd.add(movie);
                }
                mAdapter = new ViewAdapter(getApplication(),
                        movieListAdd, itemClickListener);
                staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
                mRv.setLayoutManager(staggeredGridLayoutManager);
                mRv.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                spinKitView.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext()
                        , "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
