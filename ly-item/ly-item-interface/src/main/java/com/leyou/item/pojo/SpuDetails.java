/**
 * @Author jzfeng
 * @Date 4/24/21-1:52 PM
 */

package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_spu_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpuDetails {
    @Id
    private Long spuId;
    private String description;
    private String specifications;
    private String specTemplate;
    private String packingList;
    private String afterService;

}
