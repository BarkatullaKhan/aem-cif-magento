/*******************************************************************************
 *
 *    Copyright 2019 Adobe. All rights reserved.
 *    This file is licensed to you under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License. You may obtain a copy
 *    of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software distributed under
 *    the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 *    OF ANY KIND, either express or implied. See the License for the specific language
 *    governing permissions and limitations under the License.
 *
 ******************************************************************************/
package com.venia.core.models.commerce;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import com.adobe.cq.commerce.core.components.datalayer.ProductData;
import com.adobe.cq.commerce.core.components.models.common.Price;
import com.adobe.cq.commerce.core.components.models.productteaser.ProductTeaser;
import com.adobe.cq.commerce.core.components.models.retriever.AbstractProductRetriever;
import com.adobe.cq.commerce.magento.graphql.SimpleProductQuery;
import com.shopify.graphql.support.SchemaViolationError;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class, adapters = MyProductTeaser.class, resourceType = MyProductTeaserImpl.RESOURCE_TYPE)
public class MyProductTeaserImpl implements MyProductTeaser {

	protected static final String RESOURCE_TYPE = "venia/components/commerce/productteaser";

	private static final String ECO_FRIENDLY_ATTRIBUTE = "eco_friendly";
	private static final String PRODUCT_REVIEWS_COUNT = "review_count";

	private static final Logger LOGGER = LoggerFactory.getLogger(MyProductTeaserImpl.class);

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Self
	@Via(type = ResourceSuperType.class)
	private ProductTeaser productTeaser;
	

	@ScriptVariable
	private ValueMap properties;

	private AbstractProductRetriever productRetriever;

	@PostConstruct
	public void initModel() {
		productRetriever = productTeaser.getProductRetriever();
	

		if (productRetriever != null) {
			// Pass your custom partial query to the ProductRetriever. This
			// class will
			// automatically take care of executing your query as soon
			// as you try to access any product property.
			productRetriever.extendProductQueryWith(p -> p
					.createdAt()
					.addCustomSimpleField(ECO_FRIENDLY_ATTRIBUTE)
					.addCustomSimpleField(PRODUCT_REVIEWS_COUNT)
					
					);
			
		}
	}

	@Override
	public Boolean isShowBadge() {
		final boolean showBadge = properties.get("badge", false);
		if (showBadge) {
			return true;
			// final int maxAgeProp = properties.get("age", 0);
			//
			// // Custom code to calc the date difference of the product
			// creation
			// // compared to today
			// final LocalDate createdAt =
			// LocalDate.parse(productRetriever.fetchProduct().getCreatedAt(),
			// formatter);
			// if (createdAt != null) {
			// final long age = ChronoUnit.DAYS.between(createdAt,
			// LocalDate.now());
			// if (age < maxAgeProp) {
			// return true;
			// }
			// }
		}
		return false;
	}

	@Override
	public Boolean isEcoFriendly() {

		final boolean showEcofriendlyBadge = properties.get("ecofriendly", false);
		Integer ecoFriendlyValue;
		if (showEcofriendlyBadge) {
			return true;
		} else {
			try {
				ecoFriendlyValue = productRetriever.fetchProduct().getAsInteger(ECO_FRIENDLY_ATTRIBUTE);
				if (ecoFriendlyValue != null && ecoFriendlyValue.equals(Integer.valueOf(1))) {
					LOGGER.info("*** Product is Eco Friendly**");
					return true;
				}
			} catch (SchemaViolationError e) {
				LOGGER.error("Error retrieving eco friendly attribute");
			}
		}

		LOGGER.info("*** Product is not Eco Friendly**");
		return false;
	}
	
	

	@Override
	public String getFormattedPrice() {
		return productTeaser.getPriceRange().getFormattedFinalPrice();
	}

	@Override
	public Price getPriceRange() {
		return productTeaser.getPriceRange();
	}

	@Override
	public String getImage() {
		return productTeaser.getImage();
	}

	@Override
	public String getName() {
		return productTeaser.getName();
	}

	@Override
	public String getUrl() {
		return productTeaser.getUrl();
	}

	@Override
	public String getSku() {
		return productTeaser.getSku();
	}

	@Override
	public String getCallToAction() {
		return productTeaser.getCallToAction();
	}

	@Override
	public Boolean isVirtualProduct() {
		return productTeaser.isVirtualProduct();
	}

	@Override
	public AbstractProductRetriever getProductRetriever() {
		return productRetriever;
	}

	@Override
	public ProductData getData() {
		return (ProductData) productTeaser.getData();
	}

	@Override
	public String getId() {
		return productTeaser.getId();
	}
}
