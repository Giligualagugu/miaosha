package com.kele.miaosha.controller;

import cn.hutool.core.util.IdUtil;
import com.kele.miaosha.exception.KeleResult;
import com.kele.miaosha.service.MiaoShaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/miaosha")
public class MiaoShaController {


    @Autowired
    MiaoShaService miaoShaService;

    /**
     * 简单秒杀, 直接访问数据库;
     * <p>
     * 商品加锁
     *
     * @param productId
     * @return
     */
    @GetMapping("/simple-kill")
    public KeleResult<Long> simpleKill(@RequestParam("productId") Integer productId) {
        // 每次请求模拟一个用户;
        String userId = IdUtil.fastSimpleUUID();
        return new KeleResult<>(miaoShaService.SimpleMiaosha(userId, productId)).success();

    }
}
