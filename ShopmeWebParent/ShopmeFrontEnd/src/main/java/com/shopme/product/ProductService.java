package com.shopme.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 12;
	public static final int SEARCH_RESULTS_PER_PAGE = 12;
	
	@Autowired private ProductRepository repo;
	
	public Page<Product> listByCategory(int pageNum, Integer categoryId){
		String categoryIDMatch = "-" +  String.valueOf(categoryId) + "-";
		Pageable pageable =  PageRequest.of(pageNum -1, PRODUCTS_PER_PAGE);
		
		return repo.listByCategory(categoryId, categoryIDMatch, pageable);
	}
	
	public Product getProduct(String alias) throws ProductNotFoundException {
		Product product = repo.findByAlias(alias);
		if(product == null) {
			throw new ProductNotFoundException("Couldn't find any product in DB with :" +alias);
		}
		
		return product;
	}
	public Page<Product> search(String keyword, int pageNum){
		Pageable pageable =  PageRequest.of(pageNum -1, SEARCH_RESULTS_PER_PAGE);
		
		
		return repo.search(keyword, pageable);
	}
}
