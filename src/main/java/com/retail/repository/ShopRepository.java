package com.retail.repository;

import com.retail.domain.Shop;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository {
    Shop add(Shop shop);
    Shop findOneNearest(Double lng, Double lat);
}
