package com.kongqw.serialport;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import sysu.zyb.panellistlibrary.AbstractPanelListAdapter;
import sysu.zyb.panellistlibrary.PanelListLayout;


public class SongPanelListAdapter extends AbstractPanelListAdapter {

    private Context context;
    private List<Song> songList;
    private int resourceId;
    private int record_select_id = -1;
    private OnMoreSongListener mMoreSongListener;
    private OnColumnListener mColumnListener;



    public interface OnMoreSongListener
    {
        public void OnMoreSongListener(int position);
    }

    public interface OnColumnListener
    {
        public void OnColumnListener(int position);
    }

    public SongPanelListAdapter(Context context, PanelListLayout pl_root, ListView lv_content,
                                List<Song> songList, int resourceId) {
        super(context, pl_root, lv_content);
        this.context = context;
        this.songList = songList;
        this.resourceId = resourceId;
    }

    public void setOnMoreSongListener(OnMoreSongListener eventListener) {
        mMoreSongListener = eventListener;
    }

    public void setOnColumnListener(OnColumnListener eventListener) {
        mColumnListener = eventListener;
    }

    @Override
    protected BaseAdapter getContentAdapter() {
        return new ContentAdapter(context,resourceId,songList);
    }

    private class ContentAdapter extends ArrayAdapter {

        private int itemResourceId;
        private List<Song> songList;

        public ContentAdapter(@NonNull Context context, @LayoutRes int resource, List<Song> songList) {
            super(context, resource);
            this.itemResourceId = resource;
            this.songList = songList;
        }

        @Override
        public int getCount() {
            return songList.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            String[] songDetail = songList.get(position).getSongDetail();
            boolean songSelected = songList.get(position).getSongSelected();
            SongClickListener listener = new SongClickListener(position);

            if (convertView == null){
                view = LayoutInflater.from(parent.getContext()).inflate(itemResourceId,parent,false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.tv_1.setOnClickListener(listener);
            viewHolder.tv_2.setOnClickListener(listener);
            viewHolder.tv_3.setOnClickListener(listener);
            viewHolder.tv_4.setOnClickListener(listener);


            viewHolder.tv_1.setText(songDetail[0]);
            viewHolder.tv_2.setText(songDetail[1]);
            viewHolder.tv_3.setText(songDetail[2]);
            viewHolder.tv_4.setText(songDetail[3]);



            if (songSelected)
            {
                viewHolder.tv_1.setBackgroundColor(Color.rgb(190,190,190));
                viewHolder.tv_2.setBackgroundColor(Color.rgb(190,190,190));
                viewHolder.tv_3.setBackgroundColor(Color.rgb(190,190,190));
                viewHolder.tv_4.setBackgroundColor(Color.rgb(190,190,190));

            }
            else
            {
                viewHolder.tv_1.setBackgroundColor(Color.WHITE);
                viewHolder.tv_2.setBackgroundColor(Color.WHITE);
                viewHolder.tv_3.setBackgroundColor(Color.WHITE);
                viewHolder.tv_4.setBackgroundColor(Color.WHITE);
            }

            if (position >= songList.size() -1)
            {
                if(mMoreSongListener!=null)
                {
                    mMoreSongListener.OnMoreSongListener(position);
                }
            }
            return view;
        }
    }

    private class ViewHolder
    {
        private TextView tv_1;
        private TextView tv_2;
        private TextView tv_3;
        private TextView tv_4;


        ViewHolder(View itemView) {
            tv_1 = (TextView) itemView.findViewById(R.id.id_tv_01);
            tv_2 = (TextView) itemView.findViewById(R.id.id_tv_02);
            tv_3 = (TextView) itemView.findViewById(R.id.id_tv_03);
            tv_4 = (TextView) itemView.findViewById(R.id.id_tv_04);

        }
    }


    private class SongClickListener implements View.OnClickListener
    {
        int position;
        public SongClickListener(int i) {
            super();
            position = i;
        }

        @Override
        public void onClick(View v) {
            if (record_select_id >= 0 && record_select_id < songList.size())
                songList.get(record_select_id).setSongSelected(false);

            if (position < songList.size())
                songList.get(position).setSongSelected(true);

            record_select_id = position;

            if(mColumnListener!=null)
            {
                mColumnListener.OnColumnListener(position);
            }
        }
    }
}

