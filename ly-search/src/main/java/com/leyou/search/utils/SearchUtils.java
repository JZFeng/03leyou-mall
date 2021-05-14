/**
 * @Author jzfeng
 * @Date 5/14/21-1:23 AM
 */

package com.leyou.search.utils;

import com.leyou.item.pojo.Param;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchUtils {
    public static Map<String, Object> generateSpecsFromParamList(List<Param> params) {
        Map<String, Object> param_map = new HashMap<>(); //最后存到specs字段中
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach(param -> {
                String k = param.getK();
                List<String> options = param.getOptions();
                if (CollectionUtils.isEmpty(options)) {
                    //v有值，记得加单位
                    String v = param.getV() + (org.springframework.util.StringUtils.isEmpty(param.getUnit()) ? "" : param.getUnit());
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
