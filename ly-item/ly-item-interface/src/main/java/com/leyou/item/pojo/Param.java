/**
 * @Author jzfeng
 * @Date 5/10/21-12:11 PM
 */

package com.leyou.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 临时用的，最佳设计应该是有对应的数据库表；
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class Param implements Serializable {

    private Long id;

    //参数属于哪个组
    private Long groupId;

    private String k;
    private boolean searchable;
    private boolean global;
    private boolean numerical;
    private String v;
    private String unit;


    private List<String> options;

}
