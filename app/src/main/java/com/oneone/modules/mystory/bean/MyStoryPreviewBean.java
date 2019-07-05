package com.oneone.modules.mystory.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class MyStoryPreviewBean implements Parcelable {
    private ArrayList<Summary> summary;
    private ArrayList<StoryImg> imgs;
    private ArrayList<Character> character;
    private ArrayList<Couple> couple;
    private ArrayList<Occupation> occupation;
    private ArrayList<Values> values;
    private ArrayList<Experience> experience;

    public ArrayList<Summary> getSummary() {
        return summary;
    }

    public void setSummary(ArrayList<Summary> summary) {
        this.summary = summary;
    }

    public ArrayList<StoryImg> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<StoryImg> imgs) {
        this.imgs = imgs;
    }

    public ArrayList<Character> getCharacter() {
        return character;
    }

    public void setCharacter(ArrayList<Character> character) {
        this.character = character;
    }

    public ArrayList<Couple> getCouple() {
        return couple;
    }

    public void setCouple(ArrayList<Couple> couple) {
        this.couple = couple;
    }

    public ArrayList<Occupation> getOccupation() {
        return occupation;
    }

    public void setOccupation(ArrayList<Occupation> occupation) {
        this.occupation = occupation;
    }

    public ArrayList<Values> getValues() {
        return values;
    }

    public void setValues(ArrayList<Values> values) {
        this.values = values;
    }

    public ArrayList<Experience> getExperience() {
        return experience;
    }

    public void setExperience(ArrayList<Experience> experience) {
        this.experience = experience;
    }


    @Override
    public String toString() {
        return "MyStoryPreviewBean{" +
                "summary=" + summary +
                ", imgs=" + imgs +
                ", character=" + character +
                ", couple=" + couple +
                ", occupation=" + occupation +
                ", values=" + values +
                ", experience=" + experience +
                '}';
    }

    public class Paragraph implements Serializable{
        private String text;
        private String type;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public class Summary extends Paragraph {
    }

    public class Character extends Paragraph  {
    }

    public class Couple extends Paragraph {
    }

    public class Occupation extends Paragraph {
    }

    public class Values extends Paragraph {
    }

    public class Experience extends Paragraph {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.summary);
        dest.writeList(this.imgs);
        dest.writeList(this.character);
        dest.writeList(this.couple);
        dest.writeList(this.occupation);
        dest.writeList(this.values);
        dest.writeList(this.experience);
    }

    public MyStoryPreviewBean() {
    }

    protected MyStoryPreviewBean(Parcel in) {
        this.summary = new ArrayList<Summary>();
        in.readList(this.summary, Summary.class.getClassLoader());
        this.imgs = new ArrayList<StoryImg>();
        in.readList(this.imgs, StoryImg.class.getClassLoader());
        this.character = new ArrayList<Character>();
        in.readList(this.character, Character.class.getClassLoader());
        this.couple = new ArrayList<Couple>();
        in.readList(this.couple, Couple.class.getClassLoader());
        this.occupation = new ArrayList<Occupation>();
        in.readList(this.occupation, Occupation.class.getClassLoader());
        this.values = new ArrayList<Values>();
        in.readList(this.values, Values.class.getClassLoader());
        this.experience = new ArrayList<Experience>();
        in.readList(this.experience, Experience.class.getClassLoader());
    }

    public static final Parcelable.Creator<MyStoryPreviewBean> CREATOR = new Parcelable.Creator<MyStoryPreviewBean>() {
        @Override
        public MyStoryPreviewBean createFromParcel(Parcel source) {
            return new MyStoryPreviewBean(source);
        }

        @Override
        public MyStoryPreviewBean[] newArray(int size) {
            return new MyStoryPreviewBean[size];
        }
    };
}
