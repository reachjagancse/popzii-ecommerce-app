package com.popzii.instamart.repository;

import com.popzii.instamart.domain.InstamartProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstamartProductVariantRepository extends JpaRepository<InstamartProductVariant, Long> {
	Page<InstamartProductVariant> findByProductId(Long productId, Pageable pageable);
}
