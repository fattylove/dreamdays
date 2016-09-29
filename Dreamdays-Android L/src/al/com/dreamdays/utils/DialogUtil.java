package al.com.dreamdays.utils;

import al.com.dreamdays.base.Constant;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.api.alertview.AlertBuilder;
import com.api.alertview.Effectstype;
import com.guxiu.dreamdays.R;

/**
 * 
 * @author Fatty
 *
 */
public class DialogUtil {

	public static final int VERSION = 1;
	public static final int RATEREVIEW = 2;

	/**
	 * google play Dialog
	 * 
	 * @param context
	 */
	public static void showGooglePlayDialog(final Activity context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			AlertDialog.Builder builder = new Builder(context, android.R.style.Theme_Material_Light_Dialog);
			builder.setMessage(context.getString(R.string.google_buy_content_add_event));
			builder.setTitle(context.getString(R.string.upgrade));
			builder.setPositiveButton(context.getString(R.string.upgrade),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							try {
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setData(Uri.parse(Constant.GO_GOOGLE_PLAY_BUY_DREAMDAYS));
								context.startActivity(intent);
							} catch (Exception e) {
								Toast.makeText(context, R.string.no_found_markets,Toast.LENGTH_SHORT).show();
							}
							dialog.dismiss();
						}
					});

			builder.setNegativeButton(context.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		} else {
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			dialogBuilder
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect(Effectstype.SlideBottom) 
			.withTitle(context.getString(R.string.upgrade))
			.withMessage(context.getString(R.string.google_buy_content_add_event))
			.withOkButtonText(context.getString(R.string.upgrade)) 
			.withCancelButtonText(context.getString(R.string.cancel))
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String str = "market://details?id="+ Constant.PACKAGE_NAME;
					try {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(str));
						context.startActivity(intent);
					} catch (Exception e) {
						Toast.makeText(context, R.string.no_found_markets,Toast.LENGTH_SHORT).show();
					}
					context.getSharedPreferences("p_version",Context.MODE_PRIVATE).edit().putBoolean("isShow", true).commit();
					dialogBuilder.cancel();
				}
			}).setOnCacnelButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					context.getSharedPreferences("p_version",Context.MODE_PRIVATE).edit().putBoolean("isShow", true).commit();
					dialogBuilder.cancel();
				}
			}).show();
		}
	}

	/**
	 * 提示Dialog
	 * 
	 * @param context
	 * @param r
	 */
	public static void showCheckDialog(final Activity context, int r) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			AlertDialog.Builder builder = new Builder(context,android.R.style.Theme_Material_Light_Dialog);
			builder.setMessage(context.getString(r));
			builder.setPositiveButton(context.getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}else{
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			dialogBuilder
			.withTitle("Report")
			.withMessage(context.getString(r))
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect( Effectstype.SlideBottom) 
			.withOkButtonText(context.getString(R.string.ok)) 
			.withCancelButtonText("")
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialogBuilder.cancel();
				}
			}).show();
		}
		
	}

	/**
	 * 提示Dialog
	 * 
	 * @param context
	 * @param r
	 */
	public static void showCheckDialog(final Activity context, String r) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			AlertDialog.Builder builder = new Builder(context,android.R.style.Theme_Material_Light_Dialog);
			builder.setMessage(r);
			builder.setPositiveButton(context.getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		} else {
			final AlertBuilder dialogBuilder = AlertBuilder.getInstance(context);
			dialogBuilder
			.isCancelableOnTouchOutside(true) 
			.withDuration(300) 
			.withEffect(Effectstype.SlideBottom) 
			.withTitle("Report")
			.withMessage(r)
			.withOkButtonText(context.getString(R.string.ok)) 
			.withCancelButtonText("")
			.setOnOkButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialogBuilder.cancel();
				}
			}).show();
		}
	}

}
