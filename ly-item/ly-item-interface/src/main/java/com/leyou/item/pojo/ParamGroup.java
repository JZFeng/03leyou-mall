/**
 * @Author jzfeng
 * @Date 5/10/21-12:11 PM
 */

package com.leyou.item.pojo;

import lombok.*;

import java.util.List;

/**
 * 临时用的，最佳设计应该是有对应的数据库表；
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class ParamGroup {

    private Long id;

    private Long spuId;

    private String group;

    private List<Param> params;

}
