package com.kele.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kele.miaosha.entity.ProductInfo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xujiale
 * @since 2020-10-23
 */
public interface IProductInfoService extends IService<ProductInfo> {

    boolean decreaseStock(Integer productId, Integer currentStock);

    void decreaseStock(Integer productId);
}
