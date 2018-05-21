package com.meigsmart.slb767_stress.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import com.meigsmart.slb767_stress.log.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 *
 * @author chenmeng
 * @Description Preferences文件存储对象工具类
 * @date create at 2017年9月19日 下午4:40:32
 */
public class PreferencesUtil {
	private static final String TAG = "PreferencesUtil";
	private static final String SP_LOGIN_PRIVATE = "sp_login_private";
	private static final String SP_MODEL_PRIVATE = "sp_model_private";


	public static void isFristLogin(Context context, String key, boolean isFrist) {
		SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, isFrist).apply();
	}

	public static boolean getFristLogin(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

	public static boolean setDataModel(Context context, String key,
			Object object) {
		SharedPreferences sp = context.getSharedPreferences(SP_MODEL_PRIVATE,
				Context.MODE_PRIVATE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(object);
			String objectVal = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT));
			Editor editor = sp.edit();
			editor.putString(key, objectVal);
			return editor.commit();
		} catch (IOException e) {
			LogUtil.i(TAG, e.getMessage());
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				LogUtil.e(TAG, e.getMessage());
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getDataModel(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SP_MODEL_PRIVATE,
				Context.MODE_PRIVATE);
		String objectVal = sp.getString(key, null);
		if (objectVal == null) {
			LogUtil.e(TAG, "获取保存的实体类为空");
			return null;
		}
		byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			return (T) ois.readObject();
		} catch (StreamCorruptedException e) {
			LogUtil.e(TAG, "获取保存的实体类为空");
			e.printStackTrace();
		} catch (IOException e) {
			LogUtil.e(TAG, "获取保存的实体类为空");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			LogUtil.e(TAG, "获取保存的实体类为空");
			e.printStackTrace();
		} finally {
			try {
				if (bais != null) {
					bais.close();
				}
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				LogUtil.e(TAG, "获取保存的实体类为空");
				e.printStackTrace();
			}
		}
		return null;
	}

	public static boolean deleteDataModel(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SP_MODEL_PRIVATE,
				Context.MODE_PRIVATE);
		return sp.edit().putString(key,"").commit();
	}

	public static boolean deleteDataFirst(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
				Context.MODE_PRIVATE);
		return sp.edit().putBoolean(key,false).commit();
	}

	public static void setStringData(Context context, String key,String values){
		SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
				Context.MODE_PRIVATE);
		sp.edit().putString(key,values).apply();
	}


	public static String getStringData(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
				Context.MODE_PRIVATE);
		return sp.getString(key,"");
	}

	public static void setIntegerData(Context context,String key,int values){
		SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
				Context.MODE_PRIVATE);
		sp.edit().putInt(key,values).apply();
	}

	public static int getIntegerData(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(SP_LOGIN_PRIVATE,
				Context.MODE_PRIVATE);
		return sp.getInt(key,0);
	}
}
