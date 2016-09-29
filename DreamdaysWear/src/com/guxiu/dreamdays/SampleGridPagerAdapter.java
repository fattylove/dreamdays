package com.guxiu.dreamdays;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.wearable.view.FragmentGridPagerAdapter;

public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {
	
	public static final int[] BG_IMAGES = new int[] {
        R.drawable.top_bg_1,
        R.drawable.top_bg_2,
        R.drawable.top_bg_3,
        R.drawable.top_bg_4,
        R.drawable.top_bg_5,
        R.drawable.top_bg_6,
        R.drawable.top_bg_7
    };
    
    public static final int[] ICON_IMAGES = new int[] {
        R.drawable.category_white_icon1,
        R.drawable.category_white_icon2,
        R.drawable.category_white_icon3,
        R.drawable.category_white_icon4,
        R.drawable.category_white_icon5,
        R.drawable.category_white_icon6,
        R.drawable.category_white_icon7
    };
	
    private List<Row> mRows;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<Matter> matters = new ArrayList<Matter>();
    
    public SampleGridPagerAdapter(Context ctx, FragmentManager fm , ArrayList<Matter> matters) {
        super(fm);
        this.matters.clear();
        this.matters.addAll(matters);
        for(Matter matter : this.matters){
        	CustomFragment fragment = new CustomFragment(matter);
        	fragments.add(fragment);
        }
        
        mRows = new ArrayList<SampleGridPagerAdapter.Row>();
        mRows.add(new Row(fragments));
    }

    private class Row {
        final List<Fragment> columns = new ArrayList<Fragment>();
        
        public Row(ArrayList<Fragment> fragments) {
        	columns.addAll(fragments);
        }

        Fragment getColumn(int i) {
            return columns.get(i);
        }

        public int getColumnCount() {
            return columns.size();
        }
    }

    @Override
    public Fragment getFragment(int row, int col) {
        Row adapterRow = mRows.get(row);
        return adapterRow.getColumn(col);
    }

    @Override
    public int getRowCount() {
        return mRows.size();
    }

    @Override
    public int getColumnCount(int rowNum) {
        return mRows.get(rowNum).getColumnCount();
    }

    class DrawableLoadingTask extends AsyncTask<Integer, Void, Drawable> {
        private Context context;

        DrawableLoadingTask(Context context) {
            this.context = context;
        }

        @Override
        protected Drawable doInBackground(Integer... params) {
            return context.getResources().getDrawable(params[0]);
        }
    }
}
