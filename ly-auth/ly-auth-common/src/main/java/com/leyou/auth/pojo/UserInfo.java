/**
 * @Author jzfeng
 * @Date 5/28/21-7:34 AM
 */

package com.leyou.auth.pojo;

import lombok.*;

import java.io.Serializable;

@Data @Builder @AllArgsConstructor @NoArgsConstructor @ToString

public class UserInfo implements Serializable {

    private Long id;
    private String username;

}
