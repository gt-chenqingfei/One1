package com.oneone.framework.ui.glide;

import com.bumptech.glide.load.model.GlideUrl;

/**
 * @author qingfei.chen
 * @since 2018/2/28.
 */

public class KeyGlideUrl extends GlideUrl {

    private String mUrl;

    public KeyGlideUrl(String url) {
        super(url);
        mUrl = url;
    }

    @Override
    public String getCacheKey() {
        return mUrl.replace(findTokenParam(), "");
    }

    /**
     * http://p0f9cuo54.bkt.clouddn.com/portraits/u/-5731703714851197996/0.png!style1
     * http://p0f9cuo54.bkt.clouddn.com/portraits/u/-5731703714851197996/0.png!style1?v=11
     *
     * @return
     */
    private String findTokenParam() {
        String tokenParam = "";
        int indexOf = mUrl.lastIndexOf("?");
        if (indexOf != -1) {
            tokenParam = mUrl.substring(indexOf, mUrl.length());
        }
        return tokenParam;
    }

}
