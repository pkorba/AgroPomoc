package com.piotrkorba.agropomoc;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Class responsible for type conversion. Allows using Date type in Room database.
 */
public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}

