/**
 * @Author jzfeng
 * @Date 4/17/21-3:03 PM
 */

package com.leyou.common.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class PageResult<T> implements Serializable {
    private Long total;
    private Long totalPage;
    private List<T> items;

}
