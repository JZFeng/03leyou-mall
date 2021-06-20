/**
 * @Author jzfeng
 * @Date 4/24/21-12:12 PM
 */

package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "tb_specification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Specification implements Serializable {
    @Id
    private Long categoryId;
    private String specifications;

}
