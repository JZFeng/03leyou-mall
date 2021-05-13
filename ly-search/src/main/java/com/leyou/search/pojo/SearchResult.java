/**
 * @Author jzfeng
 * @Date 5/12/21-8:25 PM
 */

package com.leyou.search.pojo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.Param;
import lombok.*;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchResult extends PageResult<Goods> {
    private List<Brand> brands;
    private List<Category> categories;
    private List<Param> params;

    public SearchResult(
            Long total,
            Long totalPage,
            List<Goods> items,
            List<Brand> brands,
            List<Category> categories,
            List<Param> params
    ) {
        super(total,totalPage,items);
        this.brands = brands;
        this.categories = categories;
        this.params = params;
    }

}
