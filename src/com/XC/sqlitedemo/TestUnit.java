package com.XC.sqlitedemo;

import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.test.AndroidTestCase;

public class TestUnit extends AndroidTestCase {
	public void insert() {
		Context context = getContext();
		SqlHelper helper = new SqlHelper(context);
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", "ÕÅÈý");
		contentValues.put("age", 15);
		contentValues.put("tips", "abc");
		int exec = helper.insert(contentValues);
		System.out.println(exec);
	}

	public void getAll() {
		SqlHelper helper = new SqlHelper(getContext());
		Cursor all = helper.getAll();
		while (all.moveToNext()) {
			for (int i = 0; i < all.getColumnCount(); i++) {
				System.out.println(all.getColumnName(i) + "  "
						+ all.getString(i));
			}
		}
	}
}
