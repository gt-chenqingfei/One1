package com.oneone.modules.support.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
	private int id;
	private String name;
	private int parentId;
	private String code;
	private int order;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.name);
		dest.writeInt(this.parentId);
		dest.writeString(this.code);
		dest.writeInt(this.order);
	}

	public City() {
	}

	protected City(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.parentId = in.readInt();
		this.code = in.readString();
		this.order = in.readInt();
	}

	public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
		@Override
		public City createFromParcel(Parcel source) {
			return new City(source);
		}

		@Override
		public City[] newArray(int size) {
			return new City[size];
		}
	};

	@Override
	public String toString() {
		return "City{" +
				"id=" + id +
				", name='" + name + '\'' +
				", parentId=" + parentId +
				", code='" + code + '\'' +
				", order=" + order +
				'}';
	}
}
