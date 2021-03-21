package com.misoshaproject.service.impl;

import com.misoshaproject.bean.Item;
import com.misoshaproject.bean.ItemStock;
import com.misoshaproject.error.BusinessException;
import com.misoshaproject.error.EmBusinessError;
import com.misoshaproject.model.ItemModel;
import com.misoshaproject.repository.ItemRepository;
import com.misoshaproject.repository.ItemStockRepository;
import com.misoshaproject.service.ItemService;
import com.misoshaproject.validator.ValidationResult;
import com.misoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemStockRepository itemStockRepository;

    @Autowired
    private ValidatorImpl validator;

    private Item convertItemDOFromItemModel(ItemModel itemModel){
        if (itemModel == null) {
            return null;
        }

        Item item = new Item();
        BeanUtils.copyProperties(itemModel,item);

        return item;
    }

    private ItemStock convertItemStockDOFromItemModel(ItemModel itemModel){
        if (itemModel == null) {
            return null;
        }

        ItemStock itemStock = new ItemStock();
        itemStock.setItemId(itemModel.getId());
        itemStock.setStock(itemModel.getStock());

        return  itemStock;
    }

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasError()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrorMsg());
        }

        //转化itemmodel->dataobject
        Item item = convertItemDOFromItemModel(itemModel);

        //写入数据库
        itemRepository.save(item);
        itemModel.setId(item.getId().intValue());

        ItemStock itemStock = convertItemStockDOFromItemModel(itemModel);
        itemStockRepository.save(itemStock);

        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        return null;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        Item item = itemRepository.getOne(id.longValue());
        if (item == null) {
            return  null;
        }

        //操作获得库存数量
        ItemStock itemstock = itemStockRepository.findByItemId(item.getId().intValue());

        //将dataobject -> model
        ItemModel itemModel = convertModelFromDataObject(item,itemstock);

        return itemModel;
    }

    private ItemModel convertModelFromDataObject(Item item,ItemStock itemStock) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(item,itemStock);
        itemModel.setStock(itemStock.getStock());

        return itemModel;
    }
}
