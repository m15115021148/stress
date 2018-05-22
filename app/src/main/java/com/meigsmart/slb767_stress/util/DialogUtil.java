package com.meigsmart.slb767_stress.util;


import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.meigsmart.slb767_stress.R;


/**
 *
 * @author chenmeng
 * @Description 对话框工具类， 统一全局对话框
 * @date create at 2017年9月12日 上午10:04:12
 */
public class DialogUtil {

	/**
	 * 自定义系统提示框
	 * 
	 * @param context
	 *            本类
	 * @param isCancle
	 *            是否 有取消or其他 按钮
	 * @param title
	 *            提示框标题
	 * @param message
	 *            提示框内容
	 * @param btOkName
	 *            按钮名称
	 * @param btCancleName
	 *            按钮名称
	 * @param listen
	 *            事件
	 * @return
	 */
	public static void customSystemDialog(Context context, boolean isCancle,
			String title, String message, String btOkName, String btCancleName,
			DialogInterface.OnClickListener listen) {
		Builder normalDia = new Builder(context);
		normalDia.setTitle(title);
		normalDia.setMessage(message);
		normalDia.setPositiveButton(btOkName, listen);
		if (isCancle) {
			normalDia.setNegativeButton(btCancleName,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		}
		normalDia.create().show();
	}

	/**
	 * 自定义输入对话框
	 *
	 * @param context
	 *            本类
	 * @param btOkName
	 *            确定名称
	 * @param btCancelName
	 *            取消名称
	 * @param pListener
	 *            确定事件
	 * @param nListener
	 *            取消事件
	 * @return 返回输入对话框的view
	 */
	public static View customInputDialog(Context context, String title,String btOkName,
										 String btCancelName, DialogInterface.OnClickListener pListener,
										 DialogInterface.OnClickListener nListener) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(context).inflate(
				R.layout.custom_input_dialog, null);
		view.findViewById(R.id.dialog_et_txt).setVisibility(View.VISIBLE);
        builder.setView(view);
		if(!TextUtils.isEmpty(title)){
			TextView textView=(TextView)view.findViewById(R.id.dialog_title);
			textView.setText(title);
		}
		builder.setPositiveButton(btOkName, pListener);
		builder.setNegativeButton(btCancelName, nListener);
        builder.create().show();
		return view;
	}

	/**
	 * 自定义输入对话框
	 * 
	 * @param context
	 *            本类
	 * @param btOkName
	 *            确定名称
	 * @param btCancleName
	 *            取消名称
	 * @param pListener
	 *            确定事件
	 * @param nListener
	 *            取消事件
	 * @return 返回输入对话框的view
	 */
	public static View customInputDialog(Context context, String btOkName,
			String btCancleName, DialogInterface.OnClickListener pListener,
			DialogInterface.OnClickListener nListener) {
		Builder buidler = new Builder(context);
		View view = LayoutInflater.from(context).inflate(
				R.layout.custom_input_dialog, null);
		view.findViewById(R.id.dialog_et_txt).setVisibility(View.VISIBLE);
		buidler.setView(view);
		buidler.setPositiveButton(btOkName, pListener);
		buidler.setNegativeButton(btCancleName, nListener);
		buidler.create().show();
		return view;
	}

	/**
	 * 自定义提示框
	 * 
	 * @param context
	 *            本类
	 * @param btOkName
	 *            确定名称
	 * @param btCancleName
	 *            取消名称
	 * @param pListener
	 *            确定 事件
	 * @param nListener
	 *            取消事件
	 * @return 返回提示框的view
	 */
	public static View customPromptDialog(Context context,String title, String btOkName,
			String btCancleName, DialogInterface.OnClickListener pListener,
			DialogInterface.OnClickListener nListener) {
		AlertDialog.Builder buidler = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(context).inflate(
				R.layout.custom_input_dialog, null);
		view.findViewById(R.id.dialog_tv_txt).setVisibility(View.VISIBLE);
		if(!TextUtils.isEmpty(title)){
			TextView textView=(TextView)view.findViewById(R.id.dialog_title);
			textView.setText(title);
		}
		buidler.setView(view);
		buidler.setPositiveButton(btOkName, pListener);
		buidler.setNegativeButton(btCancleName, nListener);
		buidler.create().show();
		return view;
	}

}
