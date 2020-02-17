package michealcob.ts.scientaapp;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {


    ItemClickListener itemClickListener;

    List<Movie> resultsList;

    Context context;

    public ViewAdapter(Context mCtx, List<Movie> _resultsList, ItemClickListener itemClickListener){
        context = mCtx;
        resultsList = _resultsList;
        this.itemClickListener= itemClickListener;
    }

    @NonNull
    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        ViewHolder viewHolder = holder;

        final Movie results = resultsList.get(i);

        String posterPath = results.getPosterpath();
        final String title = results.getTitle();
        final String rating = results.getRating();
        final String backdrop = results.getBackdrop();
        final String overview = results.getOverview();
        final String movieid = results.getMovieid();
        /*https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg*/
        String url = "https://image.tmdb.org/t/p/w500"+posterPath;


        Glide.with(context).load(url).into(viewHolder.imageView);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(results);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(null == resultsList) return 0;
        return this.resultsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    public interface ItemClickListener{
        void onItemClicked(Movie movie);
    }

}
