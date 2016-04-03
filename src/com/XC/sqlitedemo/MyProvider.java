package com.XC.sqlitedemo;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MyProvider extends ContentProvider {
	public static final String AUTHORITY = "com.XC.sqlitedemo.MyTestProvider";
	private static final int ONEPERSON = 1;
	private static final int PERSONS = 2;
	private static Map<String, String> projectionMap;
	private SqlHelper sqlHelper;
	private static UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "persons/#", ONEPERSON);
		uriMatcher.addURI(AUTHORITY, "persons", PERSONS);
		projectionMap = new HashMap<String, String>();
		projectionMap.put("id", "_id");
		projectionMap.put("name", "name");
		projectionMap.put("age", "age");
		projectionMap.put("tips", "tips");
		// projectionMap.put("note", "note");
	}

	@Override
	public boolean onCreate() {
		sqlHelper = new SqlHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder sb = new SQLiteQueryBuilder();
		sb.setTables("MytestSql");
		switch (uriMatcher.match(uri)) {
		case PERSONS:
			sb.setProjectionMap(projectionMap);
			break;
		case ONEPERSON:
			sb.setProjectionMap(projectionMap);
			sb.appendWhere("_id=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		Cursor query = sb.query(db, projection, selection, selectionArgs, null,
				null, TextUtils.isEmpty(sortOrder) ? null : sortOrder);
		query.setNotificationUri(getContext().getContentResolver(), uri);
		return query;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case ONEPERSON:
			return "vnd.android.cursor.item/oneperson";
		case PERSONS:
			return "vnd.android.cursor.dir/persons";
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (uriMatcher.match(uri) == -1) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		int id = sqlHelper.insert(values);
		if (id > 0) {
			Uri noteUri = ContentUris.withAppendedId(uri, id);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		} else {
			throw new SQLException("Failed to insert row into " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		int delete = 0;
		switch (uriMatcher.match(uri)) {
		case PERSONS:
			delete = db.delete(SqlHelper.name, selection, selectionArgs);
			break;
		case ONEPERSON:
			String id = uri.getPathSegments().get(1);
			delete = db.delete(SqlHelper.name,
					"_id = "
							+ id
							+ (TextUtils.isEmpty(selection) ? ""
									: ("and " + selection)), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return delete;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		int update = 0;
		switch (uriMatcher.match(uri)) {
		case PERSONS:
			update = db
					.update(SqlHelper.name, values, selection, selectionArgs);
			break;
		case ONEPERSON:
			String id = uri.getPathSegments().get(1);
			update = db.update(SqlHelper.name, values,
					"_id = "
							+ id
							+ (TextUtils.isEmpty(selection) ? ""
									: ("and " + selection)), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return update;
	}

}
