package com.carefactor.samup4web.cf;

public class WishListItem {

	private String foodId;
	private String wishlist_id;
	private String foodName;
	private String producer;
	private String producer_id;
	private String category;
	private String expiryDate;
	private boolean isFree;
	private double price;
	private String pickUpDateStart;
	private String pickUpDateEnd;
	private String updatedTime;
	private int quantity;
	private String currency;
	private String dateBooked;
	private int bookedQuantity;
	private String unit;
	private String description;
	private String uploadDate;
	
	public WishListItem(){
		this.foodName = "XXXXX";
		this.foodId = "XXXXX";
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFoodId() {
		return foodId;
	}

	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPickUpDateStart() {
		return pickUpDateStart;
	}

	public void setPickUpDateStart(String pickUpDateStart) {
		this.pickUpDateStart = pickUpDateStart;
	}

	public String getPickUpDateEnd() {
		return pickUpDateEnd;
	}

	public void setPickUpDateEnd(String pickUpDateEnd) {
		this.pickUpDateEnd = pickUpDateEnd;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDateBooked() {
		return dateBooked;
	}

	public void setDateBooked(String dateBooked) {
		this.dateBooked = dateBooked;
	}

	public int getBookedQuantity() {
		return bookedQuantity;
	}

	public void setBookedQuantity(int bookedQuantity) {
		this.bookedQuantity = bookedQuantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getProducer_id() {
		return producer_id;
	}

	public void setProducer_id(String producer_id) {
		this.producer_id = producer_id;
	}

	public String getWishlist_id() {
		return wishlist_id;
	}

	public void setWishlist_id(String foodWishlist_id) {
		this.wishlist_id = foodWishlist_id;
	}

}
