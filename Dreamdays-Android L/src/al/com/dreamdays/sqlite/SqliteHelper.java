package al.com.dreamdays.sqlite;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class SqliteHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "matter_db";
	private static final int DATABASE_VERSION = 1;
	private Resources resources;

	public SqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		resources = context.getResources();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建数据库表语句
		db.execSQL("CREATE TABLE IF NOT EXISTS [app_matter] (" +
				"id integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
				"matter_name varchar," +
				"matter_time varchar," +
				"create_time varchar," +
				"if_notify integer," +
				"classify_type integer," +
				"if_stict integer," +
				"repeat_type integer," +
				"acc_video_name varchar," +
				"pic_name varchar," +
				"sort_id integer);");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS [app_classify] (" +
				"id integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
				"classify_name varchar);");

		// 初始化插入语句
		db.execSQL("insert into [app_classify] (classify_name) values ('"+resources.getString(R.string.d_Anniversary)+"');");
		db.execSQL("insert into [app_classify] (classify_name) values ('"+resources.getString(R.string.d_Birthday)+"');	");
		db.execSQL("insert into [app_classify] (classify_name) values ('"+resources.getString(R.string.d_Holiday)+"');");
		db.execSQL("insert into [app_classify] (classify_name) values ('"+resources.getString(R.string.d_School)+"');");
		db.execSQL("insert into [app_classify] (classify_name) values ('"+resources.getString(R.string.d_Life)+"');");
		db.execSQL("insert into [app_classify] (classify_name) values ('"+resources.getString(R.string.d_tour)+"');");

		// 初始化五个事件
		db.execSQL("insert into [app_matter] (matter_name,matter_time,create_time,if_notify,classify_type,if_stict,repeat_type,acc_video_name,pic_name,sort_id) values " +
				"('Dreamdays','December 23, 2012' ,'December 23, 2012' ,0 ,3 ,1 ,0,'','',5);");
		db.execSQL("insert into [app_matter] (matter_name,matter_time,create_time,if_notify,classify_type,if_stict,repeat_type,acc_video_name,pic_name,sort_id) values " +
				"('Christmas Day','December 25, 2015' ,'December 25, 2015' ,1 ,3 ,0 ,6,'','',3);");
		db.execSQL("insert into [app_matter] (matter_name,matter_time,create_time,if_notify,classify_type,if_stict,repeat_type,acc_video_name,pic_name,sort_id) values " +
				"('"+resources.getString(R.string.d_his_birthday) +"','February 24, 1955'  ,'February 24, 1955' ,0 ,2 ,0 ,0,'','',4);");
		db.execSQL("insert into [app_matter] (matter_name,matter_time,create_time,if_notify,classify_type,if_stict,repeat_type,acc_video_name,pic_name,sort_id) values " +
				"('"+resources.getString(R.string.d_first_sight)  +"','June 04, 2012'      ,'June 04, 2012' ,0 ,1 ,0 ,0,'','',1);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
	}

	/**
	 * 获取sqlite实例
	 * 
	 * @param context
	 *            上下文对象
	 * @param type
	 *            类型,0表示从应用内获取，1表示从sd卡获取
	 * @param ifWrite
	 *            类型,1表示写,0表示读
	 * @return
	 */
	public static SQLiteDatabase getSQLiteDataBase(Context context, int ifWrite) {
		SQLiteDatabase db = null;
		SqliteHelper dbHelper = new SqliteHelper(context);
		if (ifWrite == 1) {
			db = dbHelper.getWritableDatabase();
		} else {
			db = dbHelper.getReadableDatabase();
		}
		return db;
	}

}
