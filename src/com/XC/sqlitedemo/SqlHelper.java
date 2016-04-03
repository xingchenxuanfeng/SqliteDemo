package com.XC.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHelper extends SQLiteOpenHelper {
	public static final String name = "MytestSql";
	private static final int version = 4;

	public SqlHelper(Context context) {
		this(context, name, null, version);
	}

	public SqlHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists MytestSql (_id integer primary key autoincrement, name nvarchar(20),age integer,tips text )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("drop table if exists MytestSql");
		// onCreate(db);
		if (oldVersion == 2 && newVersion == 3) {
			db.execSQL("alter table MytestSql add 'note' text");
		}
		if (oldVersion==3&&newVersion==4) {
			db.execSQL("drop table if exists MytestSql");
			onCreate(db);
		}
	}

	public Cursor exec(String sql) {
		Cursor rawQuery = getWritableDatabase().rawQuery(sql, null);
		return rawQuery;
	}

	public int insert(ContentValues contentValues) {
		// ContentValues contentValues=new ContentValues();
		// contentValues.put("name", "ÕÅÈý");
		// contentValues.put("age", 15);
		// // contentValues.put("tips", "");
		long insert = getWritableDatabase().insert(name, "tips",
				contentValues);
		return (int) insert;
	}

	public Cursor getAll() {
		return exec("select * from " + name);
	}

}
