package com.kele.miaosha.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.kele.miaosha.entity.OrderInfo;
import com.kele.miaosha.entity.ProductInfo;
import com.kele.miaosha.exception.BizRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class MiaoShaService {

    public static Snowflake snowflake = IdUtil.getSnowflake(1L, 1L);

    @Autowired
    IOrderInfoService orderInfoService;

    @Autowired
    IProductInfoService productInfoService;

    /**
     * 秒杀下单. 先扣库存,后返回订单;
     *
     * @param userId
     * @param productId
     */
    @Transactional
    public Long SimpleMiaosha(String userId, Integer productId) {

        ProductInfo productInfo = productInfoService.getById(productId);

        if (productInfo == null) {
            return null;
        }

        // 尝试下单
        Long orderCode = snowflake.nextId();
        if (saveOrder(orderCode, userId, productId)) {
            //下单成功后扣库存;
            Integer currentStock = productInfo.getStock();
            if (currentStock < 1) {
                throw new BizRuntimeException("购买失败...商品已售罄");
            }
            // 锁定库存;
            boolean decreFlag = productInfoService.decreaseStock(productId, currentStock);
            if (!decreFlag) {
                throw new BizRuntimeException("购买失败...你没有抢到");
            }

        } else {
            throw new BizRuntimeException("订单创建失败...你没有抢到");
        }

        return orderCode;
    }

    private boolean saveOrder(Long orderCode, String userId, Integer productId) {
        OrderInfo entity = new OrderInfo()
                .setOrderId(orderCode)
                .setUserId(userId)
                .setProductId(productId)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        return orderInfoService.save(entity);
    }
}
