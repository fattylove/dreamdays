package al.com.dreamdays.utils;

import java.io.File;

/**
 * 
 * @author Fatty
 *
 */
public class FileUtil {

	/**
	 * 校验文件是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean checkFileExists(String filePath) {
		if (filePath != null && filePath.length()>0) {
			File file = new File(filePath);
			if (file.exists())
				return true;
		}
		return false;
	}
	
	/**
	 * 删除文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		if (filePath != null && filePath.length()>0) {
			File file = new File(filePath);
			file.delete();
		}
		return true;
	}
}
