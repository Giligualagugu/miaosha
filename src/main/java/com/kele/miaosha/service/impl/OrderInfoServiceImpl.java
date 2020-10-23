package com.kele.miaosha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kele.miaosha.entity.OrderInfo;
import com.kele.miaosha.mapper.OrderInfoMapper;
import com.kele.miaosha.service.IOrderInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xujiale
 * @since 2020-10-23
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

}
