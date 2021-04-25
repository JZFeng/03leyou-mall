/**
 * @Author jzfeng
 * @Date 4/24/21-2:11 PM
 */

package com.leyou.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpuBo extends Spu{
    private String cname;
    private String bname;
}
