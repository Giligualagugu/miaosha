package com.kele.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kele.miaosha.entity.ProductInfo;
import com.kele.miaosha.mapper.ProductInfoMapper;
import com.kele.miaosha.service.IProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xujiale
 * @since 2020-10-23
 */
@Slf4j
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements IProductInfoService {

    @Transactional
    @Override
    public boolean decreaseStock(Integer productId, Integer currentStock) {

        return update(new LambdaUpdateWrapper<ProductInfo>()
                .set(ProductInfo::getStock, currentStock - 1)
                .eq(ProductInfo::getProductId, productId)
                .eq(ProductInfo::getStock, currentStock));
    }
}
