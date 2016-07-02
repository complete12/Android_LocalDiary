/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : DB helper 클래스*/
package mobile.project.ma02_20131126;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDBHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "contact_db";
	public final static String TABLE_NAME = "contact_table";
	
	public ContactDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " ( _id integer primary key autoincrement, "
				+ "name TEXT, memo TEXT, info TEXT, mapx double, mapy double, favorite integer);");
	
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '맛집1', '2', '정보1', 37.0, 128.0, 0);");
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '지역1', '3', '정보2', 36.0, 129.0, 0);");
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '맛집2', '4', '정보3', 35.0, 129.0, 0);");
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '맛집3', '5', '정보4', 37.0, 126.0, 0);");
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '지역2', '6', '정보5', 38.0, 127.0, 0);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
