package com.oneone.api.request;

/**
 * @author qingfei.chen
 * @since 2018/7/3.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
public class RedDotTypesParams {

    private String [] types;

    public RedDotTypesParams (String type ){
        types = new String[]{type};
    }

    public RedDotTypesParams (String [] types){
        this.types = types;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

}
