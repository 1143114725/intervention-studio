package com.investigate.newsupper.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import com.investigate.newsupper.R;
import com.investigate.newsupper.global.textsize.TextSizeManager;

public class DialogUtil {
	
	static int dialogBtnSize = (int) (UIUtils
			.getDimenPixelSize(R.dimen.button_text_size) * TextSizeManager
			.getInstance().getRealScale());
	/**
	 * 
	 * @param context 上下文
	 * @param text	需要显示的文本
	 */
	public static void newdialog(Context context,String text){
		AlertDialog.Builder Recorddialog = new AlertDialog.Builder(
				context, AlertDialog.THEME_HOLO_LIGHT);
		Recorddialog
//				.setTitle("提示")
//				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage(text)
				.setPositiveButton(
						context.getResources().getString(
								R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

								return;
							}

						});

		AlertDialog Recordcreate = Recorddialog.create();
		Recordcreate.show();
		TextView msgTv=((TextView) Recordcreate.findViewById(android.R.id.message));
		msgTv.setMinLines(2);
		msgTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
		msgTv.setGravity(Gravity.CENTER_VERTICAL);
		Recordcreate.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(
				TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
		Button okBtn = Recordcreate.getButton(AlertDialog.BUTTON_POSITIVE);
		okBtn.setBackgroundColor(0xFF6751B6);
		okBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogBtnSize);
		okBtn.setTextColor(Color.WHITE);
	}

}
