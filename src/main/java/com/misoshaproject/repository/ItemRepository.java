package com.misoshaproject.repository;

import com.misoshaproject.bean.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
