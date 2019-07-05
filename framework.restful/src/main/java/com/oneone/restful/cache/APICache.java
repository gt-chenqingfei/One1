package com.oneone.restful.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class APICache extends SQLiteOpenHelper {

    private static final String TABLE_API_CACHE = "api_cache";

    private static final String[] COLUMNS_API_CACHE = {
    /* 1 */"api",
    /* 2 */"params",
    /* 3 */"result",};

    private static final Logger logger = LoggerFactory.getLogger(APICache.class);

    private static APICache instance = null;

    public static synchronized APICache getInstance(Context context) {
        if (null == instance)
            instance = new APICache(context.getApplicationContext());
        return instance;
    }

    private APICache(Context context) {
        super(context, "leancloud.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS api_cache(api TEXT NOT NULL, params TEXT, result TEXT, PRIMARY KEY(api, params))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String getCachedResult(String api, Map<String, Object> params) {
        final SQLiteDatabase sqlite = getReadableDatabase();
        final JSONObject arguments = new JSONObject(params);
        final String[] selectionArgs = {api, arguments.toString()};
        Cursor c = null;

        try {
            c = sqlite.query(TABLE_API_CACHE, COLUMNS_API_CACHE,
                    "api=? and params=?", selectionArgs, null, null, null);
            if (null != c && c.moveToNext()) {
                return c.getString(c.getColumnIndex("result"));
            }
        } catch (Exception e) {
            logger.error("Get API " + api + " cache error", e);
            return null;
        } finally {
            if (null != c && !c.isClosed()) {
                c.close();
                c = null;
            }
        }

        return null;
    }

    public boolean setCachedResult(String api, Map<String, Object> params,
                                   String result) {
        final SQLiteDatabase sqlite = getWritableDatabase();
        final JSONObject arguments = new JSONObject(params);
        final String[] selectionArgs = {api, arguments.toString()};
        final ContentValues values = new ContentValues();
        Cursor c = null;

        try {
            c = sqlite.query(TABLE_API_CACHE, COLUMNS_API_CACHE,
                    "api=? and params=?", selectionArgs, null, null, null);
            if (null != c && c.moveToNext()) {
                values.put("result", result);
                return -1 != sqlite.update(TABLE_API_CACHE, values,
                        "api=? and params=?", selectionArgs);
            } else {
                values.put("api", api);
                values.put("params", arguments.toString());
                values.put("result", result);
                return -1 != sqlite.insert(TABLE_API_CACHE, null, values);
            }
        } catch (Exception e) {
            logger.error("Set API " + api + " cache error", e);
            return false;
        } finally {
            if (null != c && !c.isClosed()) {
                c.close();
                c = null;
            }
        }
    }

    public void clear() {
        final SQLiteDatabase sqlite = getWritableDatabase();
        try {
            sqlite.delete(TABLE_API_CACHE, "", new String[0]);
        } catch (Exception e) {
            logger.error("Clear API cache error", e);
        }
    }

}
