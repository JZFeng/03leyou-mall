/**
 * @Author jzfeng
 * @Date 4/16/21-10:23 PM
 */

package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByParentId(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        List<Category> categoryList = this.categoryMapper.select(record);
        return categoryList;
    }

    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }

    public List<String> queryNameByIds(List<Long> ids) {
       /* return ids.stream().map(id ->{
            return this.categoryMapper.selectByPrimaryKey(id).getName();
        }).collect(Collectors.toList());*/

        return this.categoryMapper.selectByIdList(ids).stream().map(category -> {return category.getName();}).collect(Collectors.toList());
    }

    public List<Category> queryCategoryByIds(List<Long> ids) {
        List<Category> categories = this.categoryMapper.selectByIdList(ids);
        return categories;
    }
}
