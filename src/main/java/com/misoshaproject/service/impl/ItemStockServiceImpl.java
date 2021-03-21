package com.misoshaproject.service.impl;

import com.misoshaproject.repository.ItemRepository;
import com.misoshaproject.repository.ItemStockRepository;
import com.misoshaproject.service.ItemStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemStockServiceImpl implements ItemStockService {

    @Autowired
    ItemStockRepository itemStockRepository;
}
