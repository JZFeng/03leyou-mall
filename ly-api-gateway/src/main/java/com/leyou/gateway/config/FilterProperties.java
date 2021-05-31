/**
 * @Author jzfeng
 * @Date 5/31/21-12:06 PM
 */

package com.leyou.gateway.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
@ConfigurationProperties(prefix = "leyou.filter")
public class FilterProperties {

    private List<String> allowPaths;

}
