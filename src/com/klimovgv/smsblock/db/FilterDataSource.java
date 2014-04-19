package com.klimovgv.smsblock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.klimovgv.smsblock.filters.ISmsFilter;
import com.klimovgv.smsblock.models.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterDataSource extends DataSourceBase {

    private String[] allColumns = {
            MySQLiteHelper.COLUMN_FILTER_ID,
            MySQLiteHelper.COLUMN_FILTER_NAME,
            MySQLiteHelper.COLUMN_FILTER_PRIORITY,
            MySQLiteHelper.COLUMN_FILTER_IS_BLOCK,
            MySQLiteHelper.COLUMN_FILTER_PHONE_NUMBER,
            MySQLiteHelper.COLUMN_FILTER_IS_KNOWN_NUMBER,
    };


    public FilterDataSource(Context context) {
        super(context);
    }

    private String getIdColumnName() {
        return MySQLiteHelper.COLUMN_FILTER_ID;
    }

    private String getTableName() {
        return MySQLiteHelper.TABLE_FILTERS;
    }

    public void saveFilter(Filter filter) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_FILTER_NAME, filter.getName());
        values.put(MySQLiteHelper.COLUMN_FILTER_PRIORITY, filter.getPriority());
        values.put(MySQLiteHelper.COLUMN_FILTER_IS_BLOCK, filter.isBlock() ? 1 : 0);
        values.put(MySQLiteHelper.COLUMN_FILTER_PHONE_NUMBER, filter.getPhoneNumber());
        values.put(MySQLiteHelper.COLUMN_FILTER_IS_KNOWN_NUMBER, filter.isKnownNumberFilter() ? 1 : 0);

        long insertId = database.insert(getTableName(), null, values);

//        Cursor cursor = database.query(getTableName(),
//                allColumns, getIdColumnName() + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//
//        Filter newFilter = cursorToFilter(cursor);
//        cursor.close();
    }

    public void deleteFilter(Filter filter) {
        long id = filter.getId();

        database.delete(getTableName(), getIdColumnName() + " = " + id, null);
    }

    public List<ISmsFilter> getAllFilters() {
        List<Filter> filterList = new ArrayList<Filter>();

        Cursor cursor = database.query(getTableName(), allColumns, null, null, null, null, getIdColumnName() +" DESC");

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Filter filter = cursorToFilter(cursor);
            filterList.add(filter);

            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();


        List<ISmsFilter> smsFilters = new ArrayList<ISmsFilter>();
        for (int i = 0; i < filterList.size(); i++) {
            smsFilters.add(filterList.get(i).toSmsFilter());
        }

        return smsFilters;
    }

    private Filter cursorToFilter(Cursor cursor) {
        return new Filter(cursor.getLong(0), cursor.getString(1), cursor.getInt(2),
                cursor.getInt(3) == 1, cursor.getString(4), cursor.getInt(5) == 1);
    }


}
