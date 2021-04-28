/**
 * @Author jzfeng
 * @Date 4/28/21-9:46 AM
 */

package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tb_sku")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String ownSpec;
    private String indexes;
    private Boolean enable;
    private Date createTime;
    private Date lastUpdateTime;

    @Transient
    private Long stock;

}
