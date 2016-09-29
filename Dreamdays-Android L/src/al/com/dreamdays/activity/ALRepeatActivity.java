package al.com.dreamdays.activity;

import java.util.ArrayList;

import al.com.dreamdays.adapter.RepeatAdapter;
import al.com.dreamdays.base.BaseActivity;
import al.com.dreamdays.sqlite.Repeat;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class ALRepeatActivity extends BaseActivity {

	private ListView repeatListView;
	private int repeatPosition;
	private LinearLayout bgLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.al_dreamdays_repeat_layout);
		
		bgLayout = (LinearLayout)this.findViewById(R.id.bgLayout);
		bgLayout.setBackgroundColor(Color.parseColor("#90000000"));
		bgLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		} );
		
		repeatPosition = getIntent().getIntExtra("repeatPosition", 0);
		
		repeatListView = (ListView) findViewById(R.id.category_list_view);
		RepeatAdapter repeatAdapter = new RepeatAdapter(this);
		repeatAdapter.addRepeats(initRepeatData(this));
		repeatAdapter.setPosition(repeatPosition);
		repeatListView.setAdapter(repeatAdapter);
		
		repeatListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Repeat repeat = (Repeat) repeatListView.getItemAtPosition(position);
				
				Intent intent = new Intent();
				intent.putExtra("repeat", repeat);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	public static ArrayList<Repeat> initRepeatData(Context context){
		Resources resources = context.getResources();
		ArrayList<Repeat> repeats = new ArrayList<Repeat>();
		Repeat r1 = new Repeat();
		r1.setId(0);
		r1.setName(resources.getString(R.string.none));
		r1.setsDate("7");
		repeats.add(r1);
		
		Repeat r2 = new Repeat();
		r2.setId(1);
		r2.setName(resources.getString(R.string.weekly));
		repeats.add(r2);
		
		Repeat r3 = new Repeat();
		r3.setId(2);
		r3.setName(resources.getString(R.string.bi_weekly));
		repeats.add(r3);
		
		Repeat r4 = new Repeat();
		r4.setId(3);
		r4.setName(resources.getString(R.string.monthly));
		repeats.add(r4);
		
		Repeat r5 = new Repeat();
		r5.setId(4);
		r5.setName(resources.getString(R.string.quarterly));
		repeats.add(r5);
		
		Repeat r6 = new Repeat();
		r6.setId(5);
		r6.setName(resources.getString(R.string.semi_annually));
		repeats.add(r6);
		
		Repeat r7 = new Repeat();
		r7.setId(6);
		r7.setName(resources.getString(R.string.annually));
		repeats.add(r7);
		return repeats;
	}

	@Override
	public void finish() {
		bgLayout.setBackgroundColor(Color.TRANSPARENT);
		super.finish();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
