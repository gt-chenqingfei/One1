package com.oneone.modules.find.beans;

import java.util.List;

/**
 * Created by here on 18/4/23.
 */

public class FindCondition {
    private List<Integer> character;
    private String city;
    private String userId;
    private String cityCode;

    public List<Integer> getCharacter() {
        return character;
    }

    public void setCharacter(List<Integer> character) {
        this.character = character;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
