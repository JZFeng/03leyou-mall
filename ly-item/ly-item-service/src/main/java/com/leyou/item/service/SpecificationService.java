/**
 * @Author jzfeng
 * @Date 4/24/21-12:26 PM
 */

package com.leyou.item.service;

import com.leyou.item.mapper.SpecificationMapper;
import com.leyou.item.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    public Specification queryByCategoryId(Long cid) {
        Specification specification = this.specificationMapper.selectByPrimaryKey(cid);
        return specification;
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
