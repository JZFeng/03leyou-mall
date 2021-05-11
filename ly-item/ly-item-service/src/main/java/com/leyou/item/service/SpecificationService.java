/**
 * @Author jzfeng
 * @Date 4/24/21-12:26 PM
 */

package com.leyou.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.mapper.SpecificationMapper;
import com.leyou.item.pojo.Param;
import com.leyou.item.pojo.ParamGroup;
import com.leyou.item.pojo.Specification;
import com.leyou.item.pojo.SpuDetails;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecificationService {

    public static long param_counter = 1L;
    public static long paramGroup_counter = 1L;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpuDetailsService spuDetailsService;

    public Specification queryByCategoryId(Long cid) {
        Specification specification = this.specificationMapper.selectByPrimaryKey(cid);
        return specification;
    }

    /**
     * 根据spuId，查询所有该Spu的规格参数;
     * @param spuId
     * @return
     */
    public List<Param> queryParamsBySpuId(Long spuId) {

        SpuDetails spuDetails = spuDetailsService.queryBySpuId(spuId);
        String specifications = spuDetails.getSpecifications();
        List<Param> all_params = new ArrayList<>();
        if(StringUtils.isEmpty(specifications)) {
            return all_params;
        }

        //反序列化
        List<ParamGroup> paramGroups = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            paramGroups = mapper.readValue(specifications, new TypeReference<List<ParamGroup>>() {
            });
        } catch (JsonProcessingException e) {
            System.out.println("******出错的ID为：" + spuId);
            e.printStackTrace();
        }

        if(!CollectionUtils.isEmpty(paramGroups)) {
            paramGroups.forEach( paramGroup -> {
                paramGroup.setId(paramGroup_counter++);
                paramGroup.setSpuId(spuId);
                paramGroup.getParams().forEach( param -> {param.setGroupId(paramGroup.getId());param.setId(param_counter++);});
                all_params.addAll(paramGroup.getParams());
            });
        }

        return all_params;
    }


    public List<Param> querySpecialParamsBySpuId(Long spuId) {
        return queryParamsBySpuId(spuId).stream().filter(param -> !param.isGlobal()).collect(Collectors.toList());
    }


    public List<Param> queryGenericParamsBySpuId(Long spuId) {
        return queryParamsBySpuId(spuId).stream().filter(param -> param.isGlobal()).collect(Collectors.toList());
    }


    public static List<String> mergeParams(List<List<String>> params) {

        for( int i = 1; i < params.size(); i++ ) {
            params.set( i, mergeTwoParams(params.get(i - 1), params.get(i)));
        }

        return params.get(params.size() - 1);
    }


    private static List<String> mergeTwoParams(List<String> p1, List<String> p2 ) {
        List<String> res = new ArrayList<>();

        for( String s1 : p1) {
            for(String s2: p2){
                res.add(s1 + "_" + s2);
            }
        }

        return res;
    }

    //后台实现求笛卡尔积,[ ["红","黄"],["2G"，"3G"],["64G","128G","256G"] ]
    public static List<String> join(List<List<String>> specs, String separator) {

        List<String> reduce = specs.stream().reduce(  (l1, l2) -> {
            List<String> result = new ArrayList<>();

            l1.forEach(
                s1 -> {
                    l2.forEach(s2 -> {
                        result.add(s1 + separator + s2);
                    });
                }
            );

            return result;

        }).get();

        return reduce;
    }


}
