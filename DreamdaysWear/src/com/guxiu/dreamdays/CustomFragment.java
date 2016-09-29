package com.guxiu.dreamdays;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomFragment extends Fragment implements View.OnClickListener{

	
	private TextView topNameTextView ,topDaysTextView;
	private ImageView topPointImageView ,bottomPointImageView ,centerImageView;
	private TextView centerLine1 ,centerLine2 ;
	
	private RelativeLayout dayNumsLayout;
	private LinearLayout   dayYMDsLayout;
	
	private TextView c_clickYearTextView,c_yyTagTextView,c_clickMonthTextView ;
	private TextView c_mmTagTextView ,c_clickDayTextView ,c_ddTagTextView;
	
	private TextView contentDataTextView;
	
	private LinearLayout bottomViewLayout;
	private ImageView bgImageView;
	private Matter detailMatter ; 
	
	public CustomFragment(Matter matter){
		this.detailMatter  = matter;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View rootView = inflater.inflate(R.layout.al_dreamdays_detail_center_layout, container, false);
    	
    	bgImageView = (ImageView)rootView.findViewById(R.id.bgImageView);
    	topDaysTextView = (TextView)rootView.findViewById(R.id.topDaysTextView);
 	    topPointImageView = (ImageView)rootView.findViewById(R.id.topPointImageView);
 	    bottomPointImageView = (ImageView)rootView.findViewById(R.id.bottomPointImageView);
 	    centerImageView = (ImageView)rootView.findViewById(R.id.centerImageView);
 	    topNameTextView = (TextView)rootView.findViewById(R.id.topNameTextView);
 	    centerLine1 = (TextView)rootView.findViewById(R.id.centerLine1);
 	    centerLine2 = (TextView)rootView.findViewById(R.id.centerLine2);
 	    
 	    bottomViewLayout = (LinearLayout)rootView.findViewById(R.id.bottomViewLayout);
 	    dayNumsLayout= (RelativeLayout)rootView.findViewById(R.id.dayNumsLayout);
 	    dayYMDsLayout = (LinearLayout)rootView.findViewById(R.id.dayYMDsLayout);
 	    dayNumsLayout.setOnClickListener(this);
 	    dayYMDsLayout.setOnClickListener(this);
 	    bottomViewLayout.setOnClickListener(this);
 	    
 	    c_clickYearTextView = (TextView)rootView.findViewById(R.id.c_clickYearTextView);
 	    c_clickMonthTextView = (TextView)rootView.findViewById(R.id.c_clickMonthTextView);
 	    c_clickDayTextView = (TextView)rootView.findViewById(R.id.c_clickDayTextView);
 	    
 	    c_yyTagTextView = (TextView)rootView.findViewById(R.id.c_yyTagTextView);
 	    c_mmTagTextView = (TextView)rootView.findViewById(R.id.c_mmTagTextView);
 	    c_ddTagTextView = (TextView)rootView.findViewById(R.id.c_ddTagTextView);
 	    
 	   contentDataTextView  = (TextView)rootView.findViewById(R.id.contentDataTextView);
 	   contentDataTextView.setVisibility(View.GONE);
    	
        return rootView;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	super.onViewCreated(view, savedInstanceState);
    	
    	refreshData(detailMatter);
    }

	//计算year  month day
	private long matterDateLong =0L;
	private int years;
	private int months;
	private int days;
	
	//文本
	private String yearsText;
	private String monthsText;
	private String daysText;
    
	public void refreshData(final Matter pagerMatter){
		if(pagerMatter!=null){
			
			//set event name
			topNameTextView.setText(pagerMatter.getMatterName());
			//set icon
			centerImageView.setImageResource(SampleGridPagerAdapter.ICON_IMAGES[pagerMatter.getClassifyType()]);
			bgImageView.setImageResource(SampleGridPagerAdapter.BG_IMAGES[pagerMatter.getClassifyType()]);
			
			//set event date
			try {
				matterDateLong = DateUtil.getMatterDay(Constant.appDateFormat.parse(pagerMatter.getMatterTime()));
				if (matterDateLong > 0) {
					bottomPointImageView.setVisibility(View.VISIBLE);
					topPointImageView.setVisibility(View.GONE);
				} else {
					matterDateLong = Math.abs(matterDateLong);
					bottomPointImageView.setVisibility(View.GONE);
					topPointImageView.setVisibility(View.VISIBLE);
				}
				topDaysTextView.setText(matterDateLong + "");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			int[] datas = new int[3] ;
			//Matter Date
			String startDateStr = pagerMatter.getMatterTime();
			Date startDate = null;
			String matterDate = null;
			try {
				startDate = Constant.appDateFormat.parse(startDateStr);
				matterDate = Constant.globalDateFormat.format(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//nowDate
			Date nowDate = new Date();
			String nowDateStr = Constant.equalDateFormat_2.format(nowDate);
			String nowMatterDate = nowDateStr+" 00:00:00";
			
			try {
				startDate = Constant.globalDateFormat.parse(matterDate);
				nowDate = Constant.globalDateFormat.parse(nowMatterDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			Calendar nowCalendar = Calendar.getInstance();
			nowCalendar.setTime(nowDate);
			
			if(startDate.before(nowDate)){
				datas = DateUtil.getNeturalAge(startCalendar ,nowCalendar);
			}else if(startDate.after(nowDate)){
				datas = DateUtil.getNeturalAge(nowCalendar , startCalendar);
			}
			years = datas[0];
			months = datas[1];
			days = datas[2];
			
			//没年
			if(years == 0){
				c_clickYearTextView.setVisibility(View.GONE);
				c_yyTagTextView.setVisibility(View.GONE);
			}
			
			//有年，没月日
			if(years != 0 && months == 0 && days == 0){
				yearsText = years +"";
				monthsText = "00";
				daysText = "00";
			}
			
			//有年月，没日
			if(years != 0 && months != 0 && days == 0){
				yearsText = years +"";
				if(months<10){
					monthsText = "0" + months;
				}else{
					monthsText = "" + months;
				}
				daysText = "00";
			}
			
			//有年日，没月
			if(years != 0 && months == 0 && days != 0){
				yearsText = years +"";
				monthsText = "00";
				if(days<10){
					daysText = "0" + days;
				}else{
					daysText = days+"";
				}
			}
			
			//有年月日
			if(years != 0 && months != 0 && days != 0){
				yearsText = years +"";
				if(months<10){
					monthsText = "0" + months;
				}else{
					monthsText = "" + months;
				}
				if(days<10){
					daysText = "0" + days;
				}else{
					daysText = days+"";
				}
			}
			
			//没年，有月日
			if(years == 0  && months != 0 && days != 0){
				yearsText = "";
				monthsText = months +"";
				if(days<10){
					daysText = "0" + days;
				}else{
					daysText = days+"";
				}
			}
			
			//没年日，有月
			if(years == 0  && months != 0 && days == 0){
				yearsText = "";
				monthsText = months +"";
				daysText = "00" ;
			}
			
			//没年月，有日
			if(years == 0  && months == 0 && days != 0){
				yearsText = "";
				monthsText = "";
				daysText = days+"";
			}
			
			c_clickYearTextView.setText(yearsText);
			c_clickMonthTextView.setText(monthsText);
			c_clickDayTextView.setText(daysText);
			
			
			//处理详情日期切换问题
			if(matterDateLong < 31){  // 31
				dayYMDsLayout.setVisibility(View.GONE);
				dayNumsLayout.setVisibility(View.VISIBLE);
			}
		}
	}
    
    
    
    boolean isClick  = false;
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bottomViewLayout:
			
			break;
		case R.id.dayYMDsLayout:
			dayNumsLayout.setClickable(false);
			dayYMDsLayout.setClickable(false);
			dayNumsLayout.setOnClickListener(null);
			dayYMDsLayout.setOnClickListener(null);
			
			dayYMDsLayout.startAnimation(out());
			new Handler().postDelayed(new Runnable() {
				public void run() {
					dayYMDsLayout.setVisibility(View.GONE);
					dayYMDsLayout.setClickable(false);
					dayYMDsLayout.setOnClickListener(null);
				}
			}, 200);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					dayNumsLayout.setVisibility(View.VISIBLE);
					dayNumsLayout.startAnimation(in());
					dayNumsLayout.setClickable(true);
					dayNumsLayout.setOnClickListener(CustomFragment.this);
				}
			}, 200);
			break;
		case  R.id.dayNumsLayout:
			if(matterDateLong > 31){  
				dayNumsLayout.setClickable(false);
				dayYMDsLayout.setClickable(false);
				dayNumsLayout.setOnClickListener(null);
				dayYMDsLayout.setOnClickListener(null);
				
				dayNumsLayout.startAnimation(out());
				new Handler().postDelayed(new Runnable() {
					public void run() {
						dayNumsLayout.setVisibility(View.GONE);
						dayNumsLayout.setClickable(false);
						dayNumsLayout.setOnClickListener(null);
					}
				}, 200);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						dayYMDsLayout.setVisibility(View.VISIBLE);
						dayYMDsLayout.startAnimation(in());
						dayYMDsLayout.setClickable(true);
						dayYMDsLayout.setOnClickListener(CustomFragment.this);
					}
				}, 200);
			}
			break;
		default:
			break;
		}
	}

	public Animation in(){
		Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);  
		alphaAnimation.setDuration(200);
		return alphaAnimation;
	}
	
	public Animation out(){
		Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);  
		alphaAnimation.setDuration(200);
		return alphaAnimation;
	}

    
}
