package com.leyou.item.service;


import org.junit.Test;


import static com.leyou.item.service.SpecificationService.mergeParams;


public class SpecificationServiceTest {

    @Test
    public void testMergeParams() {
        String[][] params = new String[][]{{"苹果绿","亮橙"},{"4GB","6GB","3GB"},{"64GB","128GB","256GB"}};
        String[] res = mergeParams(params);
        System.out.println(res);
        for (String re : res) {
            System.out.println(re);
        }
    }

}