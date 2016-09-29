package al.com.dreamdays.base;

public class LINKS {

	public static final String VERSION = "http://guxiusupport.com/api/version.php";
	
	public static String GET_VERSION(int type){
		StringBuffer buffer = new StringBuffer();
		buffer.append(VERSION);
		buffer.append("?");
		buffer.append("type");
		buffer.append("=");
		buffer.append(type);
		System.err.println(buffer.toString());
		return buffer.toString();
	}
	
	public static final String REGISTER		 = "http://guxiusupport.com/api/regist.php";
	public static final String LOGIN 		 = "http://guxiusupport.com/api/login.php";
	public static final String FORGET 		 = "http://guxiusupport.com/api/forget.php";
	public static final String SAVE 		 = "http://guxiusupport.com/api/save.php";
	public static final String GETTIMELIST 	 = "http://guxiusupport.com/api/getTimeList.php";
	public static final String GETMATTER 	 = "http://guxiusupport.com/api/getMatter.php";

}
