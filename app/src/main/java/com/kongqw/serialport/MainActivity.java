package com.kongqw.serialport;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sysu.zyb.panellistlibrary.PanelListLayout;

public class MainActivity extends AppCompatActivity {

    private PanelListLayout pl_root;
    private SongPanelListAdapter adapter = null;
    private ListView lv_content;
    private List<Song> songList = new ArrayList<>();
    private List<String> mCondition = new ArrayList<>();
    private String[] permissions=new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private List<String> mPermissionList=new ArrayList<>();
    private static final int PERMISSION_REQUEST = 1;

    private AppApplication mApp;
    private int mSongTotal = 0;
    private int mSelectSong = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApp = AppApplication.getInstance();
        pl_root = findViewById(R.id.id_pl_root);
        lv_content = findViewById(R.id.id_lv_content);


        if (goToRequestPrimission())
        {
            if (adapter == null) {
                adapter = new SongPanelListAdapter(this, pl_root, lv_content, songList, R.layout.item_room);

                adapter.setTitleHeight(30);
                adapter.setTitleWidth(50);
                adapter.setRowDataList(generateRowData());
                adapter.setColumnDataList(generateColumnData());
                adapter.setOnMoreSongListener(new SongPanelListAdapter.OnMoreSongListener() {
                    @Override
                    public void OnMoreSongListener(int position) {
                        if (position < mSongTotal - 1) {
                            mApp.refreshSongRecordsByCondition(position + 1, mCondition);
                            addMoreSongData();
                            adapter.notifyDataSetChanged();
                            Log.d("MoreSong : ", String.valueOf(position));
                        }
                    }
                });
                adapter.setOnColumnListener(new SongPanelListAdapter.OnColumnListener() {
                    @Override
                    public void OnColumnListener(int position) {


                        adapter.notifyDataSetChanged();
                    }
                });

                mApp.refreshSongRecordsCount(mCondition);
                mApp.refreshSongRecordsByCondition(0, mCondition);
                addMoreSongData();
                pl_root.setAdapter(adapter);
            }
            else
            {
                clearSongData();
                mApp.refreshSongRecordsCount(mCondition);
                mApp.refreshSongRecordsByCondition(0, mCondition);
                addMoreSongData();
                adapter.notifyDataSetChanged();
            }
        }
    }

    private List<String> generateRowData() {
        List<String> rowDataList = new ArrayList<>();
        String[] colum_chinese = new String[]{"id", "編號", "歌名", "歌曲長度"};

        for (int i = 0; i < 4; i++) {
            rowDataList.add(colum_chinese[i]);
        }

        return rowDataList;
    }

    private List<String> generateColumnData() {
        List<String> columnDataList = new ArrayList<>();
        for (Song song : songList) {
            columnDataList.add(String.valueOf(song.getSongNo()));
        }
        return columnDataList;
    }


    private void addMoreSongData()
    {
        mSongTotal = mApp.getSongTotal();
        for (int i = 1;i<= mApp.getSongCount();i++){
            Song song = new Song();
            song.setSongNo(i);
            song.setSongDetail(mApp.getSongRecord(i-1));
            songList.add(song);
        }
    }

    private void clearSongData()
    {
        mSelectSong = -1;
        songList.clear();
    }


    private Boolean goToRequestPrimission(){
        Boolean primission = true;
        mPermissionList.clear();
        for(int i=0;i<permissions.length;i++)
        {
            if(ContextCompat.checkSelfPermission(this,permissions[i])!= PackageManager.PERMISSION_GRANTED)
            {
                mPermissionList.add(permissions[i]);
                primission = false;
            }
        }
        if(!mPermissionList.isEmpty()){
            String[] permissions=mPermissionList.toArray(new String[mPermissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,PERMISSION_REQUEST);
        }

        return primission;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("Permissions","ok");
                    mApp.refreshSongRecordsCount(mCondition);
                    mApp.refreshSongRecordsByCondition(0,mCondition);
                    addMoreSongData();
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Log.d("Permissions","fail");
                    goToRequestPrimission();
                }
                break;
            default:
                break;
        }
    }
}

