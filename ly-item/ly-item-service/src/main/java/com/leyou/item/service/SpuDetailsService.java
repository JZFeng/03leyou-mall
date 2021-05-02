/**
 * @Author jzfeng
 * @Date 5/1/21-9:04 PM
 */

package com.leyou.item.service;

import com.leyou.item.mapper.SpuDetailsMapper;
import com.leyou.item.pojo.SpuDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpuDetailsService {
    @Autowired
    SpuDetailsMapper spuDetailsMapper;

    public SpuDetails queryBySpuId(Long spuId) {
        SpuDetails record = new SpuDetails();
        record.setSpuId(spuId);
        return spuDetailsMapper.selectOne(record);
    }
}
