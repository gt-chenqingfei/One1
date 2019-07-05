package com.oneone.support.qiniu;

import android.graphics.Bitmap;

public class UploadFile {
	private String url;
	private int curPosition;
	private String filePath;
	private Bitmap bitmap;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCurPosition() {
		return curPosition;
	}

	public void setCurPosition(int curPosition) {
		this.curPosition = curPosition;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		UploadFile otherFile = (UploadFile) o;
		return filePath.equals(otherFile.getFilePath());
	}
}
