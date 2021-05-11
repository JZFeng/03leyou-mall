/**
 * @Author jzfeng
 * @Date 5/11/21-8:56 AM
 */

package com.leyou.search.pojo;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SearchRequest {
    private static final Integer DEFAULT_SIZE = 20;
    private static final Integer DEFAULT_PAGE = 1;

    private String key = ""; //需要给缺省值
    private Integer page;
    private Integer size;
    private String sortBy = "";
    private Boolean descending = false;
    private Map<String, String> filter;


    public Integer getPage(){
        if(page == null ) {
            return DEFAULT_PAGE;
        }
        return Math.max(DEFAULT_PAGE, page);
    }

    public Integer getSize() {
        if(size == null) {
            size = DEFAULT_SIZE;
        }
        return Math.min(100, size);
    }
}
