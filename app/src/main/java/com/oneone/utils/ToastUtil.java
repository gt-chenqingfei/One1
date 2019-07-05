package com.oneone.utils;

import com.oneone.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {
	private static Toast currentToast;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void show(Context context, String text) {
		if (currentToast != null)
			currentToast.cancel();

		String ToastStr = "<font color='#ffffff'>"+text+"</font>";
		
		currentToast = Toast.makeText(context, Html.fromHtml(ToastStr), Toast.LENGTH_LONG);
//		currentToast.setText(text);
		currentToast.setGravity(Gravity.CENTER, 0, 0);

		View view = currentToast.getView();
		view.setPadding(36, 30, 36, 30);
		view.setBackground(context.getResources().getDrawable(
				R.drawable.shape_toast_background));

		currentToast.show();

	}

	public static void showTitleToast(Activity activity, String titleStr,
			String textStr) {
		if (currentToast != null)
			currentToast.cancel();

		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_with_title, null);
		layout.setPadding(36, 30, 36, 30);
		TextView title = (TextView) layout.findViewById(R.id.tvTitleToast);
		title.setText(titleStr);
		TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
		text.setText(textStr);
		currentToast = new Toast(activity.getApplicationContext());
//		currentToast.setGravity(Gravity.RIGHT | Gravity.TOP, 12, 40);
		currentToast.setGravity(Gravity.CENTER, 0, 0);
		currentToast.setDuration(Toast.LENGTH_LONG);
		currentToast.setView(layout);
		currentToast.show();
	}

}
