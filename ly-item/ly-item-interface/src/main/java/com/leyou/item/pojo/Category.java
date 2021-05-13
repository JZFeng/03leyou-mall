/**
 * @Author jzfeng
 * @Date 4/16/21-9:10 PM
 */

package com.leyou.item.pojo;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;


}
