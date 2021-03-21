package com.misoshaproject.repository;

import com.misoshaproject.bean.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemStockRepository extends JpaRepository<ItemStock,Long> {
            ItemStock findByItemId(Integer itemId);
}
