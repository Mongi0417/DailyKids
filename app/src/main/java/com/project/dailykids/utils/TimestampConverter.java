package com.project.dailykids.utils;

import java.text.SimpleDateFormat;

public class TimestampConverter {
    public static String timestampToDate(long timestamp) {
        return new SimpleDateFormat("yyyy년 MM월 dd일").format(timestamp);
    }

    public static String timestampToDateAndTime(long timestamp) {
        return new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm").format(timestamp);
    }

    public static String timestampToDateWithDot(long timestamp) {
        return new SimpleDateFormat("yyyy.MM.dd").format(timestamp);
    }
}
