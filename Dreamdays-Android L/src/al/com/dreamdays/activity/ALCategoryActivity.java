package al.com.dreamdays.activity;

import java.util.ArrayList;
import java.util.List;

import al.com.dreamdays.adapter.CategoryAdapter;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.sqlite.Category;
import al.com.dreamdays.sqlite.CategoryService;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.api.alertview.AlertBuilder;
import com.api.alertview.Effectstype;
import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALCategoryActivity extends BaseActivity {

	private ListView categoryListView;
	private TextView addGategoryEditText;
	private CategoryAdapter categoryAdapter;
	
	private List<Category> categoryList; 
	private LinearLayout bgLayout;
	int categoryID;
	
	public static final int REQUEST_CODE = 4004;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_dreamdays_category_layout);
		categoryListView = (ListView) findViewById(R.id.category_list_view);
		bgLayout = (LinearLayout)findViewById(R.id.bgLayout);
		bgLayout.setBackgroundColor(Color.parseColor("#90000000"));
		bgLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		CategoryService categoryService = new CategoryService();
		categoryList = categoryService.queryCategoryList(this);
		categoryID = getIntent().getIntExtra("categoryID", 0);
		
		//add footerView
		View contentView = LayoutInflater.from(this).inflate(R.layout.al_dreamdays_a_addcategory_layout, null);
		addGategoryEditText = (TextView) contentView.findViewById(R.id.category_item_title);
		addGategoryEditText.setTypeface(BaseApplication.typeface_medium);
		categoryListView.addFooterView(contentView);
		
		//adapter
		categoryAdapter = new CategoryAdapter(this, categoryList);
		categoryAdapter.setPosition(categoryID);
		categoryListView.setAdapter(categoryAdapter);
		categoryListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View currentView,	int position, long arg3) {
				Category c = (Category)categoryListView.getItemAtPosition(position);
				if(c!=null){
					Intent intent = new Intent();
					intent.putExtra("backCategoryId", c.get_id());
					ALCategoryActivity.this.setResult(RESULT_OK, intent);
					ALCategoryActivity.this.finish();
				}
			}
		});
		
		categoryListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Category category = (Category) categoryListView.getItemAtPosition(position);
				if(category!= null){
					if(category.get_id() > 6 ){
						deleteDialog(ALCategoryActivity.this , category.get_id());
					}
				}
				return true;
			}
		});
		
		addGategoryEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ALCategoryActivity.this, EditTextActivity.class);
				ALCategoryActivity.this.startActivityForResult(intent, REQUEST_CODE);
			}
		});

	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case REQUEST_CODE:
			
			String result = null;
			if(arg2!=null){
				result = arg2.getStringExtra("editor");
			}
			
			addGategoryEditText.setText(result);
			String categoryText = addGategoryEditText.getText().toString();
			CategoryService categoryService = new CategoryService();
			if(!TextUtils.isEmpty(categoryText)){
				categoryService.addCategory(ALCategoryActivity.this, categoryText);
				addGategoryEditText.setText("");
			}
			
			ArrayList<Category> categories = categoryService.queryCategoryList(ALCategoryActivity.this);
			categoryAdapter.clear();
			categoryAdapter.addCategory(categories);
			break;

		default:
			break;
		}
	}
	
	/**
	 * delete event Dailog
	 * 
	 * @param context
	 * @param id
	 */
	public void deleteDialog(final Context context , final int id) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			AlertDialog.Builder builder= new Builder(context , android.R.style.Theme_Material_Light_Dialog);
			builder.setMessage(R.string.al_delete_category_content);
			builder.setTitle(R.string.al_delete_category_title);

			builder.setPositiveButton(R.string.al_delete_btn,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							CategoryService categoryService = new CategoryService();
							categoryService.deleteCategoryById(context, id);
							ArrayList<Category> categories = categoryService.queryCategoryList(context);
							categoryAdapter.clear();
							categoryAdapter.addCategory(categories);
							categoryAdapter.setPosition(0);
							
							Intent intent = new Intent();
							intent.putExtra("deleteId", id);
							intent.setAction(ALAddEditEventActivity.CATEGORY_CHANGED_ACTION);
							ALCategoryActivity.this.sendBroadcast(intent);
							
							dialog.dismiss();
						}
					});

			builder.setNegativeButton(R.string.cancel ,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}else{
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			Effectstype effect = Effectstype.SlideBottom;
			dialogBuilder
			.withTitle(context.getString(R.string.al_delete_category_title))
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect(effect) 
			.withOkButtonText(context.getString(R.string.al_delete_btn)) 
			.withCancelButtonText(context.getString(R.string.cancel))
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					CategoryService categoryService = new CategoryService();
					categoryService.deleteCategoryById(context, id);
					ArrayList<Category> categories = categoryService.queryCategoryList(context);
					categoryAdapter.clear();
					categoryAdapter.addCategory(categories);
					categoryAdapter.setPosition(0);
					
					Intent intent = new Intent();
					intent.putExtra("deleteId", id);
					intent.setAction(ALAddEditEventActivity.CATEGORY_CHANGED_ACTION);
					ALCategoryActivity.this.sendBroadcast(intent);
					
					dialogBuilder.cancel();
				}
			}).setOnCacnelButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialogBuilder.cancel();
				}
			}).show();
			
		}
		
		
	}
	
	@Override
	public void finish() {
		bgLayout.setBackgroundColor(Color.TRANSPARENT);
		super.finish();
	}
	
	/**
	 *  on KeyDown back
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
