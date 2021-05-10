package com.leyou.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leyou.item.LyItemService;
import com.leyou.item.pojo.Param;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyItemService.class)
class SpecificationServiceTest {

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SpuDetailsService spuDetailsService;

    @Test
    void testJoin() {
        List<List<String>> specs = Arrays.asList(
                Arrays.asList("红", "黄", "蓝"),
                Arrays.asList("2G", "4G", "8G"),
                Arrays.asList("64GB", "128GB")
        );

        int expected_size = specs.stream().map( spec -> {
            return spec.size();
        }).reduce( (n1, n2) -> {
            return n1 * n2;
        }).get();

        List<String> reduce = SpecificationService.join(specs, "_");
        System.out.println("笛卡尔积总共：" + reduce.size() + ";\n\r结果集为：" + reduce);
        Assert.assertTrue( reduce.size() == expected_size );
    }

    @Test
    void testJoinAllEmpty() {
        List<List<String>> specs = Arrays.asList(
                Arrays.asList(),
                Arrays.asList(),
                Arrays.asList()
        );

        int expected_size = specs.stream().map( spec -> {
            return spec.size();
        }).reduce( (n1, n2) -> {
            return n1 * n2;
        }).get();

        List<String> reduce = SpecificationService.join(specs, "_");
        System.out.println("笛卡尔积总共：" + reduce.size() + ";\n\r结果集为：" + reduce);
        Assert.assertTrue( reduce.size() == expected_size );
    }

    @Test
    void testJoinOneEmpty() {
        List<List<String>> specs = Arrays.asList(
                Arrays.asList()
        );

        int expected_size = specs.stream().map( spec -> {
            return spec.size();
        }).reduce( (n1, n2) -> {
            return n1 * n2;
        }).get();

        List<String> reduce = SpecificationService.join(specs, "_");
        System.out.println("笛卡尔积总共：" + reduce.size() + ";\n\r结果集为：" + reduce);
        Assert.assertTrue( reduce.size() == expected_size );
    }

    @Test
    void testJoinOneList() {
        List<List<String>> specs = Arrays.asList(
                Arrays.asList("黄")
        );

        int expected_size = specs.stream().map( spec -> {
            return spec.size();
        }).reduce( (n1, n2) -> {
            return n1 * n2;
        }).get();

        List<String> reduce = SpecificationService.join(specs, "_");
        System.out.println("笛卡尔积总共：" + reduce.size() + ";\n\r结果集为：" + reduce);
        Assert.assertTrue( reduce.size() == expected_size );
    }


    @Test
    void testJoinTwoSingle() {
        List<List<String>> specs = Arrays.asList(
                Arrays.asList("黄"),
                Arrays.asList("2G")
        );

        int expected_size = specs.stream().map( spec -> {
            return spec.size();
        }).reduce( (n1, n2) -> {
            return n1 * n2;
        }).get();

        List<String> reduce = SpecificationService.join(specs, "_");
        System.out.println("笛卡尔积总共：" + reduce.size() + ";\n\r结果集为：" + reduce);
        Assert.assertTrue( reduce.size() == expected_size );
    }

    @Test
    void testJoinOneTwo() {
        List<List<String>> specs = Arrays.asList(
                Arrays.asList("黄","红"),
                Arrays.asList("2G")
        );

        int expected_size = specs.stream().map( spec -> {
            return spec.size();
        }).reduce( (n1, n2) -> {
            return n1 * n2;
        }).get();

        List<String> reduce = SpecificationService.join(specs, "_");
        System.out.println("笛卡尔积总共：" + reduce.size() + ";\n\r结果集为：" + reduce);
        Assert.assertTrue( reduce.size() == expected_size );
    }


    @Test
    void testMergeParams() {
        List<List<String>> specs = Arrays.asList(
                Arrays.asList("红", "黄", "蓝"),
                Arrays.asList("2G", "4G", "8G"),
                Arrays.asList("64GB", "128GB")
        );

        int expected_size = specs.stream().map( spec -> {
            return spec.size();
        }).reduce( (n1, n2) -> {
            return n1 * n2;
        }).get();

        List<String> reduce = SpecificationService.mergeParams(specs);
        System.out.println("笛卡尔积总共：" + reduce.size() + ";\n\r结果集为：" + reduce);
        Assert.assertTrue( reduce.size() == expected_size );
    }


    @Test
    public void testGetParams() throws JsonProcessingException {
        Long spuId = 243l;
        List<Param> all_params = specificationService.queryParamsBySpuId(spuId);
        specificationService.querySpecialParamsBySpuId(spuId).forEach(System.out::println);
        System.out.println("-----------------");
        specificationService.queryGenericParamsBySpuId(spuId).forEach(System.out::println);
    }

}