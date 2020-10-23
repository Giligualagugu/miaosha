package com.kele.miaosha.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.kele.miaosha.SingleMiaoShaDTO;
import com.kele.miaosha.constant.MiaoShaConstant;
import com.kele.miaosha.entity.OrderInfo;
import com.kele.miaosha.entity.ProductInfo;
import com.kele.miaosha.exception.BizRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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


    @Autowired
    RedisTemplate<String, Long> redisTemplate;


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
//        OrderInfo entity = new OrderInfo()
//                .setOrderId(orderCode)
//                .setUserId(userId)
//                .setProductId(productId)
//                .setCreateTime(LocalDateTime.now())
//                .setUpdateTime(LocalDateTime.now());
//        return orderInfoService.save(entity);
        return true;
    }

    /**
     * 不是很好的方案;
     * <p>
     * 单机版秒杀,  使用队列过滤前100个请求;
     * <p>
     * 订单失效的客户从队列移除;
     *
     * @param singleMiaoShaDTO
     * @return
     */
    public Long singleQueueKill(SingleMiaoShaDTO singleMiaoShaDTO) {
        Long orderCode = snowflake.nextId();
        saveOrder(orderCode, singleMiaoShaDTO.getUserId(), singleMiaoShaDTO.getProductId());

        productInfoService.decreaseStock(singleMiaoShaDTO.getProductId());

        return orderCode;
    }


    /**
     * 缓存商品的库存, 先扣除redis, 订单支付后, 扣除DB;
     *
     * @param userId
     * @param pid
     * @return
     */
    @Transactional
    public Long sinpleCacheKill(String userId, Integer pid) {

        Long orderCode = snowflake.nextId();


        Long current = redisTemplate.opsForValue().get(MiaoShaConstant.MIAOSHA_PREFIX + pid);

        if (current == null || current < 1) {
            throw new BizRuntimeException("购买失败...商品已售罄");
        }

        if (saveOrder(orderCode, userId, pid)) {
            // 预扣除 redis缓存;
            Long stock = redisTemplate.opsForValue().decrement(MiaoShaConstant.MIAOSHA_PREFIX + pid);
            log.info("剩余库存:{}", stock);
        } else {
            throw new BizRuntimeException("订单创建失败...你没有抢到");
        }

        return orderCode;
    }
}
