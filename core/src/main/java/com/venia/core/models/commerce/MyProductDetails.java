package com.venia.core.models.commerce;

import org.osgi.annotation.versioning.ProviderType;

import com.adobe.cq.commerce.core.components.models.product.Product;

@ProviderType
public interface MyProductDetails extends Product {
	
	 public int getReviewsCount();
	 
	 public int getRatingSummery();
	 
	 public double getAverageRating();
	 
	 
	 

	
}
