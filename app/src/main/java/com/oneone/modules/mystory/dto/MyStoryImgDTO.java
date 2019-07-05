package com.oneone.modules.mystory.dto;

import com.oneone.modules.mystory.bean.StoryImg;

import java.util.ArrayList;

public class MyStoryImgDTO {
    private boolean preview;
    private boolean openSingle;
    private ArrayList<StoryImg> photos;

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public boolean isOpenSingle() {
        return openSingle;
    }

    public void setOpenSingle(boolean openSingle) {
        this.openSingle = openSingle;
    }

    public ArrayList<StoryImg> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<StoryImg> photos) {
        this.photos = photos;
    }

}

