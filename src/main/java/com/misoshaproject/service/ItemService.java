package com.misoshaproject.service;

import com.misoshaproject.error.BusinessException;
import com.misoshaproject.model.ItemModel;

import java.util.List;

public interface ItemService {

    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

}
