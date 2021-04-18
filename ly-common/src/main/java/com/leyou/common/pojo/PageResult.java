/**
 * @Author jzfeng
 * @Date 4/17/21-3:03 PM
 */

package com.leyou.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private Long total;
    private Long totalPage;
    private List<T> items;


}
