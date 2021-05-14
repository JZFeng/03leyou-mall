/**
 * @Author jzfeng
 * @Date 5/13/21-10:54 PM
 */

package com.leyou.item.utils;

import com.leyou.item.pojo.Param;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemUtils {

    public static Map<String, Object> generateSpecsFromParamList(List<Param> params) {
        Map<String, Object> param_map = new HashMap<>(); //最后存到specs字段中
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach(param -> {
                String k = param.getK();
                List<String> options = param.getOptions();
                if (CollectionUtils.isEmpty(options)) {
                    //v有值，记得加单位
                    String v = param.getV() + (StringUtils.isEmpty(param.getUnit()) ? "" : param.getUnit());
                    param_map.put(k, v);
                } else {
                    //Option有值
                    param_map.put(k, options);
                }
            });
        }

        return param_map;
    }
}
