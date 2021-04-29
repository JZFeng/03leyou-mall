/**
 * @Author jzfeng
 * @Date 4/24/21-2:11 PM
 */

package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpuBo extends Spu{
    @Transient
    private String cname;

    @Transient
    private String bname;

    @Transient
    private SpuDetails spuDetails;

    @Transient
    private List<Sku> skus;
}
