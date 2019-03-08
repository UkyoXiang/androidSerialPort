package com.kongqw.serialport;

public class Song {

    private int songNo;
    private boolean songSelected;
    private String[] songDetail = new String[]{};
    private String[] songFilter = new String[]{};
    public Song() {

    }

    public int getSongNo() {
        return songNo;
    }

    public void setSongNo(int songNo) {
        this.songNo = songNo;
    }

    public boolean getSongSelected() {
        return songSelected;
    }

    public void setSongSelected(Boolean selected) {
        this.songSelected = selected;
    }

    public String[] getSongDetail() {
        return songDetail;
    }

    public void setSongDetail(String[] songDetail) {
        this.songDetail = songDetail;
    }

    public String[] getSongFilter() {
        return songFilter;
    }

    public void setSongFilter(String[] songFilter) {
        this.songFilter = songFilter;
    }
}
