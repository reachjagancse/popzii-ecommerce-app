package com.popzii.instamart.repository;

import com.popzii.instamart.domain.InstamartCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstamartCategoryRepository extends JpaRepository<InstamartCategory, Long> {
}
