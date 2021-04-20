/**
 * @Author jzfeng
 * @Date 4/17/21-2:44 PM
 */

package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand(category_id, brand_id) values(#{cid}, #{id})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("id") Long id);

}
