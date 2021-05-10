/**
 * @Author jzfeng
 * @Date 4/17/21-3:28 PM
 */

package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    BrandMapper brandMapper;

    /*
        单表查询，通用mapper的selectByExample
    **/
    public PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {

        //准备查询条件
        Example example = new Example(Brand.class);
        if(StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }
        if(StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " desc" : " asc");
            example.setOrderByClause(orderByClause);
        }

        //开始分页
        PageHelper.startPage(page,rows);

        List<Brand> brands = brandMapper.selectByExample(example);
        PageInfo<Brand> pageInfo = new PageInfo<Brand>(brands);

        return new PageResult<Brand>(pageInfo.getTotal(), (long) pageInfo.getPages(),pageInfo.getList());
    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        this.brandMapper.insertSelective(brand);

        for (Long cid : cids) {
            this.brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    public List<Brand> queryBrandByCid(Long cid) {
        return this.brandMapper.queryBrandByCid(cid);
    }

    public Brand queryBrandById(Long id) {
        Brand res = null;
        Brand brand = this.brandMapper.selectByPrimaryKey(id);
        if(brand != null) {
            res = brand;
        }
        return res;
    }
}
