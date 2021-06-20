/**
 * @Author jzfeng
 * @Date 5/12/21-8:25 PM
 */

package com.leyou.search.pojo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchResult extends PageResult<Goods> implements Serializable {
    private List<Brand> brands;
    private List<Category> categories;
    Map<String, Object> specs;

    public SearchResult(
            Long total,
            Long totalPage,
            List<Goods> items,
            List<Brand> brands,
            List<Category> categories,
            Map<String, Object> specs
    ) {
        super(total,totalPage,items);
        this.brands = brands;
        this.categories = categories;
        this.specs = specs;
    }

}
