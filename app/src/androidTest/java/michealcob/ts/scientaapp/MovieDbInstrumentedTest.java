package michealcob.ts.scientaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import michealcob.ts.scientaapp.util.Logging;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class MovieDbInstrumentedTest {

    private static String title;
    private static String rating;
    private static String overview;
    private static String posterpath;
    private static String backdrop;
    private static String movieid;
    private static long id;

    private Context mContext;

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    }

    @Test
    public void testDropDB(){
        assertTrue(mContext.deleteDatabase(MovieDB.DATABASE_NAME));
        Logging.Log("testDropDB Pass");
    }

    @Test
    public void testCreateDB(){
        MovieDB movieDB = new MovieDB(mContext);
        SQLiteDatabase db = movieDB.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
        Logging.Log("testCreateDB Pass");

    }


    @Test
    public void testInsertData(){
        MovieDB dbHelper = new MovieDB(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        title = "Micheal";
        rating = "9";
        overview = "this is good";
        posterpath = "thereisgood";
        backdrop = "thisisalsogood";
        movieid = "12434g";

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDB.KEY_TITLE, title);
        contentValues.put(MovieDB.KEY_RATING, rating);
        contentValues.put(MovieDB.KEY_OVERVIEW, overview);
        contentValues.put(MovieDB.KEY_POSTERPATH, posterpath);
        contentValues.put(MovieDB.KEY_BACKDROP, backdrop);
        contentValues.put(MovieDB.KEY_MOVIEID, movieid);

        id = db.insert(MovieDB.TABLE_MOVIE, null, contentValues);
        assertTrue(id != -1);
        Logging.Log("testInsertData Pass - ID: "+ id);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("michealcob.ts.scientaapp", appContext.getPackageName());
    }


}
