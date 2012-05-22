package com.carefactor.samup4web.cf;

/**
 * 
 *  @author Samuel Idowu
 * 
 * @project CareFactor 
 * @Competition Ericsson Application Awards
 * 
 *
 */

import java.io.Serializable;

public class FoodBankItem implements Serializable{

	/**
	 * producer_id', 'category', 'foodname', 'description', 'expiry_date', 'is_free', 
	 * 'price', 
                'quantity', 'unit', 'pick_up_date_start', 'pick_up_date_end', 
                'currency','pick_up_date_end', 'update_timestamp'
	 */
	private static final long serialVersionUID = 1L;
	
	private String foodId;
	private String foodName;
	private String producer;
	private String category;
	private String expiryDate;
	private boolean isFree;
	private double price;
	private String pickUpDateStart;
	private String pickUpDateEnd;
	private int quantity;
	private String currency;
	private String unit;
	private String description;
	private String uploadDate;
	
	
	
	
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getFoodId() {
		return foodId;
	}
	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}
	
	
}