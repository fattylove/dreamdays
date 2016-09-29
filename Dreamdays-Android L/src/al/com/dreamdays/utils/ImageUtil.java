package al.com.dreamdays.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import al.com.dreamdays.activity.photo.AlbumEntity;
import al.com.dreamdays.base.Constant;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 
 * @author Fatty
 *
 */
@SuppressWarnings("deprecation")
public class ImageUtil {
	
	public static final int REQ_CODE_CAMERA = 0; // 表示图片采集
	public static final int REQ_CODE_PICTURE = 1; // 表示图片选取
	public static final int PHOTORESOULT = 2; // 表示图片截取
	private static final String IMAGE_UNSPECIFIED = "image/*";
	
	public static final int PHOTO_ZOOM = 15;
	
	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 */
	public static void photoZoom(Context context, Uri uri ,int outputX , int outputY) {
		Activity act = (Activity) context;
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 32);
		intent.putExtra("aspectY", 21);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", false);
		intent.putExtra("return-data", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		act.startActivityForResult(intent, PHOTO_ZOOM);
	}
	
	/**
	 * Resource 图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap loadResourceDrawable(Context context , int resId) {
		Bitmap bitmap = null;
        InputStream is = context.getResources().openRawResource(resId);  
        //A
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565; 
		options.inPurgeable = true;  
		options.inInputShareable = true;  
		//B
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeStream(is, null, options);
		final int minSideLength = Math.min(Constant.width, Constant.height);
		options.inSampleSize = computeSampleSize(options, minSideLength , Constant.width * Constant.height);
		options.inJustDecodeBounds = false;

		try {
			//C
			bitmap= BitmapFactory.decodeStream(is, null, options);
		} catch (OutOfMemoryError e) {
			bitmap = null ;
		}
		return bitmap;
	}
	
	/**
	 * Assets图片
	 * 
	 * @param context
	 * @param assetsName
	 * @return
	 */
	public static Bitmap loadAssetsDrawable(Context context , String assetsName) {
		Bitmap bitmap = null;
		AssetManager am =context.getResources().getAssets();
		InputStream is;
		try {
			is = am.open(assetsName);
		} catch (IOException ioe) {
			return null;
		}
		//A
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565; 
		options.inPurgeable = true;  
		options.inInputShareable = true;  
		//B
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeStream(is, null, options);
		final int minSideLength = Math.min(Constant.width, Constant.height);
		options.inSampleSize = computeSampleSize(options, minSideLength , Constant.width * Constant.height);
		options.inJustDecodeBounds = false;
		try {
			//C
			bitmap = BitmapFactory.decodeStream(is, null, options);
		} catch (OutOfMemoryError e) {
			bitmap =  null ;
		}
		return bitmap;
	}

	/**
	 * 通过File拿bitmap
	 * 
	 * @param file
	 * @return
	 */
	public static Bitmap loadSdcardDrawable(File file ,int width , int height) {
		Bitmap bitmap = null;
		if (null != file && file.exists()) {
			//A
			BitmapFactory.Options options =  new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565; 
			options.inPurgeable = true;  
			options.inInputShareable = true;
			//B
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(file.getPath(),  options);
			final int minSideLength = Math.min(width, height);
			options.inSampleSize = computeSampleSize(options, minSideLength, width * height);
			options.inJustDecodeBounds = false;
			
			try {
				//C
				bitmap = BitmapFactory.decodeFile(file.getPath(), options);
			} catch (OutOfMemoryError e) {
				bitmap = null;
			}
		}
		return bitmap;
	}

	/**
	 * 压缩图片并存储制定图片
	 * 
	 * @param context
	 * 
	 * @param imgPath
	 *            图片的完整路径
	 * 
	 * @param dir
	 *            储存路径
	 * 
	 * @param filename
	 *            存储文件名
	 */
	public static boolean saveAndCompressImageInCache(String imgPath, String dir, String filename) throws Exception {
		Bitmap bitmap = null;
		//A
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565; 
		options.inPurgeable = true;  
		options.inInputShareable = true;
		//B  
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(imgPath)), null, options);
		final int minSideLength = Math.min(Constant.width, Constant.height);
		options.inSampleSize = computeSampleSize(options, minSideLength , Constant.width * Constant.height);
		options.inJustDecodeBounds = false;
	
		try {
			//C
			bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(imgPath)), null, options);
			
			//D  按照屏幕的尺寸拿缩略图
			bitmap = ThumbnailUtils.extractThumbnail(bitmap ,Constant.width, Constant.height);
			
			//E  保存图片到Sdcard
			ImageUtil.saveBitmap(dir ,filename, bitmap);
			
			//F
			if(bitmap!=null && !bitmap.isRecycled()){
				bitmap.recycle();
				bitmap = null;
			}
		} catch (OutOfMemoryError e) {
			return false ;
		}
		return true;
	}
	

	/**
	 * 保存图片到sd卡
	 * 
	 * @param path
	 *            文件的路径
	 * @param filename
	 *            文件的名字
	 * @param mBitmap
	 *            需保存的bitmap对象
	 */
	public static void saveBitmap(String path ,String filename, Bitmap mBitmap) {
		File f = new File(path);
		FileOutputStream fos = null;
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File(path + filename + ".jpg");
		try {
			f.createNewFile();
			fos = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * 将bitmap存到本地SDcard
	 * 
	 * @param fileUrl  文件的路径
	 * 
	 * @param mBitmap  存储的bitmap对象
	 */
	public static void saveBitmap(String filepath, Bitmap mBitmap) {
		FileOutputStream fos = null;
		File f = new File(filepath);
		if(f.exists()){
			f.delete();
		}
		try {
			f.createNewFile();
			fos = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 压缩图片算法
	 * 
	 * @param options
	 *            BitmapFactory.Options
	 * @param minSideLength
	 *            宽高的最小值
	 * @param maxNumOfPixels
	 *            像素面积 w * h
	 * @return
	 */
	private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	/**
	 * 压缩图片算法
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	


	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	/**
	 * 将Assest目录下的文件copy到sdcard里
	 * 
	 * @param context
	 * @param localName
	 * @param assestName
	 * @return
	 */
	public static boolean copyImageToSdrcardFromAssest(Context context ,String localName , String assestName) {
		FileOutputStream fOut = null;
		InputStream in = null;
		File f = new File(localName);
		if (!f.exists()) {
			try {
				f.createNewFile();
				fOut = new FileOutputStream(f);
				AssetManager assetManager = context.getAssets();
				in = assetManager.open(assestName);
				byte[] b = new byte[1024];
				while (in.read(b) != -1) {
					fOut.write(b);
				}
				fOut.flush();
				return true;
			} catch (Exception e) {
				return false;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (fOut != null) {
						fOut.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**********************************************************************************************************/
	/**
	 * Drawable转换成Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap 转换成 Drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		return new BitmapDrawable(null, bitmap);
	}

	/**
	 * Bitmap转换成字节数组byte[]
	 * 
	 * @param bmp
	 * @return
	 */
	public static byte[] bitmap2Byte(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 字节数组byte[]转换成Bitmap
	 * 
	 * @param buffer
	 * @return
	 */
	public static Bitmap byte2Bitmap(byte[] buffer) {
		return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
	}
	
	
	/**
	 * dip转换成px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转换成dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	
	
	/**
	 * 获取本地图片
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<String> getAllMediaImages(Activity context) {
		ArrayList<String> localPaths = new ArrayList<String>();
		Cursor cursor = context.managedQuery(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null, null, null);
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			String path = cursor.getString(cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA));
			localPaths.add(path);
		}
		return localPaths;
	}
	
	/**
	 * 获取相册列表
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<AlbumEntity> getAlbums(Context context) {
		ArrayList<AlbumEntity> albums = new ArrayList<AlbumEntity>();
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		int fileNum = cursor.getCount();

		for (int counter = 0; counter < fileNum; counter++) {
			String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
			String file_dir = getDir(path);
			boolean in_albums = false;
			for (AlbumEntity temp_album : albums) {
				if (temp_album.getmName().equals(file_dir)) {
					in_albums = true;
					break;
				}
			}
			if (!in_albums) {
				AlbumEntity album = new AlbumEntity();
				album.setmName( getDir(path));
//				album.mNum = "(" + getPicNum(context ,album.mName) + ")";
				album.setmCoverUrl(path);
				albums.add(album);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return albums;
	}

	/**
	 * 获取图片数量
	 * 
	 * @param context
	 * @param album_file_dir
	 * @return
	 */
	public static int getPicNum(Context context , String album_file_dir) {
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		int fileNum = cursor.getCount();

		int photo_num = 0;
		for (int counter = 0; counter < fileNum; counter++) {
			String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
			String file_dir = getDir(path);
			if (album_file_dir.equals(file_dir))
				photo_num++;
			cursor.moveToNext();
		}
		cursor.close();
		return photo_num;
	}
	
	/**
	 * 获取相册下的图片列表
	 * 
	 * @param context
	 * @param album_dir
	 * @return
	 */
	public static ArrayList<String> getPhotos(Context context, String album_dir) {
		ArrayList<String> photos = new ArrayList<String>();
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] { MediaStore.Images.Media.DATA };
		Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		int fileNum = cursor.getCount();

		for (int counter = 0; counter < fileNum; counter++) {
			String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
			String file_dir = getDir(path);
			if (file_dir.equals(album_dir))
				photos.add(path);
			cursor.moveToNext();
		}
		cursor.close();

		return photos;
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String getDir(String path) {
		String subString = path.substring(0, path.lastIndexOf('/'));
		return subString.substring(subString.lastIndexOf('/') + 1, subString.length());
	}
	
	/**
	 * 
	 * @author CHY
	 * 
	 * 计算图片的RGB平均值
	 * 
	 * @param mBitmap
	 * 
	 * @return  array{R , G , B}  
	 * 
	 */
	public static float[] getAverageColor2(Bitmap bitmap){
		Bitmap mBitmap =  small(bitmap);
		int bWidth = mBitmap.getWidth();
		int bHeight = mBitmap.getHeight();
		long R = 0;
		long G = 0;
		long B = 0;
		long rR = 0;
		long rG = 0;
		long rB = 0;
		int count = 0;
		
		int color ;
		int r;
		int g;
		int b;
		for (int i = 0; i < bHeight; i++) {
			for (int j = 0 ; j < bWidth; j++) {
				color = mBitmap.getPixel(j, i);
				r = Color.red(color);
				g = Color.green(color);
				b = Color.blue(color);
				
				R += r;
				G += g;
				B += b;
				count ++;
			}
		}
		rR =  R / count;
		rG =  G / count;
		rB =  B / count;
		
		double[] hsvColor = RGBtoHSV((double)rR , (double)rG,  (double)rB);
		
		double s_limit = 0.05;
		
		double h = hsvColor[0];
		double s = hsvColor[1];
		double v = hsvColor[2];
		
		float[] targetColor = new float[3];
		if (s < s_limit) {
			double min = 1;
			for (int k = 0; k < Constant.hsvGray.length; k ++) {
				double tmp_v = Math.abs(Constant.hsvGray[k][2] / 100 - v);
				
				if (tmp_v < min) {
					min = tmp_v;
					targetColor[0] = Constant.hsvGray[k][0];
					targetColor[1] = Constant.hsvGray[k][1] / 100;
					targetColor[2] = Constant.hsvGray[k][2] / 100;
				}
			}
		} else {
			double min = 360;
			for (int k = 0; k < Constant.hsvColors.length; k++) {
				double tmp_h = Math.abs(Constant.hsvColors[k][0] - h);
				
				if (tmp_h < min){
					min = tmp_h;
					targetColor[0] = Constant.hsvColors[k][0];
					targetColor[1] = Constant.hsvColors[k][1] / 100;
					targetColor[2] = Constant.hsvColors[k][2] / 100;
				}
			}
		}
		return targetColor;
	}
	
	public static void printColor(Bitmap mBitmap){
		int bWidth = mBitmap.getWidth();
		int bHeight = mBitmap.getHeight();
		long R = 0;
		long G = 0;
		long B = 0;
		long rR = 0;
		long rG = 0;
		long rB = 0;
		int count = 0;
		for (int i = 0; i < bHeight; i++) {
			for (int j = 0 ; j < bWidth; j++) {
				int color = mBitmap.getPixel(j, i);
				int r = Color.red(color);
				int g = Color.green(color);
				int b = Color.blue(color);
				
				
				R += r;
				G += g;
				B += b;
				count ++;
			}
		}
		rR =  R / count;
		rG =  G / count;
		rB =  B / count;
		
		System.err.println("rR："+rR +"rG："+rG + "rB："+rB);
	}

	// return h: 0 ~ 360, s: 0 ~ 1, v: 0 ~ 1
	public static double[] RGBtoHSV(double r, double g, double b){
	    double h, s, v;
	    double min, max, delta;
	    min = Math.min(Math.min(r, g), b);
	    max = Math.max(Math.max(r, g), b);
		// V
		v = max / 255;
		delta = max - min;
		// S
		if (max != 0)
			s = delta / max;
		else {
			s = 0;
			h = -1;
			return new double[] { h, s, v };
		}
		// H
		if (r == max)
			h = (g - b) / delta; // between yellow & magenta
		else if (g == max)
			h = 2 + (b - r) / delta; // between cyan & yellow
		else
			h = 4 + (r - g) / delta; // between magenta & cyan
		
		h *= 60; // degrees

		if (h < 0)
			h += 360;

	    return new double[]{h, s, v};
	}

	/**
	 * 
	 * bitmap等比放大
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap big(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(1.5f, 1.5f); 
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	/**
	 * 
	 * bitmap 等比缩小
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.5f, 0.5f);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

}
