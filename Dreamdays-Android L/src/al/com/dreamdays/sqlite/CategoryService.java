package al.com.dreamdays.sqlite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.guxiu.dreamdays.R;

public class CategoryService {

	/**
	 * 根据Id查询Category
	 * 
	 * @param context
	 *            上下文对象
	 * @param categoryId
	 *            类别Id
	 * @return 类别对象
	 */
	public Category queryCategoryById(Context context, int categoryId) {
		Category category = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select id,classify_name from app_classify where id = ? order by id desc";
		Cursor c = db.rawQuery(selectSQL, new String[] { categoryId + "" });
		while (c.moveToNext()) {
			category = new Category();
			category.set_id(c.getInt(c.getColumnIndex("id")));
			category.setCategoryName(c.getString(c.getColumnIndex("classify_name")));
		}
		c.close();
		db.close();
		return category;
	}

	/**
	 * 根据Id查询Category
	 * 
	 * @param context
	 *            上下文对象
	 * @param categoryId
	 *            类别Id
	 * @return 类别对象
	 */
	public int queryIdByName(Context context, String classifyName) {
		int categoryId = 1;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select id from app_classify where classify_name = ?";
		Cursor c = db.rawQuery(selectSQL, new String[] { classifyName });
		while (c.moveToNext()) {
			categoryId = c.getInt(c.getColumnIndex("id"));
		}
		c.close();
		db.close();
		return categoryId;
	}

	/**
	 * 查询分类列表
	 * 
	 * @param context
	 *            上下文对象
	 * @return 类别列表
	 */
	public ArrayList<Category> queryCategoryList(Context context) {
		ArrayList<Category> categoryList = new ArrayList<Category>();
		Category category = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select id,classify_name from app_classify order by id";

		Cursor c = db.rawQuery(selectSQL, null);
		while (c.moveToNext()) {
			category = new Category();
			category.set_id(c.getInt(c.getColumnIndex("id")));
			category.setCategoryName(c.getString(c.getColumnIndex("classify_name")));
			categoryList.add(category);
		}
		c.close();
		db.close();
		return categoryList;
	}

	
	public ArrayList<Category> queryHavedCategory(Context context) {
		ArrayList<Category> categoryList = new ArrayList<Category>();
		Category category = null;
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		String selectSQL = "select id,classify_name from app_classify where id in (select classify_type from app_matter group by classify_type)";

		Cursor c = db.rawQuery(selectSQL, null);
		while (c.moveToNext()) {
			category = new Category();
			category.set_id(c.getInt(c.getColumnIndex("id")));
			category.setCategoryName(c.getString(c.getColumnIndex("classify_name")));
			categoryList.add(category);
		}
		c.close();
		db.close();
		return categoryList;
	}
	
	/**
	 * 
	 * @param context
	 * @param cate_name
	 * @return
	 */
	public int addCategory(Context context, String cate_name) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		ContentValues cv = new ContentValues();
		cv.put("classify_name", cate_name);
		int rowID = (int) db.insert("app_classify", null, cv);
		db.close();
		if (rowID > 0) {
			return rowID;
		}
		return 0;
	}
	
	public void replaceCategory(Context context, ArrayList<Category> categories) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 1);
		for(Category category : categories){
			ContentValues cv = new ContentValues();
			cv.put("id", category.get_id());
			cv.put("classify_name", category.getCategoryName());
			db.replace("app_classify", null, cv);
		}
		db.close();
	}
	
	public void insertDefaultCategory(Context context){
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 1);
		db.execSQL("insert into [app_classify] (id,classify_name) values (1,'"+context.getResources().getString(R.string.d_Anniversary)+"');");
		db.execSQL("insert into [app_classify] (id,classify_name) values (2,'"+context.getResources().getString(R.string.d_Birthday)+"');	");
		db.execSQL("insert into [app_classify] (id,classify_name) values (3,'"+context.getResources().getString(R.string.d_Holiday)+"');");
		db.execSQL("insert into [app_classify] (id,classify_name) values (4,'"+context.getResources().getString(R.string.d_School)+"');");
		db.execSQL("insert into [app_classify] (id,classify_name) values (5,'"+context.getResources().getString(R.string.d_Life)+"');");
		db.execSQL("insert into [app_classify] (id,classify_name) values (6,'"+context.getResources().getString(R.string.d_tour)+"');");
		db.close();
	}

	/**
	 * 
	 * @param context
	 * @param cate_id
	 * @return
	 */
	public boolean deleteCategoryById(Context context, int cate_id) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		db.execSQL("delete from app_classify where id = ?", new Integer[] { cate_id });
		db.close();
		return true;
	}
	
	public boolean deleteCategory(Context context) {
		SQLiteDatabase db = SqliteHelper.getSQLiteDataBase(context, 0);
		db.execSQL("delete from app_classify;");
		db.close();
		return true;
	}
}
