package com.oneone.support.qiniu;


import org.json.JSONObject;

import com.oneone.modules.entry.beans.UploadTokenBean;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import android.os.Handler;

public class UploadImgUtil {
	public static UploadManager QI_NIU_UPLOAD_MANAGER;
	public static void uploadImg (final UploadObj uploadObj, final Handler handler) {
		System.out.println(uploadObj);
		String data = uploadObj.getUploadUri().getPath();
		String key = uploadObj.getUploadTokenBean().getPath();
		String token = uploadObj.getUploadTokenBean().getToken();

		QI_NIU_UPLOAD_MANAGER.put(data, key, token,
		    new UpCompletionHandler() {
		        @Override
		        public void complete(String key, ResponseInfo info, JSONObject res) {
		            //res包含hash、key等信息，具体字段取决于上传策略的设置
		             if(info.isOK()) {
						 uploadObj.setStatus(UploadObj.UPLOAD_SUCCESS);
		            	 handler.sendMessage(handler.obtainMessage(0, uploadObj));
		             } else {
						 uploadObj.setStatus(UploadObj.UPLOAD_FAILED);
		            	 handler.sendMessage(handler.obtainMessage(1, uploadObj));
		                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
		             }
		            }
		        }, null);
	}
	
	public static void uploadImgPhoto (final UploadFile uploadFile, UploadTokenBean uploadToken, final Handler handler) {
		String data = uploadFile.getFilePath();
		String key = uploadToken.getPath();
		String token = uploadToken.getToken();
		QI_NIU_UPLOAD_MANAGER.put(data, key, token,
		    new UpCompletionHandler() {
		        @Override
		        public void complete(String key, ResponseInfo info, JSONObject res) {
		            //res包含hash、key等信息，具体字段取决于上传策略的设置
		             if(info.isOK()) {
		            	 handler.sendMessage(handler.obtainMessage(5, uploadFile));
		             } else {
		            	 handler.sendMessage(handler.obtainMessage(6, uploadFile));
		                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
		             }
		            }
		        }, null);
	}
}
