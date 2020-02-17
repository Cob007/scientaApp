package michealcob.ts.scientaapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;


public class DetailsDialog extends DialogFragment {

    public static final String TAG = "setRoute";

    ImageView ivClose, ivBackdrop, ivFav;

    TextView mTitle, mRating, mOverview;

    String youtubeId;
    Movie parceMovie;
    MovieDB movieDB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.details, container, false);

        ivClose = view.findViewById(R.id.iv_back);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelUpload();
            }
        });

        movieDB = new MovieDB(getActivity());

        ivFav = view.findViewById(R.id.favorite);

        parceMovie = getArguments().getParcelable("movie");

        String title = getArguments().getString("title");
        String rating = getArguments().getString("rating");
        String overview = getArguments().getString("overview");

        String backdrop = getArguments().getString("backdrop");

        String movieId = getArguments().getString("id");

        String url = "https://image.tmdb.org/t/p/w500"+backdrop;

        ivBackdrop = view.findViewById(R.id.iv_backdrop);

        checkFav();

        Glide.with(getActivity()).load(url).into(ivBackdrop);

        mTitle = view.findViewById(R.id.tv_title);
        mOverview =view.findViewById(R.id.tv_overview);
        mRating = view.findViewById(R.id.tv_rating);

        mTitle.setText(title);
        mOverview.setText(overview);
        mRating.setText(rating);
        ivBackdrop = view.findViewById(R.id.iv_backdrop);

        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callStorage(parceMovie);
            }
        });
        return view;

    }

    private void checkFav() {
        String parMoId = parceMovie.movieid;
        List<Movie> check = movieDB.getAllMovies();
        for (int i=0; i<check.size();i++){
            Movie mo = check.get(i);
            if (parMoId.equals(mo.getMovieid())){
                ivFav.setImageResource(R.drawable.ic_favorite_red);
                ivFav.setEnabled(false);
                return;
            }
        }
    }

    private void callStorage(Movie parceMovie) {
        movieDB.createMovie(parceMovie);
        ivFav.setImageResource(R.drawable.ic_favorite_red);
    }

    private void cancelUpload() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
