package com.popzii.instamart.repository;

import com.popzii.instamart.domain.InstamartProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstamartProductRepository extends JpaRepository<InstamartProduct, Long> {
	Page<InstamartProduct> findByCategoryId(Long categoryId, Pageable pageable);
	Page<InstamartProduct> findByNameContainingIgnoreCase(String name, Pageable pageable);
	Page<InstamartProduct> findByCategoryIdAndNameContainingIgnoreCase(Long categoryId, String name, Pageable pageable);
}
