package al.com.dreamdays.utils.event;

import java.text.ParseException;
import java.util.Comparator;

import al.com.dreamdays.base.Constant;
import al.com.dreamdays.sqlite.Matter;
import al.com.dreamdays.utils.DateUtil;

/**
 * 
 * @author Fatty
 *
 */
public class SinceEventCompare implements Comparator<Matter> {
	
	@Override
	public int compare(Matter matter0, Matter matter1) {
		try {
			long matterDay1 = DateUtil.getMatterDay(Constant.appDateFormat.parse(matter0.getMatterTime()));
			long matterDay2 = DateUtil.getMatterDay(Constant.appDateFormat.parse(matter1.getMatterTime()));
			return (int) (matterDay2 - matterDay1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
