package com.oneone.support.qiniu;

import com.qiniu.android.common.AutoZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.UploadManager;

import java.io.File;

/**
 * @author qingfei.chen
 * @since 2018/4/10.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class QiNiuConfiguration {

    public static void init() {
        //骑牛
        KeyGenerator keyGen = new KeyGenerator() {

            @Override
            public String gen(String arg0, File arg1) {
                // TODO Auto-generated method stub
                return null;
            }
        };

        Configuration configuration = new Configuration.Builder().chunkSize(512 * 1024) // 分片上传时，每片的大小。
                // 默认256K
                .putThreshhold(1024 * 1024) // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .useHttps(true) // 是否使用https上传域名
                .responseTimeout(60) // 服务器响应超时。默认60秒
                .recorder(null) // recorder分片上传时，已上传片记录器。默认null
                .recorder(null, keyGen) // keyGen
                // 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(AutoZone.autoZone) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadImgUtil.QI_NIU_UPLOAD_MANAGER = new UploadManager(configuration);
    }
}
