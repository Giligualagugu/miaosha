package com.kele.miaosha.controller;

import cn.hutool.core.util.IdUtil;
import com.kele.miaosha.SingleMiaoShaDTO;
import com.kele.miaosha.exception.BizRuntimeException;
import com.kele.miaosha.exception.KeleResult;
import com.kele.miaosha.service.MiaoShaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ArrayBlockingQueue;

@RestController
@RequestMapping("/miaosha")
public class MiaoShaController {

    private static final ArrayBlockingQueue<SingleMiaoShaDTO> SINGEL_MIAOSHA_QUEUE = new ArrayBlockingQueue<>(100);

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

    /**
     * 简单秒杀, 库存先放 redis 里, 付款后同步到 DB;
     *
     * @param pid
     * @return
     */
    @GetMapping("/simple-cache-kill/{pid}")
    public KeleResult<Long> simpleCacheKill(@PathVariable("pid") Integer pid) {
        String userId = IdUtil.fastSimpleUUID();
        Long orderId = miaoShaService.sinpleCacheKill(userId, pid);
        return new KeleResult<>(orderId).success();
    }

    /**
     * 请求入队后 从队列取出进行处理;
     * 适用于单系统部署;
     *
     * @param pid
     * @return
     */
    @GetMapping("/single-queue-kill/{pid}")
    public KeleResult<Long> queueKill(@PathVariable("pid") Integer pid) {
        // 每次请求模拟一个用户;
        String userId = IdUtil.fastSimpleUUID();
        SingleMiaoShaDTO singleMiaoShaDTO = new SingleMiaoShaDTO(pid, userId);

        if (SINGEL_MIAOSHA_QUEUE.offer(singleMiaoShaDTO)) {
            Long orderId = miaoShaService.singleQueueKill(singleMiaoShaDTO);
            return new KeleResult<>(orderId).success();
        }

        throw new BizRuntimeException("卖完了");
    }

    @GetMapping("/testheader")
    public KeleResult<String> testHeader(@RequestHeader("kele") String kele) {

        System.out.println(kele);
        return new KeleResult<>(kele).success();
    }
}
