package michealcob.ts.scientaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MovieDB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SCIENTA";

    public static final String TABLE_MOVIE = "movie_table";



    public static final String KEY_ID = "id";
    public static final String KEY_MOVIEID = "createdAt";
    public static final String KEY_TITLE= "updateAt";

    public static final String KEY_OVERVIEW = "name";
    public static final String KEY_RATING = "phone";
    public static final String KEY_POSTERPATH = "company";
    public static final String KEY_BACKDROP = "aim";



    public MovieDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MOVIE =
                "CREATE TABLE " + TABLE_MOVIE + "("
                        + KEY_ID + " INTEGER PRIMARY KEY, "
                        + KEY_MOVIEID + " TEXT,"
                        + KEY_TITLE+ " TEXT,"
                        + KEY_RATING + " TEXT,"
                        + KEY_OVERVIEW + " TEXT,"
                        + KEY_POSTERPATH + " TEXT,"
                        + KEY_BACKDROP + " TEXT"
                        + ")";

        db.execSQL(CREATE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(db);
    }

    public void createMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIEID, movie.getMovieid());
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_RATING, movie.getRating());
        values.put(KEY_OVERVIEW, movie.getOverview());
        values.put(KEY_POSTERPATH, movie.getPosterpath());
        values.put(KEY_BACKDROP, movie.getBackdrop());
        //inserting Rows
        db.insert(TABLE_MOVIE,null, values);
        db.close();
    }

    public Movie getMovie(String id) {
        // get readable database as we are not inserting anything
        Movie movie = new Movie();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOVIE,
                new String[]{KEY_MOVIEID, KEY_TITLE, KEY_RATING,
                        KEY_OVERVIEW, KEY_POSTERPATH, KEY_BACKDROP},
                KEY_MOVIEID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            movie.setId(Integer.parseInt(cursor.getString(0)));
            movie.setMovieid(cursor.getString(1));
            movie.setTitle(cursor.getString(2));
            movie.setRating(cursor.getString(3));
            movie.setOverview(cursor.getString(4));
            movie.setPosterpath(cursor.getString(5));
            movie.setBackdrop(cursor.getString(6));
            cursor.close();
        }else{
            movie.setMovieid(null);
            movie.setTitle(null);
            movie.setRating(null);
            movie.setOverview(null);
            movie.setPosterpath(null);
            movie.setBackdrop(null);
        }
        // close the db connection
        return movie;

    }

    public List<Movie> getAllMovies(){
        List<Movie> moviesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(0)));
                movie.setMovieid(cursor.getString(1));
                movie.setTitle(cursor.getString(2));
                movie.setRating(cursor.getString(3));
                movie.setOverview(cursor.getString(4));
                movie.setPosterpath(cursor.getString(5));
                movie.setBackdrop(cursor.getString(6));
                moviesList.add(movie);
            }while (cursor.moveToNext());
        }
        return moviesList;
    }
}
