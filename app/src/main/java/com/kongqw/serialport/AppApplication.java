package com.kongqw.serialport;

import android.app.Application;
import android.database.Cursor;

import com.kongqw.serialport.database.Database;
import com.kongqw.serialport.database.Table;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;


public class AppApplication extends Application {

    public static final String MV_DIR_NAME = "ktv_mv";
    public static final String STORAGE_PATH = "/storage/";
    public static final String UPDATE_SONG_DB_FILE_EXTENSION[] = {"sr"};
    private static final String DBPASSWORD = "willnogirlfriend";
    private static final int KEYLENGTH = 256;
    private static AppApplication mApp;
    private Database mDatabase;
    private String mSongRecords[][];
    private int mSongTotal = 0;

    /** */
    public static AppApplication getInstance() {
        return mApp;
    }

    private static String getFileMD5(File file) throws NoSuchAlgorithmException, IOException {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        digest = MessageDigest.getInstance("MD5");
        in = new FileInputStream(file);
        while ((len = in.read(buffer, 0, 1024)) != -1) {
            digest.update(buffer, 0, len);
        }
        in.close();
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        /*Process process = null;
        try {
            process = new ProcessBuilder( new String[]{ "su", "-c", "id" } ).redirectErrorStream( true ).start();

            BufferedReader in = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
            String back = in.readLine();
            if( back != null )
                Log.e( "process", back );
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(process != null){
                try{
                    process.destroy();
                }catch (Exception e) {
                }
            }
        }*/

        mDatabase = new Database(this);
        //mDatabase.initDatabaseFromAssets( Database.DB_MAIN, "ktv.db" );
//        mDatabase.initDatabaseFromAssets( Database.DB_219, "ktv_219.db" );
    }

    public long getTime() {
        return System.currentTimeMillis() / 1000;
    }

    public int getSongTotal() {
        return mSongTotal;
    }

    public int getSongCount() {
        return mSongRecords == null ? 0 : mSongRecords.length;
    }

    public String[] getSongRecord(int index) {
        return mSongRecords[index];

    }

    public void refreshSongRecordsByCondition(int position, List<String> condition) {
        mDatabase.open(Database.DB_TIME);
        Cursor cursor = mDatabase.queryAllByCondition(Table.Song.TABLE_NAME, condition, "LIMIT 100 OFFSET " + String.valueOf(position));
        mSongRecords = mDatabase.cursorToStringArray(cursor);
        cursor.close();
    }

    public void refreshSongRecordsCount(List<String> condition) {
        mDatabase.open(Database.DB_TIME);
        Cursor cursor = mDatabase.queryCountByCondition(Table.Song.TABLE_NAME, condition);
        cursor.moveToFirst();
        mSongTotal = cursor.getInt(0);
        cursor.close();
    }

    public int checkSongExists(String condition) {
        int result = 0;
        mDatabase.open(Database.DB_TIME);
        Cursor cursor = mDatabase.queryCountByCondition(Table.Song.TABLE_NAME, Arrays.asList(condition));

        cursor.moveToFirst();
        result = cursor.getInt(0);
        cursor.close();
        return result;
    }

    public int updateSongRecord(String[] columns, String[] records, String[] whereColumns, String[] whereRecords) {
        int result = 0;
        mDatabase.open(Database.DB_TIME);
        result = mDatabase.update(Table.Song.TABLE_NAME, columns, records, whereColumns, whereRecords);
        return result;
    }

    public String insertSongRecord(String[] columns, String[] records) {
        String result = "";
        mDatabase.open(Database.DB_TIME);
        result = mDatabase.insert(Table.Song.TABLE_NAME, columns, records);
        return result;
    }

    public int deleteSongRecord(String[] whereColumns, String[] whereRecords) {
        int result = 0;
        mDatabase.open(Database.DB_TIME);
        result = mDatabase.delete(Table.Song.TABLE_NAME, whereColumns, whereRecords);
        return result;
    }


}
