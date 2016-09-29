package com.api.alertview;

import al.com.dreamdays.base.BaseApplication;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guxiu.dreamdays.R;

/*
 * Copyright 2014 litao
 * https://github.com/sd6352051/NiftyDialogEffects
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@SuppressLint("NewApi")
public class AlertBuilder extends Dialog implements DialogInterface {

	private final String defTextColor = "#FFFFFFFF";
	private final String defDividerColor = "#11000000";
	private final String defMsgColor = "#FFFFFFFF";
	private final String defDialogColor = "#FFE74C3C";
	private static Context tmpContext;
	private Effectstype type = null;
	private LinearLayout mLinearLayoutView;
	private RelativeLayout mRelativeLayoutView;
	private LinearLayout mLinearLayoutMsgView;
	private LinearLayout mLinearLayoutTopView;
	private FrameLayout mFrameLayoutCustomView;
	private View mDialogView;
	private View mDivider;
	private TextView mTitle;
	private TextView mMessage;
	private ImageView mIcon;
	private Button okButton;
	private Button cancelButton;
	private int mDuration = -1;
	private boolean isCancelable = true;
	private static AlertBuilder instance;

	public AlertBuilder(Context context) {
		super(context);
		init(context);

	}

	public AlertBuilder(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);

	}

	public static AlertBuilder getInstance(Context context) {
		if (instance == null || !tmpContext.equals(context)) {
			synchronized (AlertBuilder.class) {
				if (instance == null || !tmpContext.equals(context)) {
					instance = new AlertBuilder(context, R.style.dialog_untran);
				}
			}
		}
		tmpContext = context;
		return instance;

	}

	private void init(Context context) {
		mDialogView = View.inflate(context, R.layout.dialog_layout, null);
		mLinearLayoutView = (LinearLayout) mDialogView.findViewById(R.id.parentPanel);
		mRelativeLayoutView = (RelativeLayout) mDialogView.findViewById(R.id.main);
		mLinearLayoutTopView = (LinearLayout) mDialogView.findViewById(R.id.topPanel);
		mLinearLayoutMsgView = (LinearLayout) mDialogView.findViewById(R.id.contentPanel);
		mFrameLayoutCustomView = (FrameLayout) mDialogView.findViewById(R.id.customPanel);

		mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
		mMessage = (TextView) mDialogView.findViewById(R.id.message);
		mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
		mDivider = mDialogView.findViewById(R.id.titleDivider);
		okButton = (Button) mDialogView.findViewById(R.id.button1);
		cancelButton = (Button) mDialogView.findViewById(R.id.button2);
		
		mTitle.setTypeface(BaseApplication.typeface_heavy);
		mMessage.setTypeface(BaseApplication.typeface_heavy);
		okButton.setTypeface(BaseApplication.typeface_heavy);
		cancelButton.setTypeface(BaseApplication.typeface_heavy);
		
		setContentView(mDialogView);

		this.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				mLinearLayoutView.setVisibility(View.VISIBLE);
				if (type == null) {
					type = Effectstype.Slidetop;
				}
				start(type);
			}
		});
		
		mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isCancelable)
					dismiss();
			}
		});
	}

	public void toDefault() {
		mTitle.setTextColor(Color.parseColor(defTextColor));
		mDivider.setBackgroundColor(Color.parseColor(defDividerColor));
		mMessage.setTextColor(Color.parseColor(defMsgColor));
		mLinearLayoutView.setBackgroundColor(Color.parseColor(defDialogColor));
	}

	public AlertBuilder withDividerColor(String colorString) {
		mDivider.setBackgroundColor(Color.parseColor(colorString));
		return this;
	}

	public AlertBuilder withDividerColor(int color) {
		mDivider.setBackgroundColor(color);
		return this;
	}

	public AlertBuilder withTitle(CharSequence title) {
		toggleView(mLinearLayoutTopView, title);
		mTitle.setText(title);
		return this;
	}

	public AlertBuilder withTitleColor(String colorString) {
		mTitle.setTextColor(Color.parseColor(colorString));
		return this;
	}

	public AlertBuilder withTitleColor(int color) {
		mTitle.setTextColor(color);
		return this;
	}

	public AlertBuilder withMessage(int textResId) {
		toggleView(mLinearLayoutMsgView, textResId);
		mMessage.setText(textResId);
		return this;
	}

	public AlertBuilder withMessage(CharSequence msg) {
		toggleView(mLinearLayoutMsgView, msg);
		mMessage.setText(msg);
		return this;
	}

	public AlertBuilder withMessageColor(String colorString) {
		mMessage.setTextColor(Color.parseColor(colorString));
		return this;
	}

	public AlertBuilder withMessageColor(int color) {
		mMessage.setTextColor(color);
		return this;
	}

	public AlertBuilder withDialogColor(String colorString) {
		mLinearLayoutView.getBackground().setColorFilter(
				ColorUtils.getColorFilter(Color.parseColor(colorString)));
		return this;
	}

	public AlertBuilder withDialogColor(int color) {
		mLinearLayoutView.getBackground().setColorFilter(
				ColorUtils.getColorFilter(color));
		return this;
	}

	public AlertBuilder withIcon(int drawableResId) {
		mIcon.setImageResource(drawableResId);
		return this;
	}

	public AlertBuilder withIcon(Drawable icon) {
		mIcon.setImageDrawable(icon);
		return this;
	}

	public AlertBuilder withDuration(int duration) {
		this.mDuration = duration;
		return this;
	}

	public AlertBuilder withEffect(Effectstype type) {
		this.type = type;
		return this;
	}

	public AlertBuilder withButtonDrawable(int resid) {
		okButton.setBackgroundResource(resid);
		cancelButton.setBackgroundResource(resid);
		return this;
	}

	public AlertBuilder withOkButtonText(CharSequence text) {
		okButton.setVisibility(View.VISIBLE);
		okButton.setBackgroundColor(Color.TRANSPARENT);
		okButton.setTextColor(Color.parseColor("#ff1744"));
		okButton.setText(text);

		return this;
	}

	public AlertBuilder withCancelButtonText(CharSequence text) {
		cancelButton.setVisibility(View.VISIBLE);
		cancelButton.setBackgroundColor(Color.TRANSPARENT);
		cancelButton.setTextColor(Color.parseColor("#90a4ae"));
		cancelButton.setText(text);
		return this;
	}

	public AlertBuilder setOnOkButtonClick(View.OnClickListener click) {
		okButton.setOnClickListener(click);
		return this;
	}

	public AlertBuilder setOnCacnelButtonClick(View.OnClickListener click) {
		cancelButton.setOnClickListener(click);
		return this;
	}

	public AlertBuilder setCustomView(View view, Context context) {
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mLinearLayoutTopView.setVisibility(View.GONE);
		mLinearLayoutMsgView.setVisibility(View.GONE);
		mFrameLayoutCustomView.addView(view);

		return this;
	}

	public AlertBuilder isCancelableOnTouchOutside(boolean cancelable) {
		this.isCancelable = cancelable;
		this.setCanceledOnTouchOutside(cancelable);
		return this;
	}

	public AlertBuilder isCancelable(boolean cancelable) {
		this.isCancelable = cancelable;
		this.setCancelable(cancelable);
		return this;
	}

	private void toggleView(View view, Object obj) {
		if (obj == null) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void show() {
		super.show();
	}

	private void start(Effectstype type) {
		BaseEffects animator = type.getAnimator();
		if (mDuration != -1) {
			animator.setDuration(Math.abs(mDuration));
		}
		animator.start(mRelativeLayoutView);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		okButton.setVisibility(View.GONE);
		cancelButton.setVisibility(View.GONE);
		
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mLinearLayoutTopView.setVisibility(View.VISIBLE);
		mLinearLayoutMsgView.setVisibility(View.VISIBLE);
	}
}
