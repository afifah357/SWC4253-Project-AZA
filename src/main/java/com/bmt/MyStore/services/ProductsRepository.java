package com.bmt.MyStore.services;

import com.bmt.MyStore.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Long> {
}

