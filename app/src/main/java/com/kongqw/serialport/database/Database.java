package com.kongqw.serialport.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Database {

    /** 命令成功 */
    public static final int CMD_SUCCESS = 0;

    /** 命令失敗 */
    public static final int CMD_FAILED  = 1;

    public static final int DB_MAIN = 0;
    public static final int DB_219 = 1;
    public static final int DB_TIME = 2;

    public static final  File DB_TIME_FILE =new File( Environment.getExternalStorageDirectory() + "/KaraokeController", "song_time.db" );
    public static final  File DB_MAIN_FILE =new File( Environment.getExternalStorageDirectory() + "/KaraokeController", "ktv.db" );
    private static final File DB_219_FILE = new File( Environment.getExternalStorageDirectory() + "/KaraokeController", "ktv_219.db" );

    private Context mContext;

    private SQLiteDatabase mSQLiteDatabase;

    /** */
    public Database( Context context ) {
        mContext = context;
    }

    /** */
    public boolean open( int what ) {
        switch( what ) {
            case DB_MAIN: {
                if( DB_MAIN_FILE.exists() ) {
                    mSQLiteDatabase = SQLiteDatabase.openDatabase(DB_MAIN_FILE.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
                    return true;
                }
                break;
            }
            case DB_219: {
                if( DB_219_FILE.exists() ) {
                    mSQLiteDatabase = SQLiteDatabase.openDatabase(DB_219_FILE.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
                    return true;
                }
                break;
            }
            case DB_TIME: {
                if( DB_TIME_FILE.exists() ) {
                    mSQLiteDatabase = SQLiteDatabase.openDatabase(DB_TIME_FILE.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
                    return true;
                }
                break;
            }
        }

        return false;
    }


    /***/
    public void close() {
        if( mSQLiteDatabase != null ) {
            mSQLiteDatabase.close();
            mSQLiteDatabase = null;
        }
    }

    /** */
    public void initDatabaseFromAssets( int what, String path ) {

        File dbFile = null;
        switch( what ) {
            case DB_MAIN: {
                dbFile = DB_MAIN_FILE;
                break;
            }
            case DB_219: {
                dbFile = DB_219_FILE;
                break;
            }
        }

        if( dbFile != null && !dbFile.exists() ) {
            dbFile.getParentFile().mkdirs();

            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = mContext.getAssets().open( path );
                outputStream = new FileOutputStream( dbFile );

                int length = -1;
                byte[] buffer = new byte[1024];
                while ( ( length = inputStream.read( buffer ) ) != -1 ) {
                    outputStream.write( buffer, 0, length );
                }

            } catch ( IOException e ) {
                e.printStackTrace();
            } finally {
                if ( inputStream != null ) {
                    try {
                        inputStream.close();
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    }
                }
                if ( outputStream != null ) {
                    try {
                        outputStream.close();
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



    public String insert(String table, String record[] ) throws SQLException {
        return insert( table, null, record );
    }

    /**
     * 新增資料
     *
     * @param table		資料表
     * @param column	欄位名稱
     * @param record	儲存資料
     *
     * @return			成功：回傳該筆資料的index；否則回傳null
     */
    public String insert(String table, String column, String record ) throws SQLException {
        return insert( table, new String[]{ column }, new String[]{ record } );
    }

    /**
     * 新增資料
     *
     * @param table		資料表
     * @param columns	欄位名稱列表
     * @param records	儲存資料列表
     *
     * @return			成功：回傳該筆資料的index；否則回傳null
     */
    public String insert(String table, String[] columns, String[] records ) throws SQLException {
        StringBuilder cmdBuilder = new StringBuilder( "INSERT INTO " ).append( table );

        if( columns != null )
        {
            for( int i = 0 ; i < columns.length ; i++ )
            {
                if( i == 0 )
                {
                    cmdBuilder.append( "(" );
                }
                else
                {
                    cmdBuilder.append( "," );
                }

                cmdBuilder.append( columns[i] );
            }

            cmdBuilder.append( ")" );
        }

        for( int i = 0 ; i < columns.length ; i++ )
        {
            if( i == 0 )
            {
                cmdBuilder.append( " VALUES (" );
            }
            else
            {
                cmdBuilder.append( ", " );
            }

            if( records[i] == null )
            {
                cmdBuilder.append( "NULL" );
            }
            else
            {
                cmdBuilder.append( "'" ).append( SqliteEscape( records[i] ) ).append( "'" );
            }
        }

        cmdBuilder.append( ")" );

        Log.d("insertByCmd : ",cmdBuilder.toString());
        mSQLiteDatabase.execSQL( cmdBuilder.toString() );

        Cursor cursor = query( table, columns, records );

        String idx = null;
        if( cursor.getCount() > 0 )
        {
            cursor.moveToFirst();
            idx = cursor.getString( 0 );
        }

        cursor.close();

        return idx;
    }

    //============================================================================================================//
    //============================================================================================================//
    /**
     * 刪除資料表所有資料
     *
     * @param table		資料表
     *
     * @return			成功：CMD_SUCCESS；失敗：CMD_FAILED
     */
    public int delete( String table ) throws SQLException {
        return delete( table, null, "" );
    }

    /**
     * 刪除資料
     *
     * @param table			資料表
     * @param whereColumn	條件欄位
     * @param whereRecord	條件數值
     *
     * @return				成功：CMD_SUCCESS；失敗：CMD_FAILED
     */
    public int delete(String table, String whereColumn, String whereRecord ) throws SQLException {
        return delete( table, whereColumn == null ? null : new String[]{ whereColumn }, new String[]{ whereRecord } );
    }

    /**
     * 刪除資料
     *
     * @param table			資料表
     * @param whereColumns	條件欄位列表
     * @param whereRecords	條件數值列表
     *
     * @return				成功：CMD_SUCCESS；失敗：CMD_FAILED
     */
    public int delete(String table, String[] whereColumns, String[] whereRecords ) throws SQLException {
        StringBuilder whereBuilder = new StringBuilder();

        if( whereColumns != null )
        {
            for( int i = 0 ; i < whereColumns.length ; i++ )
            {
                if( i > 0 )
                {
                    whereBuilder.append( " AND " );
                }

                whereBuilder.append( whereColumns[i] );

                if( whereRecords[i] == null )
                {
                    whereBuilder.append( " IS NULL" );
                }
                else
                {
                    whereBuilder.append( " = '" ).append( SqliteEscape( whereRecords[i] ) ).append( "'" );
                }
            }
        }
        else
        {
            return delete( table, null );
        }

        return delete( table, "WHERE " + whereBuilder.toString() );
    }

    /**
     * 刪除資料
     *
     * @param table			資料表
     * @param clause    	條件式
     *
     * @return				成功：CMD_SUCCESS；失敗：CMD_FAILED
     */
    public int delete(String table, String clause ) throws SQLException {
        StringBuilder cmdBuilder = new StringBuilder( "DELETE FROM " ).append( table );

        if( !TextUtils.isEmpty( clause ) )
        {
            cmdBuilder.append( " " ).append( clause );
        }

        Log.d("deleteByCmd : ",cmdBuilder.toString());
        mSQLiteDatabase.execSQL( cmdBuilder.toString() );

        return CMD_SUCCESS;
    }

    //============================================================================================================//
    //============================================================================================================//

    public int update(String table, String desColumn, String desRecord ) throws SQLException {
        return update( table, new String[]{ desColumn }, new String[]{ desRecord }, null );
    }

    /**
     * 更新資料
     *
     * @param table			資料表
     * @param desColumn		新資料的欄位
     * @param desRecord		新資料
     * @param whereColumn	條件欄位
     * @param whereRecord	條件數值
     *
     * @return				成功：CMD_SUCCESS；失敗：CMD_FAILED
     */
    public int update(String table, String desColumn, String desRecord, String whereColumn, String whereRecord ) throws SQLException {
        return update( table,
                new String[]{ desColumn },
                new String[]{ desRecord },
                whereColumn == null ? null : new String[]{ whereColumn },
                new String[]{ whereRecord } );
    }

    /**
     * 更新資料
     *
     * @param table			資料表
     * @param desColumn		新資料的欄位
     * @param desRecord		新資料
     * @param whereColumns	條件欄位列表
     * @param whereRecords	條件數值列表
     *
     * @return				成功：CMD_SUCCESS；失敗：CMD_FAILED
     */
    public int update(String table, String desColumn, String desRecord, String[] whereColumns, String[] whereRecords ) throws SQLException {
        return update( table,
                new String[]{ desColumn },
                new String[]{ desRecord },
                whereColumns,
                whereRecords );
    }

    /**
     * 更新資料
     *
     * @param table			資料表
     * @param desColumns	新資料的欄位列表
     * @param desRecords	新資料列表
     * @param whereColumn	條件欄位
     * @param whereRecord	條件數值
     *
     * @return				成功：CMD_SUCCESS；失敗：CMD_FAILED
     */
    public int update(String table, String[] desColumns, String[] desRecords, String whereColumn, String whereRecord ) throws SQLException {
        return update( table,
                desColumns,
                desRecords,
                whereColumn == null ? null : new String[]{ whereColumn },
                new String[]{ whereRecord } );
    }

    /**
     * 更新資料
     *
     * @param table			資料表
     * @param desColumns	新資料的欄位列表
     * @param desRecords	新資料列表
     * @param whereColumns	條件欄位列表
     * @param whereRecords	條件數值列表
     *
     * @return				成功：CMD_SUCCESS；失敗：CMD_FAILED
     */
    public int update(String table, String[] desColumns, String[] desRecords, String[] whereColumns, String[] whereRecords ) throws SQLException {
        StringBuilder whereBuilder = new StringBuilder();

        if( whereColumns != null )
        {
            for( int i = 0 ; i < whereColumns.length ; i++ )
            {
                if( i > 0 )
                {
                    whereBuilder.append( " AND " );
                }

                whereBuilder.append( whereColumns[i] );

                if( whereRecords[i] == null )
                {
                    whereBuilder.append( " IS NULL" );
                }
                else
                {
                    whereBuilder.append( " = '" ).append( SqliteEscape( whereRecords[i] ) ).append( "'" );
                }
            }
        }

        return update( table, desColumns, desRecords, whereBuilder.length() == 0 ? null : "WHERE " + whereBuilder.toString() );
    }

    /**
     * 更新資料
     *
     * @param table			資料表
     * @param desColumns	新資料的欄位列表
     * @param desRecords	新資料列表
     * @param clause		條件式
     *
     * @return				成功：CMD_SUCCESS；失敗：CMD_FAILED
     */
    public int update(String table, String[] desColumns, String[] desRecords, String clause ) throws SQLException {
        StringBuilder cmdBuilder = new StringBuilder( "UPDATE " ).append( table ).append( " SET " );

        for( int i = 0 ; i < desColumns.length ; i++ )
        {
            if( i > 0 )
            {
                cmdBuilder.append( ", " );
            }

            cmdBuilder.append( desColumns[i] ).append( " = " );

            if( desRecords[i] == null )
            {
                cmdBuilder.append( "NULL" );
            }
            else
            {
                cmdBuilder.append( "'" ).append( SqliteEscape( desRecords[i] ) ).append( "'" );
            }
        }

        if( !TextUtils.isEmpty( clause ) )
        {
            cmdBuilder.append( " " ).append( clause );
        }

        Log.d("updateByCmd : ",cmdBuilder.toString());
        mSQLiteDatabase.execSQL( cmdBuilder.toString() );

        return CMD_SUCCESS;
    }

    //============================================================================================================//
    //============================================================================================================//
    /**
     * 搜尋資料表所有資料
     *
     * @param table		資料表
     *
     * @return			成功：所有資料；失敗：null
     */
    public Cursor query(String table )
    {
        return query( table, null, "" );
    }

    /**
     * 搜尋指定欄位資料
     *
     * @param table		資料表
     * @param clause	修件
     *
     * @return			成功：資料列表；失敗：null
     */
    public Cursor query(String table, String clause )
    {
        return query( table, null, clause );
    }

    /**
     * 搜尋指定欄位資料
     *
     * @param table		資料表
     * @param columns	欄位列表
     *
     * @return			成功：資料列表；失敗：null
     */
    public Cursor query(String table, String[] columns )
    {
        return query( table, columns, "" );
    }

    /**
     * 搜尋資料
     *
     * @param table			資料表
     * @param whereColumns	條件欄位列表
     * @param whereRecords	條件數值列表
     *
     * @return				成功：資料列表；失敗：null
     */
    public Cursor query(String table, String[] whereColumns, String[] whereRecords )
    {
        return query( table, null, whereColumns, whereRecords, null );
    }

    /**
     * 搜尋資料
     *
     * @param table			資料表
     * @param whereColumns	條件欄位列表
     * @param whereRecords	條件數值列表
     * @param clause		額外件件
     *
     * @return				成功：資料列表；失敗：null
     */
    public Cursor query(String table, String[] whereColumns, String[] whereRecords, String clause )
    {
        return query( table, null, whereColumns, whereRecords, clause );
    }

    /**
     * 搜尋指定欄位資料
     *
     * @param table			資料表
     * @param columns		指定欄位列表
     * @param whereColumns	條件欄位列表
     * @param whereRecords	條件數值列表
     *
     * @return				成功：資料列表；失敗：null
     */
    public Cursor query(String table, String[] columns, String[] whereColumns, String[] whereRecords )
    {
        return query( table, columns, whereColumns, whereRecords, null );
    }

    /**
     * 搜尋指定欄位資料
     *
     * @param table			資料表
     * @param columns		指定欄位列表
     * @param whereColumns	條件欄位列表
     * @param whereRecords	條件數值列表
     * @param clause		額外條件
     *
     * @return				成功：資料列表；失敗：null
     */
    public Cursor query(String table, String[] columns, String[] whereColumns, String[] whereRecords, String clause )
    {
        StringBuilder whereBuilder = new StringBuilder();

        if( whereColumns != null )
        {
            for( int i = 0 ; i < whereColumns.length ; i++ )
            {
                if( i > 0 )
                {
                    whereBuilder.append( " AND " );
                }

                whereBuilder.append( whereColumns[i] );

                if( whereRecords[i] == null )
                {
                    whereBuilder.append( " IS NULL" );
                }
                else
                {
                    whereBuilder.append( " = '" ).append( SqliteEscape( whereRecords[i] ) ).append( "'" );
                }
            }
        }

        if( !TextUtils.isEmpty( clause ) )
        {
            whereBuilder.append( " " ).append( clause );
        }

        return query( table, columns, whereBuilder.length() == 0 ? null : "WHERE " + whereBuilder.toString() );
    }

    /**
     * 搜尋指定欄位資料
     *
     * @param table		資料表
     * @param columns	指定欄位列表
     * @param clause	條件式
     *
     * @return				成功：資料列表；失敗：null
     */
    public Cursor query(String table, String[] columns, String clause )
    {
        StringBuilder cmdBuilder = new StringBuilder( "SELECT " );

        if( columns == null )
        {
            cmdBuilder.append( "*" );
        }
        else
        {
            for( int i = 0 ; i < columns.length ; i++ )
            {
                if( i > 0 )
                {
                    cmdBuilder.append( "," );
                }

                cmdBuilder.append( columns[i] );
            }
        }

        cmdBuilder.append( " FROM " ).append( table );

        if( !TextUtils.isEmpty( clause ) )
        {
            cmdBuilder.append( " " ).append( clause );
        }

        return queryByCmd( cmdBuilder.toString() );
    }

    /**
     * 搜尋指定欄位資料
     *
     * @param table		資料表
     * @param clause	條件
     * @param clause	附加條件
     *
     * @return				成功：資料列表；失敗：null
     */
    public Cursor queryAllByCondition(String table, List<String> condition, String clause ) {
        StringBuilder cmdBuilder = new StringBuilder( "SELECT *" );
        cmdBuilder.append( " FROM " ).append( table );

        if( clause != null && condition.size() > 0)
        {
            cmdBuilder.append( " WHERE " );
            for( int i = 0 ; i < condition.size() ; i++ )
            {
                if( i > 0 )
                {
                    cmdBuilder.append( " AND " );
                }
                cmdBuilder.append( condition.get(i) );
            }
        }

        if( !TextUtils.isEmpty( clause ) )
        {
            cmdBuilder.append( " " ).append( clause );
        }
        Log.d("queryAllByCondition",cmdBuilder.toString());
        return queryByCmd( cmdBuilder.toString() );
    }

    public Cursor queryCountByCondition(String table, List<String> condition) {
        StringBuilder cmdBuilder = new StringBuilder( "SELECT COUNT(*)" );
        cmdBuilder.append( " FROM " ).append( table );

        if( condition != null && condition.size() > 0)
        {
            cmdBuilder.append( " WHERE " );
            for( int i = 0 ; i < condition.size() ; i++ )
            {
                if( i > 0 )
                {
                    cmdBuilder.append( " AND " );
                }

                cmdBuilder.append( condition.get(i) );
            }
        }

        return queryByCmd( cmdBuilder.toString() );
    }


    /** */
    public Cursor queryByCmd(String cmd ) {
        Log.d("queryByCmd : ",cmd);
        return mSQLiteDatabase.rawQuery( cmd, null );
    }

    /** */
    public String[][] cursorToStringArray(Cursor cursor ) {
        String[][] result = null;

        if( cursor.getCount() > 0 )
        {
            result = new String[ cursor.getCount() ][ cursor.getColumnCount() ];

            cursor.moveToFirst();
            for( int i = 0 ; i < result.length ; i++ )
            {
                for( int j = 0 ; j < result[0].length ; j++ )
                {
                    result[i][j] = cursor.getString( j );
                }
                cursor.moveToNext();
            }
        }

        return result;
    }

    /** */
    public void execSQL( String sql ) throws SQLException {
        mSQLiteDatabase.execSQL( sql );
    }


    /** */
    public int queryCount(String table, String clause ) {
        return queryCount( table, clause, null );
    }

    /** */
    public int queryCount(String table, String clause, String distinct_column ) {
        StringBuilder cmdBuilder = new StringBuilder( "SELECT COUNT(" );
        if( TextUtils.isEmpty( distinct_column ) )
            cmdBuilder.append( "*" );
        else
            cmdBuilder.append( "DISTINCT " ).append( distinct_column );
        cmdBuilder.append( ") FROM " ).append( table );
        if( !TextUtils.isEmpty( clause ) )
            cmdBuilder.append( " " ).append( clause );


        Cursor cursor = mSQLiteDatabase.rawQuery( cmdBuilder.toString(), null );

        cursor.moveToFirst();
        int count = cursor.getInt( 0 );

        cursor.close();

        return count;
    }


    /** */
    private String SqliteEscape(String keyWord )
    {
        //	keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        //	keyWord = keyWord.replace("[", "/[");
        //	keyWord = keyWord.replace("]", "/]");
        //	keyWord = keyWord.replace("%", "/%");
        //	keyWord = keyWord.replace("&","/&");
        //	keyWord = keyWord.replace("_", "/_");
        //	keyWord = keyWord.replace("(", "/(");
        //	keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public String getDBPath()
    {
        return DB_MAIN_FILE.getAbsolutePath();
    }

}
