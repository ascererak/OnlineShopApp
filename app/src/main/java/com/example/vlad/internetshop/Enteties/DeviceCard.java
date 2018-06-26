package com.example.vlad.internetshop.Enteties;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceCard implements Serializable{

    @SerializedName("deviceId")
    @Expose
    private Integer deviceId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("longDescription")
    @Expose
    private String longDescription;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("imageThumbnailUrl")
    @Expose
    private String imageThumbnailUrl;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("inStok")
    @Expose
    private Integer inStok;
    @SerializedName("isPopular")
    @Expose
    private Boolean isPopular;
    @SerializedName("bought")
    @Expose
    private Integer bought;
    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("promotional")
    @Expose
    private Integer promotional;
    @SerializedName("addDate")
    @Expose
    private String addDate;

    public DeviceCard(){
        name = "Noname";
        deviceId = -1;
        shortDescription = "no";
        longDescription = "no";
        price = 0d;
        imageThumbnailUrl = "";
        imageUrl = "";
        inStok = 0;
        isPopular = true;
        bought = 0;
        categoryId = 0;
        promotional = 0;
        addDate = "";
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;
    }

    public void setImageThumbnailUrl(String imageThumbnailUrl) {
        this.imageThumbnailUrl = imageThumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getInStok() {
        return inStok;
    }

    public void setInStok(Integer inStok) {
        this.inStok = inStok;
    }

    public Boolean getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(Boolean isPopular) {
        this.isPopular = isPopular;
    }

    public Integer getBought() {
        return bought;
    }

    public void setBought(Integer bought) {
        this.bought = bought;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPromotional() {
        return promotional;
    }

    public void setPromotional(Integer promotional) {
        this.promotional = promotional;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

}