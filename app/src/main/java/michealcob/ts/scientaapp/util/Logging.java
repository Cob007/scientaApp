package michealcob.ts.scientaapp.util;

import android.util.Log;

public class Logging {
    public static final void Log(String message){
        Log("movieAPP",message);
    }

    public static final void Log(String tag, String messgae){
        Log.d(tag,messgae);
    }
}
