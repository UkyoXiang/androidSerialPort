package com.kongqw.serialport.database;

/**
 * Created by Danny on 2018/1/25.
 */

public class Table {
    
    public static class Song {
        public static final String TABLE_NAME = "songData";
        /** 0 index */
        public static final String COLUMN_INDEX = "idx";
        /** 1 編號 */
        public static final String COLUMN_ID = "id";
        /** 2 檔名 */
        public static final String COLUMN_VIDEO_FILE = "video_file";
        /** 3 歌名 */
        public static final String COLUMN_TITLE = "title";
        /** 4 字部 */
        public static final String COLUMN_TITLE_WORD_COUNT = "title_word_count";
        /** 5 歌手 */
        public static final String COLUMN_SINGER = "singer";
        /** 6 語別 */
        public static final String COLUMN_LANGUAGE = "language";
        /** 7 區域 */
        public static final String COLUMN_AREA = "area";
        /** 8 曲風 */
        public static final String COLUMN_GENRE = "genre";
        /** 9 心情 */
        public static final String COLUMN_MOOD = "mood";
        /** 10 姓畫 */
        public static final String COLUMN_LAST_NAME_STROKES = "last_name_strokes";
        /** 11 歌手首注音 */
        public static final String COLUMN_SINGER_ZHUYIN_FIRST_CHAR = "singer_zhuyin_first_char";
        /** 12 歌手首拼音 */
        public static final String COLUMN_SINGER_PINYIN_FIRST_CHAR = "singer_pinyin_first_char";
        /** 13 歌名首注音 */
        public static final String COLUMN_TITLE_ZHUYIN_FIRST_CHAR = "title_zhuyin_first_char";
        /** 14 歌名首拼音 */
        public static final String COLUMN_TITLE_PINYIN_FIRST_CHAR = "title_pinyin_first_char";
        /** 15 性別團體 */
        public static final String COLUMN_SEX = "sex";
        /** 16 合唱歌者 */
        public static final String COLUMN_FEATURING = "featuring";
        /** 17 對唱 */
        public static final String COLUMN_ANTIPHONAL = "antiphonal";
        /** 18 帶商 */
        public static final String COLUMN_VENDOR = "vendor";
        /** 19 點播 */
        public static final String COLUMN_REQUEST_COUNT = "request_count";
        /** 20 新歌 */
        public static final String COLUMN_IS_NEW = "is_new";
        /** 21 導唱 */
        public static final String COLUMN_VOCAL = "vocal";
        /** 22 日期 */
        public static final String COLUMN_ADD_DATE = "add_date";
        /** 23 活動標籤 */
        public static final String COLUMN_ACTIVITY_TAG1 = "activity_tag1";
        /** 24 音量 */
        public static final String COLUMN_VOLUME = "volume";
        /** 25 描述 */
        public static final String COLUMN_DESCRIPTION = "description";
        /** 26 filecd */
        public static final String COLUMN_FILE_CD = "file_cd";
        /** 27 sort */
        public static final String COLUMN_SORT = "sort";
        /** 28 rlsound */
        public static final String COLUMN_RLSOUND = "rlsound";
        /** 29 rlsound2 */
        public static final String COLUMN_RLSOUND2 = "rlsound2";
        /** 30 ch */
        public static final String COLUMN_CH = "ch";
        /** 31 add_no */
        public static final String COLUMN_ADD_NO = "add_no";
        /** 32 light */
        public static final String COLUMN_LIGHT = "light";
        /** 33 NEWER */
        public static final String COLUMN_NEWER = "newer";
        /** 34 son1 */
        public static final String COLUMN_SON1 = "son1";
        /** 35 son2 */
        public static final String COLUMN_SON2 = "son2";
        /** 36 son3 */
        public static final String COLUMN_SON3 = "son3";
        /** 37 ok */
        public static final String COLUMN_OK = "ok";
        /** 38 machine */
        public static final String COLUMN_MACHINE = "machine";
        /** 39 video_length */
        public static final String COLUMN_LENGTH = "video_length";
        /** 40 tv */
        public static final String COLUMN_TV = "tv";
        /** 41 activity_2 */
        public static final String COLUMN_ACTIVITY_TAG2 = "activity_tag2";
        /** 42 activity_3 */
        public static final String COLUMN_ACTIVITY_TAG3 = "activity_tag3";


    }


    /** */
    public static class Marquee {
        public static final String TABLE_NAME = "marquee";
        /** 0 index */
        public static final String COLUMN_INDEX = "idx";
        /** 1 時間 */
        public static final String COLUMN_TIME = "time";
        /** 2 內容 */
        public static final String COLUMN_CONTENT = "content";
    }

    /** */
    public static class Advertising {
        public static final String TABLE_NAME = "advertising";
        /** 0 index */
        public static final String COLUMN_INDEX = "idx";
        /** 1 名稱 */
        public static final String COLUMN_NAME = "name";
        /** 2 檔名 */
        public static final String COLUMN_VIDEO_FILE = "video_file";
        /** 3 音量 */
        public static final String COLUMN_VOLUME = "volume";
        /** 4 聲音 */
        public static final String COLUMN_MUTE = "mute";
    }

    /** */
    public static class Service {
        public static final String TABLE_NAME = "service";
        /** 0 index */
        public static final String COLUMN_INDEX = "idx";
        /** 1 類別 */
        public static final String COLUMN_CATEGORY = "category";
        /** 2 名稱 */
        public static final String COLUMN_NAME = "name";
        /** 3 最大數量 */
        public static final String COLUMN_MAX = "max";
    }

    /** */
    public static class Meal {
        public static final String TABLE_NAME = "meal";
        /** 0 index */
        public static final String COLUMN_INDEX = "idx";
        /** 1 類別 */
        public static final String COLUMN_CATEGORY = "category";
        /** 2 名稱 */
        public static final String COLUMN_NAME = "name";
        /** 3 最大數量 */
        public static final String COLUMN_MAX = "max";
        /** 4 價格 */
        public static final String COLUMN_PRICE = "price";
        /** 5 額外1 */
        public static final String COLUMN_EXTRA1 = "extra1";
        /** 6 額外1價格 */
        public static final String COLUMN_EXTRA1_PRICE = "extra1_price";
        /** 5 額外21 */
        public static final String COLUMN_EXTRA2 = "extra2";
        /** 6 額外2價格 */
        public static final String COLUMN_EXTRA2_PRICE = "extra2_price";
        /** 5 額外3 */
        public static final String COLUMN_EXTRA3 = "extra3";
        /** 6 額外3價格 */
        public static final String COLUMN_EXTRA3_PRICE = "extra3_price";
    }
}
