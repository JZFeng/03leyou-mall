/**
 * @Author jzfeng
 * @Date 4/24/21-12:26 PM
 */

package com.leyou.item.service;

import com.leyou.item.mapper.SpecificationMapper;
import com.leyou.item.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    public Specification queryByCategoryId(Long id) {
        Specification specification = this.specificationMapper.selectByPrimaryKey(id);
        return specification;
    }
}
