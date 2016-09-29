package al.com.dreamdays.sqlite;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *
 * Repeat -  repeat_type
 *
 */
public class Repeat implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 20150108L;
	
	private int id;
	private String name;
	private long lDate;
	private String sDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getlDate() {
		return lDate;
	}

	public void setlDate(long lDate) {
		this.lDate = lDate;
	}

	public String getsDate() {
		return sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

}
