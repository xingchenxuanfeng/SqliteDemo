package com.XC.sqlitedemo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView lv=(ListView) findViewById(R.id.lv);
//		Cursor cursor = getContentResolver()
//				.query(ContactsContract.Contacts.CONTENT_URI ,
//						null, null, null, null);
		Cursor cursor = getContentResolver()
				.query(Uri
						.parse("content://com.XC.sqlitedemo.MyTestProvider/persons"),
						null, null, null, null);
		MyCursorAdapter adapter=new MyCursorAdapter(this, cursor);
		lv.setAdapter(adapter);
	}

	class MyCursorAdapter extends CursorAdapter {

		public MyCursorAdapter(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			TextView id = (TextView) arg0.findViewById(R.id.tid);
			TextView name = (TextView) arg0.findViewById(R.id.tname);
			TextView age = (TextView) arg0.findViewById(R.id.tage);
			TextView tips = (TextView) arg0.findViewById(R.id.tips);
			TextView note = (TextView) arg0.findViewById(R.id.note);
			id.setText(arg2.getString(arg2.getColumnIndex("_id")));
			name.setText(arg2.getString(arg2.getColumnIndex("name")));
			age.setText(arg2.getString(arg2.getColumnIndex("age")));
			tips.setText(arg2.getString(arg2.getColumnIndex("tips")));
//			note.setText(arg2.getString(arg2.getColumnIndex("note")));
			// id.setText(arg2.getString(arg2.getColumnIndex("id")));
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			LayoutInflater inflater = LayoutInflater.from(arg0);
			View view = inflater.inflate(R.layout.item, arg2, false);
			return view;
		}
	}
}
