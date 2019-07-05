package com.oneone.support.share;

/**
 * @author qingfei.chen
 * @since 2017/12/20.
 * Copyright © 2017 HereTech Technology Co.,Ltd. All rights reserved.
 */

public class ShareParams {
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_WEB_PAGE = 4;
    public static final int TYPE_MUSIC = 5;
    public static final int TYPE_VIDEO = 6;
    public static final int TYPE_APPS = 7;
    public static final int TYPE_FILE = 8;
    public static final int TYPE_EMOJI = 9;
    public static final int TYPE_ALIPAY = 10;
    public static final int TYPE_WX_MINI_PROGRAM = 11;

    private String title;
    private String text;
    private String url;
    private String imageUrl;
    private int type;

    public ShareParams() {
    }

    public ShareParams(String title, String text, String url, String imageUrl, int type) {
        this.title = title;
        this.text = text;
        this.url = url;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
