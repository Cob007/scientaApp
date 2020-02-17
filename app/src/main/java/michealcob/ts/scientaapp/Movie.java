package michealcob.ts.scientaapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    Integer id;
    String title;
    String rating;
    String overview;
    String posterpath;
    String backdrop;
    String movieid;

    protected Movie(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        rating = in.readString();
        overview = in.readString();
        posterpath = in.readString();
        backdrop = in.readString();
        movieid = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie() {
    }

    public Integer getId() {
        return id;
    }

    public String getMovieid() {
        return movieid;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getPosterpath() {
        return posterpath;
    }

    public String getRating() {
        return rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeString(rating);
        dest.writeString(overview);
        dest.writeString(posterpath);
        dest.writeString(backdrop);
        dest.writeString(movieid);
    }
}
