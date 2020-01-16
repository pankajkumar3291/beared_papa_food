package com.smartitventures.beardpapa.other_class;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
public class Session {
    private static Session instance = null;
    private SharedPreferences sharedPref;
    private Context context;
    private SharedPreferences.Editor editor;
    public Session(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }
    public void put(String key, Object value) {
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
            editor.commit();
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
            editor.commit();
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
            editor.commit();
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
            editor.commit();
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
            editor.commit();
        } else if (value instanceof Double) {
            editor.putString(key, String.valueOf(value));
            editor.commit();
        }
    }
    public Object get(String key) {
        Object object = null;
        Map<String, ?> all = sharedPref.getAll();
        if (all.get(key) instanceof String) {
            object = sharedPref.getString(key, "");
            return object;
        } else if (all.get(key) instanceof Boolean) {
            object = sharedPref.getBoolean(key, false);
            return object;
        } else if (all.get(key) instanceof Integer) {
            object = sharedPref.getInt(key, -1);
            return object;
        } else if (all.get(key) instanceof Float) {
            object = sharedPref.getFloat(key, -0.0f);
            return object;
        } else if (all.get(key) instanceof Long) {
            object = sharedPref.getFloat(key, 0l);
            return object;
        } else if (all.get(key) instanceof Double) {
            double dvalue;
            dvalue = Double.valueOf(sharedPref.getString(key, "-0.0"));
            object = dvalue;
            return object;
        } else return object;
    }
    public boolean contains(String key) {
        return sharedPref.contains(key);
    }
    public void delete(String key) {
        editor.remove(key);
        editor.commit();
    }
    public void clearAll() {
        editor.clear();
    }

    public static Session getInstance(Context context1) {
        if(instance == null) {
            instance = new Session(context1);
        }
        return instance;
    }
}
