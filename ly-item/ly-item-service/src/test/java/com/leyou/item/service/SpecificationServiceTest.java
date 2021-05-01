package com.leyou.item.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


class SpecificationServiceTest {

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
}