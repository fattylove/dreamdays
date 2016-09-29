package al.com.dreamdays.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author Fatty
 *
 */
public class SignInUpPagerAdapter extends FragmentPagerAdapter {

	ArrayList<Fragment> list;

	/**
	 * 
	 * @param fm
	 * @param list
	 */
	public SignInUpPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

}
