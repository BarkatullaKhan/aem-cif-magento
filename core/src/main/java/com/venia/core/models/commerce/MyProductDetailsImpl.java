package com.venia.core.models.commerce;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

import com.adobe.cq.commerce.core.components.models.common.Price;
import com.adobe.cq.commerce.core.components.models.product.Asset;
import com.adobe.cq.commerce.core.components.models.product.GroupItem;
import com.adobe.cq.commerce.core.components.models.product.Product;
import com.adobe.cq.commerce.core.components.models.product.Variant;
import com.adobe.cq.commerce.core.components.models.product.VariantAttribute;
import com.adobe.cq.commerce.core.components.models.retriever.AbstractProductRetriever;

@Model(adaptables = SlingHttpServletRequest.class, adapters = MyProductDetails.class, resourceType = MyProductDetailsImpl.RESOURCE_TYPE,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class MyProductDetailsImpl implements MyProductDetails {
	
	protected static final String RESOURCE_TYPE = "venia/components/custom/product";

	@Self
	private Product product;

	@ScriptVariable
	private ValueMap properties;

	private AbstractProductRetriever productRetriever;

	private static final String PRODUCT_REVIEWS_COUNT = "review_count";
	private static final String RATING_SUMMERY = "rating_summary";

	@PostConstruct
	public void initModel() {
		productRetriever = product.getProductRetriever();

		if (productRetriever != null) {
			// Pass your custom partial query to the ProductRetriever. This
			// class will
			// automatically take care of executing your query as soon
			// as you try to access any product property.
			productRetriever.extendProductQueryWith(
					p -> p
					.addCustomSimpleField(PRODUCT_REVIEWS_COUNT)
					.addCustomSimpleField(RATING_SUMMERY)

			);

		}
	}

	@Override
	public int getReviewsCount() {
		Integer reviewsCount;
		try {
			reviewsCount=productRetriever.fetchProduct().getAsInteger(PRODUCT_REVIEWS_COUNT);
			return reviewsCount;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return 0;
	}

	@Override
	public int getRatingSummery() {
		Integer ratingSummery;
		try {
			ratingSummery=productRetriever.fetchProduct().getAsInteger(RATING_SUMMERY);
			
			return ratingSummery;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return 0;
	}
	
	@Override
	public double getAverageRating() {
		Integer ratingSummery;
		Integer reviewsCount;
		
		try {
			reviewsCount=productRetriever.fetchProduct().getAsInteger(PRODUCT_REVIEWS_COUNT);
			ratingSummery=productRetriever.fetchProduct().getAsInteger(RATING_SUMMERY);
			return  Math.round((5.0*(double)ratingSummery/(100.0)) * 100.0) / 100.0;
		} catch (Exception e) {
		}
		
		return 0;
	}

	@Override
	public Boolean getFound() {

		return product.getFound();
	}

	@Override
	public String getName() {

		return product.getName();
	}

	@Override
	public String getDescription() {

		return product.getDescription();
	}

	@Override
	public String getSku() {

		return product.getSku();
	}

	@Override
	public String getCurrency() {

		return product.getCurrency();
	}

	@Override
	public Double getPrice() {

		return product.getPrice();
	}

	@Override
	public Price getPriceRange() {

		return product.getPriceRange();
	}

	@Override
	public String getFormattedPrice() {
		return product.getFormattedPrice();
	}

	@Override
	public Boolean getInStock() {

		return product.getInStock();
	}

	@Override
	public Boolean isConfigurable() {

		return product.isConfigurable();
	}

	@Override
	public Boolean isGroupedProduct() {

		return product.isGroupedProduct();
	}

	@Override
	public Boolean isVirtualProduct() {

		return product.isVirtualProduct();
	}

	@Override
	public Boolean isBundleProduct() {

		return product.isBundleProduct();
	}

	@Override
	public String getVariantsJson() {

		return product.getVariantsJson();
	}

	@Override
	public List<Variant> getVariants() {

		return product.getVariants();
	}

	@Override
	public List<GroupItem> getGroupedProductItems() {

		return product.getGroupedProductItems();
	}

	@Override
	public List<Asset> getAssets() {

		return product.getAssets();
	}

	@Override
	public String getAssetsJson() {

		return product.getAssetsJson();
	}

	@Override
	public List<VariantAttribute> getVariantAttributes() {

		return product.getVariantAttributes();
	}

	@Override
	public Boolean loadClientPrice() {

		return product.loadClientPrice();
	}

	

	@Override
	public String getMetaDescription() {

		return product.getMetaDescription();
	}

	@Override
	public String getMetaKeywords() {

		return product.getMetaKeywords();
	}

	@Override
	public String getMetaTitle() {

		return product.getMetaTitle();
	}

	@Override
	public String getCanonicalUrl() {

		return product.getCanonicalUrl();
	}

	@Override
	public AbstractProductRetriever getProductRetriever() {
		// TODO Auto-generated method stub
		return null;
	}

}
