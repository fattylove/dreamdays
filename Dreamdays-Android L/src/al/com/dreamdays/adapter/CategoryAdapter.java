package al.com.dreamdays.adapter;

import java.util.ArrayList;
import java.util.List;

import al.com.dreamdays.activity.ALHomeActivity;
import al.com.dreamdays.base.BaseApplication;
import al.com.dreamdays.sqlite.Category;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class CategoryAdapter extends BaseAdapter {

	private Context mContext;
	private List<Category> mCategoryList = new ArrayList<Category>();
	private int mCurrentPosition;


	public CategoryAdapter(Context context, List<Category> categoryList) {
		this.mContext = context;
		this.mCategoryList = categoryList;
	}
	
	public void setPosition(int position){
		this.mCurrentPosition = position;
	}

	public void addCategory(List<Category> categoryList){
		mCategoryList.addAll(categoryList);
		this.notifyDataSetChanged();
	}
	
	public void clear(){
		mCategoryList.clear();
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mCategoryList.size();
	}

	public List<Category> getmCategoryList() {
		return mCategoryList;
	}

	public void setmCategoryList(List<Category> mCategoryList) {
		this.mCategoryList = mCategoryList;
	}

	@Override
	public Object getItem(int position) {
		return mCategoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.al_dreamdays_category_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.itemText = (TextView) convertView.findViewById(R.id.category_item_title);
			viewHolder.itemIcon = (ImageView) convertView.findViewById(R.id.category_item_icon);
			viewHolder.itemCheck = (ImageView) convertView.findViewById(R.id.category_item_check);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Category category = mCategoryList.get(position);
		
		if (mCurrentPosition == category.get_id()) {
			viewHolder.itemCheck.setVisibility(View.VISIBLE);
		} else {
			viewHolder.itemCheck.setVisibility(View.INVISIBLE);
		}
		
		ALHomeActivity.setBlackIcon(viewHolder.itemIcon, mContext, category.get_id());
		
		viewHolder.itemText.setText(category.getCategoryName());
		viewHolder.itemText.setTypeface(BaseApplication.typeface_medium);
		return convertView;
	}

	public class ViewHolder {
		TextView itemText;
		ImageView itemIcon;
		ImageView itemCheck;
	}
}
