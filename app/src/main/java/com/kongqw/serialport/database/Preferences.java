package com.kongqw.serialport.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * Created by Danny on 2016/5/16.
 */
public class Preferences {

    public static final int DEFAULT_APP_MODE = 0;
//    public static final int DEFAULT_APP_MODE = 1;
//    public static final String DEFAULT_SERVER_IP =  "47.106.144.136";







    public static final int DEFAULT_ADVERTISING_ORDER_MODE = 0;
    public static final int DEFAULT_ADVERTISING_DISPLAY_LENGTH = 2;


    public static final Boolean PRINT_SHOW_LOG = true;
    public static final int DEFAULT_UP_ADVERTISING_DISPLAY_LENGTH = 600;


    public static final int DEFAULT_MARQUEE_DISPLAY_MODE = 0;
    public static final int DEFAULT_MARQUEE_DISPLAY_LENGTH = 30;
    public static final int DEFAULT_MARQUEE_DISPLAY_INTERVAL = 5;



    public static final int DEFAULT_TRANSACTION_TOTAL_AMOUNT = 0;
    public static final int DEFAULT_RANSACTION_COUNT = 0;


    public static final String KEY_APP_MODE = "APP_MODE";
    public static final String KEY_ROOM_ID   = "ROOM_ID";
    public static final String KEY_ROOM_PW   = "ROOM_PW";
    public static final String KEY_SERVER_IP = "SERVER_IP";

    public static final String KEY_TRANSACTION_TOTAL_AMOUNT = "TRANSACTION_TOTAL_AMOUNT";
    public static final String KEY_TRANSACTION_COUNT = "KEY_TRANSACTION_COUNT";
    public static final String KEY_T5501_DATE = "KEY_T5501_DATE";
    public static final String KEY_T5501_SN = "KEY_T5501_SN";


    public static final String KEY_ACTIVITY_CELLS = "ACTIVITY_CELLS";
    public static final String KEY_ACTIVITY_HOME_TITLE = "ACTIVITY_HOME_TITLE";
    public static final String KEY_ACTIVITY_INNER_TITLE = "ACTIVITY_INNER_TITLE";
    public static final String KEY_ACTIVITY_TAG   = "ACTIVITY_TAG";

    public static final String KEY_GENRE_1 = "GENRE_1";
    public static final String KEY_GENRE_2 = "GENRE_2";
    public static final String KEY_GENRE_3 = "GENRE_3";
    public static final String KEY_GENRE_4 = "GENRE_4";
    public static final String KEY_GENRE_5 = "GENRE_5";
    
    public static final String KEY_MOOD_1 = "MOOD_1";
    public static final String KEY_MOOD_2 = "MOOD_2";
    public static final String KEY_MOOD_3 = "MOOD_3";
    public static final String KEY_MOOD_4 = "MOOD_4";
    public static final String KEY_MOOD_5 = "MOOD_5";

    public static final String KEY_NEW_SONG_MONTH = "NEW_SONG_MONTH";

    public static final String KEY_ADVERTISING_ORDER_MODE     = "ADVERTISING_ORDER_MODE";
    public static final String KEY_ADVERTISING_DISPLAY_LENGTH = "ADVERTISING_DISPLAY_LENGTH";

    public static final String KEY_MARQUEE_DISPLAY_MODE     = "MARQUEE_DISPLAY_MODE";
    public static final String KEY_MARQUEE_DISPLAY_LENGTH   = "MARQUEE_DISPLAY_LENGTH";
    public static final String KEY_MARQUEE_DISPLAY_INTERVAL = "MARQUEE_DISPLAY_INTERVAL";

    /***/
    public static boolean clearParams( Context context ) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );
        return preferences.edit().clear().commit();
    }

    public static Map<String,?> getAll(Context context ) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        return preferences.getAll();
    }

    /***/
    public static String getStringParam(Context context, String key, String defValue )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        return preferences.getString( key, defValue );
    }

    /***/
    public static int getIntegerParam(Context context, String key, int defValue )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        return preferences.getInt( key, defValue );
    }

    /***/
    public static long getLongParam(Context context, String key, long defValue )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        return preferences.getLong( key, defValue );
    }

    /***/
    public static float getFloatleParam(Context context, String key, float defValue )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        return preferences.getFloat( key, defValue );
    }

    /***/
    public static boolean getBooleanParam(Context context, String key, boolean defValue )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        return preferences.getBoolean( key, defValue );
    }

    /***/
    public static boolean setParam(Context context, String key, String value )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString( key, value );

        return editor.commit();
    }

    /***/
    public static boolean setParam(Context context, String key, int value )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt( key, value );

        return editor.commit();
    }

    /***/
    public static boolean setParam(Context context, String key, long value )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong( key, value );

        return editor.commit();
    }

    /***/
    public static boolean setParam(Context context, String key, float value )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = preferences.edit();

        editor.putFloat( key, value );

        return editor.commit();
    }

    /***/
    public static boolean setParam(Context context, String key, boolean value )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean( key, value );

        return editor.commit();
    }

    /***/
    public static boolean setParams(Context context, String keys[], boolean values[] )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = preferences.edit();

        for( int i = 0 ; i < keys.length ; i++ )
            editor.putBoolean( keys[i], values[i] );

        return editor.commit();
    }

    /***/
    public static boolean setParams(Context context, String keys[], int values[] )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = preferences.edit();

        for( int i = 0 ; i < keys.length ; i++ )
            editor.putInt( keys[i], values[i] );

        return editor.commit();
    }

    /***/
    public static boolean setParams(Context context, String keys[], float values[] )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = preferences.edit();

        for( int i = 0 ; i < keys.length ; i++ )
            editor.putFloat( keys[i], values[i] );

        return editor.commit();
    }

    /***/
    public static boolean setParams(Context context, String keys[], String values[] )
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );
        //	SharedPreferences preferences = context.getSharedPreferences( context.getApplicationContext().getPackageName(), Context.MODE_PRIVATE );

        SharedPreferences.Editor editor = preferences.edit();

        for( int i = 0 ; i < keys.length ; i++ )
            editor.putString( keys[i], values[i] );

        return editor.commit();
    }

    /***/
    public static boolean removeParam(Context context, String key ) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );

        SharedPreferences.Editor editor = preferences.edit();

        editor.remove( key );

        return editor.commit();
    }

    /***/
    public static boolean removeParams(Context context, String keys[] ) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( context );

        SharedPreferences.Editor editor = preferences.edit();

        for( int i = 0 ; i < keys.length ; i++ )
            editor.remove( keys[i] );

        return editor.commit();
    }
}
