package al.com.dreamdays.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class MatterService {

	
	/**
	 * 是否包含这个事件
	 * 
	 * @param context
	 *            上下文对象
	 * @param matterId
	 *            事件id
	 * @return
	 */
	public boolean ifHasThisMatter(Context context, int matterId) {
		boolean ifHasMatter = false;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		Cursor c = db.rawQuery("select id from app_matter where id = ?", new String[] { matterId + "" });
		while (c.moveToNext()) {
			ifHasMatter = true;
		}
		c.close();
		db.close();
		return ifHasMatter;
	}

	/**
	 * 添加一条事件数据
	 * 
	 * @param context
	 *            上下文对象
	 * @param matter
	 *            事件对象
	 */
	public void insertOrUpdateNewMatter(Context context, Matter matter) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 1);
		if (ifHasThisMatter(context, matter.get_id())) {
			if (matter.getIfStick() > 0) {
				db.execSQL("update app_matter set if_stict=0");
			}
			db.execSQL(
					"update app_matter set " +
					"matter_name = ?," +
					"matter_time = ?," +
					"create_time = ?," +
					"if_notify = ?," +
					"if_stict=?," +
					"repeat_type=?," +
					"classify_type=?," +
					"acc_video_name=?," +
					"pic_name=? where id = " + matter.get_id(),
					new String[] { 
							matter.getMatterName(),
							matter.getMatterTime(), 
							matter.getCreateTime(),
							matter.getIf_notify() + "",
							matter.getIfStick() + "",
							matter.getRepeat_type() + "",
							matter.getClassifyType() + "",
							matter.getVideoName(), matter.getPicName() });
			db.close();
		} else {
			ContentValues cv = new ContentValues();
			cv.put("matter_name", matter.getMatterName());
			cv.put("matter_time", matter.getMatterTime());
			cv.put("create_time", matter.getCreateTime());
			cv.put("if_notify", matter.getIf_notify());
			cv.put("if_stict", matter.getIfStick());
			cv.put("classify_type", matter.getClassifyType());
			cv.put("acc_video_name", matter.getVideoName());
			cv.put("pic_name", matter.getPicName());
			cv.put("sort_id", matter.getSort_id());
			cv.put("repeat_type", matter.getRepeat_type());

			long rowId = db.insert("app_matter", null, cv);

			if (matter.getIfStick() > 0) {
				db.execSQL("update app_matter set if_stict=0 WHERE id<" + rowId);
			}
		}
		db.close();
	}
	
	
	/**
	 * 添加一条事件数据
	 * 
	 * @param context
	 *            上下文对象
	 * @param matter
	 *            事件对象
	 */
	public void updateVoiceMatter(Context context, Matter matter) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 1);
		if (ifHasThisMatter(context, matter.get_id())) {
			db.execSQL("update app_matter set " + "acc_video_name=? where id = " + matter.get_id(), new String[] { matter.getVideoName() });
			db.close();
		} 
		db.close();
	}
	
	/**
	 * 更新
	 * 
	 * @param context
	 * @param matter
	 */
	public void updateNotify(Context context, Matter matter){
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 1);
		ContentValues cv = new ContentValues();
		cv.put("id", matter.get_id());
		cv.put("matter_name", matter.getMatterName());
		cv.put("matter_time", matter.getMatterTime());
		cv.put("create_time", matter.getCreateTime());
		cv.put("if_notify", 0);
		cv.put("if_stict", matter.getIfStick());
		cv.put("classify_type", matter.getClassifyType());
		cv.put("acc_video_name", matter.getVideoName());
		cv.put("pic_name", matter.getPicName());
		cv.put("sort_id", matter.getSort_id());
		cv.put("repeat_type", matter.getRepeat_type());
		db.replace("app_matter", null, cv);
		db.close();
	}

	public synchronized void insertMatters(Context context, ArrayList<Matter> matters) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 1);
		for(Matter matter : matters){
			ContentValues cv = new ContentValues();
			cv.put("matter_name", matter.getMatterName());
			cv.put("matter_time", matter.getMatterTime());
			cv.put("create_time", matter.getCreateTime());
			cv.put("if_notify", matter.getIf_notify());
			cv.put("if_stict", matter.getIfStick());
			cv.put("classify_type", matter.getClassifyType());
			cv.put("acc_video_name", matter.getVideoName());
			cv.put("pic_name", matter.getPicName());
			cv.put("sort_id", matter.getSort_id());
			cv.put("repeat_type", matter.getRepeat_type());
			db.insert("app_matter", null, cv);
		}
		db.close();
	}
	
	/**
	 * 根据id删除一条事件
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param matterId
	 *            事件id
	 */
	public void deleteMatterById(Context mContext, int matterId) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(mContext, 1);
		db.execSQL("delete from app_matter where id = ?", new Integer[] { matterId });
		db.close();
	}
	
	public synchronized void deleteMatters(Context mContext) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(mContext, 1);
		db.execSQL("delete from app_matter;");
		db.close();
	}

	public List<Matter> queryMatterByCategory(Context context, int category_id) {
		List<Matter> matterList = new ArrayList<Matter>();
		Matter matter = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "";
		if (category_id != 0) {
			selectSQL = "select * from app_matter where if_stict = 0 and classify_type = " + category_id + " order by sort_id desc";
		} else {
			selectSQL = "select * from app_matter where if_stict = 0 order by sort_id ,id desc";
		}
		Cursor c = db.rawQuery(selectSQL, null);
		while (c.moveToNext()) {
			matter = new Matter();
			matter.set_id(c.getInt(c.getColumnIndex("id")));
			matter.setMatterName(c.getString(c.getColumnIndex("matter_name")));
			matter.setMatterTime(c.getString(c.getColumnIndex("matter_time")));
			matter.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			matter.setIf_notify(c.getInt(c.getColumnIndex("if_notify")));
			matter.setClassifyType(c.getInt(c.getColumnIndex("classify_type")));
			matter.setVideoName(c.getString(c.getColumnIndex("acc_video_name")));
			matter.setPicName(c.getString(c.getColumnIndex("pic_name")));
			matter.setIfStick(c.getInt(c.getColumnIndex("if_stict")));
			matter.setSort_id(c.getInt(c.getColumnIndex("sort_id")));
			matter.setRepeat_type(c.getInt(c.getColumnIndex("repeat_type")));
			matterList.add(matter);
		}
		c.close();
		db.close();
		return matterList;
	}
	
	
	public ArrayList<Matter> queryMattersByCategory(Context context, int category_id) {
		ArrayList<Matter> matterList = new ArrayList<Matter>();
		Matter matter = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select * from app_matter where classify_type = " + category_id + " order by sort_id desc";
		Cursor c = db.rawQuery(selectSQL, null);
		while (c.moveToNext()) {
			matter = new Matter();
			matter.set_id(c.getInt(c.getColumnIndex("id")));
			matter.setMatterName(c.getString(c.getColumnIndex("matter_name")));
			matter.setMatterTime(c.getString(c.getColumnIndex("matter_time")));
			matter.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			matter.setIf_notify(c.getInt(c.getColumnIndex("if_notify")));
			matter.setClassifyType(c.getInt(c.getColumnIndex("classify_type")));
			matter.setVideoName(c.getString(c.getColumnIndex("acc_video_name")));
			matter.setPicName(c.getString(c.getColumnIndex("pic_name")));
			matter.setIfStick(c.getInt(c.getColumnIndex("if_stict")));
			matter.setSort_id(c.getInt(c.getColumnIndex("sort_id")));
			matter.setRepeat_type(c.getInt(c.getColumnIndex("repeat_type")));
			matterList.add(matter);
		}
		c.close();
		db.close();
		return matterList;
	}
	
	public List<Matter> queryAllMatter(Context context) {
		List<Matter> matterList = new ArrayList<Matter>();
		Matter matter = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select * from app_matter";
		Cursor c = db.rawQuery(selectSQL, null);
		while (c.moveToNext()) {
			matter = new Matter();
			matter.set_id(c.getInt(c.getColumnIndex("id")));
			matter.setMatterName(c.getString(c.getColumnIndex("matter_name")));
			matter.setMatterTime(c.getString(c.getColumnIndex("matter_time")));
			matter.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			matter.setIf_notify(c.getInt(c.getColumnIndex("if_notify")));
			matter.setClassifyType(c.getInt(c.getColumnIndex("classify_type")));
			matter.setVideoName(c.getString(c.getColumnIndex("acc_video_name")));
			matter.setPicName(c.getString(c.getColumnIndex("pic_name")));
			matter.setIfStick(c.getInt(c.getColumnIndex("if_stict")));
			matter.setSort_id(c.getInt(c.getColumnIndex("sort_id")));
			matter.setRepeat_type(c.getInt(c.getColumnIndex("repeat_type")));
			matterList.add(matter);
		}
		c.close();
		db.close();
		return matterList;
	}
	
	public List<Matter> queryAllMatterExitStickMatter(Context context) {
		List<Matter> matterList = new ArrayList<Matter>();
		Matter matter = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select * from app_matter where if_stict<1 order by sort_id ,id desc";
		Cursor c = db.rawQuery(selectSQL, null);
		while (c.moveToNext()) {
			matter = new Matter();
			matter.set_id(c.getInt(c.getColumnIndex("id")));
			matter.setMatterName(c.getString(c.getColumnIndex("matter_name")));
			matter.setMatterTime(c.getString(c.getColumnIndex("matter_time")));
			matter.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			matter.setIf_notify(c.getInt(c.getColumnIndex("if_notify")));
			matter.setClassifyType(c.getInt(c.getColumnIndex("classify_type")));
			matter.setVideoName(c.getString(c.getColumnIndex("acc_video_name")));
			matter.setPicName(c.getString(c.getColumnIndex("pic_name")));
			matter.setIfStick(c.getInt(c.getColumnIndex("if_stict")));
			matter.setSort_id(c.getInt(c.getColumnIndex("sort_id")));
			matter.setRepeat_type(c.getInt(c.getColumnIndex("repeat_type")));
			matterList.add(matter);
		}
		c.close();
		db.close();
		return matterList;
	}
	
	public List<Matter> queryAllMatter(Context context , int if_notify) {
		List<Matter> matterList = new ArrayList<Matter>();
		Matter matter = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select * from app_matter where if_notify = "+if_notify+ ";";
		Cursor c = db.rawQuery(selectSQL, null);
		while (c.moveToNext()) {
			matter = new Matter();
			matter.set_id(c.getInt(c.getColumnIndex("id")));
			matter.setMatterName(c.getString(c.getColumnIndex("matter_name")));
			matter.setMatterTime(c.getString(c.getColumnIndex("matter_time")));
			matter.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			matter.setIf_notify(c.getInt(c.getColumnIndex("if_notify")));
			matter.setClassifyType(c.getInt(c.getColumnIndex("classify_type")));
			matter.setVideoName(c.getString(c.getColumnIndex("acc_video_name")));
			matter.setPicName(c.getString(c.getColumnIndex("pic_name")));
			matter.setIfStick(c.getInt(c.getColumnIndex("if_stict")));
			matter.setSort_id(c.getInt(c.getColumnIndex("sort_id")));
			matter.setRepeat_type(c.getInt(c.getColumnIndex("repeat_type")));
			matterList.add(matter);
		}
		c.close();
		db.close();
		return matterList;
	}
	
	public List<Matter> queryAllRepeatMatter(Context context) {
		List<Matter> matterList = new ArrayList<Matter>();
		Matter matter = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select * from app_matter where repeat_type>0";
		Cursor c = db.rawQuery(selectSQL, null);
		while (c.moveToNext()) {
			matter = new Matter();
			matter.set_id(c.getInt(c.getColumnIndex("id")));
			matter.setMatterName(c.getString(c.getColumnIndex("matter_name")));
			matter.setMatterTime(c.getString(c.getColumnIndex("matter_time")));
			matter.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			matter.setIf_notify(c.getInt(c.getColumnIndex("if_notify")));
			matter.setClassifyType(c.getInt(c.getColumnIndex("classify_type")));
			matter.setVideoName(c.getString(c.getColumnIndex("acc_video_name")));
			matter.setPicName(c.getString(c.getColumnIndex("pic_name")));
			matter.setIfStick(c.getInt(c.getColumnIndex("if_stict")));
			matter.setSort_id(c.getInt(c.getColumnIndex("sort_id")));
			matter.setRepeat_type(c.getInt(c.getColumnIndex("repeat_type")));
			matterList.add(matter);
		}
		c.close();
		db.close();
		return matterList;
	}

	public Matter queryStickMatter(Context context) {
		Matter matter = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select * from app_matter where if_stict = 1 order by sort_id desc limit 1";
		Cursor c = db.rawQuery(selectSQL, null);
		while (c.moveToNext()) {
			matter = new Matter();
			matter.set_id(c.getInt(c.getColumnIndex("id")));
			matter.setMatterName(c.getString(c.getColumnIndex("matter_name")));
			matter.setMatterTime(c.getString(c.getColumnIndex("matter_time")));
			matter.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			matter.setIf_notify(c.getInt(c.getColumnIndex("if_notify")));
			matter.setClassifyType(c.getInt(c.getColumnIndex("classify_type")));
			matter.setVideoName(c.getString(c.getColumnIndex("acc_video_name")));
			matter.setPicName(c.getString(c.getColumnIndex("pic_name")));
			matter.setIfStick(c.getInt(c.getColumnIndex("if_stict")));
			matter.setSort_id(c.getInt(c.getColumnIndex("sort_id")));
			matter.setRepeat_type(c.getInt(c.getColumnIndex("repeat_type")));
		}
		c.close();
		db.close();
		return matter;
	}
	
	public Matter queryStickMatterById(Context context , int id) {
		Matter matter = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select * from app_matter where if_stict = 1 and id = "+ id;
		Cursor c = db.rawQuery(selectSQL, null);
		while (c.moveToNext()) {
			matter = new Matter();
			matter.set_id(c.getInt(c.getColumnIndex("id")));
			matter.setMatterName(c.getString(c.getColumnIndex("matter_name")));
			matter.setMatterTime(c.getString(c.getColumnIndex("matter_time")));
			matter.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			matter.setIf_notify(c.getInt(c.getColumnIndex("if_notify")));
			matter.setClassifyType(c.getInt(c.getColumnIndex("classify_type")));
			matter.setVideoName(c.getString(c.getColumnIndex("acc_video_name")));
			matter.setPicName(c.getString(c.getColumnIndex("pic_name")));
			matter.setIfStick(c.getInt(c.getColumnIndex("if_stict")));
			matter.setSort_id(c.getInt(c.getColumnIndex("sort_id")));
			matter.setRepeat_type(c.getInt(c.getColumnIndex("repeat_type")));
		}
		c.close();
		db.close();
		return matter;
	}

	/**
	 * 
	 * @param context
	 * @param matter_id
	 * @return
	 */
	public Matter queryMatterById(Context context, int matter_id) {
		Matter matter = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select * from app_matter where id = ? order by sort_id desc";
		Cursor c = db.rawQuery(selectSQL, new String[] { matter_id + "" });
		while (c.moveToNext()) {
			matter = new Matter();
			matter.set_id(c.getInt(c.getColumnIndex("id")));
			matter.setMatterName(c.getString(c.getColumnIndex("matter_name")));
			matter.setMatterTime(c.getString(c.getColumnIndex("matter_time")));
			matter.setCreateTime(c.getString(c.getColumnIndex("create_time")));
			matter.setIf_notify(c.getInt(c.getColumnIndex("if_notify")));
			matter.setClassifyType(c.getInt(c.getColumnIndex("classify_type")));
			matter.setVideoName(c.getString(c.getColumnIndex("acc_video_name")));
			matter.setPicName(c.getString(c.getColumnIndex("pic_name")));
			matter.setIfStick(c.getInt(c.getColumnIndex("if_stict")));
			matter.setSort_id(c.getInt(c.getColumnIndex("sort_id")));
			matter.setRepeat_type(c.getInt(c.getColumnIndex("repeat_type")));
		}
		c.close();
		db.close();
		return matter;
	}

	/**
	 * 是否包含这个事件
	 * 
	 * @param context
	 *            上下文对象
	 * @return maxId
	 */
	public int getMaxId(Context context) {
		int maxId = 1;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		Cursor c = db.rawQuery("select max(id) as id from app_matter", null);
		while (c.moveToNext()) {
			maxId = c.getInt(c.getColumnIndex("id"));
		}
		c.close();
		db.close();
		return maxId;
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public int getCount(Context context) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		Cursor c = db.rawQuery("select * from app_matter", null);
		int a = c.getCount();
		c.close();
		db.close();
		return a;
	}

	/**
	 * 
	 * @param context
	 * @param videoName
	 * @param category_id
	 * @return
	 */
	public boolean updateVideoName(Context context, String videoName, int category_id) {
		if (category_id > 0 && !videoName.equals("")) {
			SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 1);
			db.rawQuery("update app_matter set acc_video_name = ? where id =?", new String[] { videoName, category_id + "" });
			db.close();
			return true;
		}
		return false;
	}

	/**
	 * update Gategory
	 * 
	 * @param context
	 * @param matterId
	 */
	public void updateCategory(Context context, int matterId) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 1);
		db.execSQL("update app_matter set classify_type = 1 where id = " + matterId);
		db.close();
	}

}
