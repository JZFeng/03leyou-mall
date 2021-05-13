/**
 * @Author jzfeng
 * @Date 4/17/21-2:39 PM
 */

package com.leyou.item.pojo;

import lombok.*;

import javax.persistence.*;

@Table(name = "tb_brand")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    private Character letter;

}
