/**
 * @Author jzfeng
 * @Date 4/24/21-12:26 PM
 */

package com.leyou.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.mapper.SpecificationMapper;
import com.leyou.item.pojo.Param;
import com.leyou.item.pojo.ParamGroup;
import com.leyou.item.pojo.Specification;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecificationService {

    public static long param_counter = 1L;
    public static long paramGroup_counter = 1L;

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpuDetailsService spuDetailsService;

    public Specification queryByCategoryId(Long cid) {
        return this.specificationMapper.selectByPrimaryKey(cid);
    }

    /**
     * 根据spuId，查询所有该Spu的规格参数;
     * @param spuId
     * @return 因为是具体Spu，所以返回值中有具体的规格参数值
     *
     * 数据示范：
     * "specs": {"分辨率":"其他","品牌":"小米（MI）","CPU核数":"八核","后置摄像头":"1200万","机身重量（g）":"165g","型号":"5x","上市年份":"2017.0年","电池容量（mAh）":"3000mAh","机身存储":["64GB"],"机身颜色":["香槟金","黑色","红色","玫瑰金"],"CPU品牌":"骁龙（Snapdragon)","CPU型号":"骁龙625（MSM8953）","机身材质工艺":"null","CPU频率":"1.4GHz","操作系统":"Android","内存":["4GB"],"主屏幕尺寸（英寸）":"5.5英寸","前置摄像头":"500.0万"}
     * "specifications": [{"group":"主体","params":[{"k":"品牌","searchable":false,"global":true,"options":[]},{"k":"型号","searchable":false,"global":true,"options":[]},{"k":"上市年份","searchable":false,"global":true,"options":[],"numerical":true,"unit":"年"}]},{"group":"基本信息","params":[{"k":"机身颜色","searchable":false,"global":false,"options":[]},{"k":"机身重量（g）","searchable":false,"global":true,"options":[],"numerical":true,"unit":"g"},{"k":"机身材质工艺","searchable":false,"global":true,"options":[]}]},{"group":"操作系统","params":[{"k":"操作系统","searchable":true,"global":true,"options":["Android","IOS","Windows","功能机"]}]},{"group":"主芯片","params":[{"k":"CPU品牌","searchable":true,"global":true,"options":["骁龙（Snapdragon)","麒麟"]},{"k":"CPU型号","searchable":false,"global":true,"options":[]},{"k":"CPU核数","searchable":true,"global":true,"options":["一核","二核","四核","六核","八核","十核"]},{"k":"CPU频率","searchable":true,"global":true,"options":[],"numerical":true,"unit":"GHz"}]},{"group":"存储","params":[{"k":"内存","searchable":true,"global":false,"options":["1GB及以下","2GB","3GB","4GB","6GB","8GB"],"numerical":false,"unit":""},{"k":"机身存储","searchable":true,"global":false,"options":["8GB及以下","16GB","32GB","64GB","128GB","256GB"],"numerical":false,"unit":""}]},{"group":"屏幕","params":[{"k":"主屏幕尺寸（英寸）","searchable":true,"global":true,"options":[],"numerical":true,"unit":"英寸"},{"k":"分辨率","searchable":false,"global":true,"options":[]}]},{"group":"摄像头","params":[{"k":"前置摄像头","searchable":true,"global":true,"options":[],"numerical":true,"unit":"万"},{"k":"后置摄像头","searchable":true,"global":true,"options":[],"numerical":true,"unit":"万"}]},{"group":"电池信息","params":[{"k":"电池容量（mAh）","searchable":true,"global":true,"options":[],"numerical":true,"unit":"mAh"}]}]
     */
    public List<Param> querySpecsBySpuId(Long spuId) {
        String spec_str = this.spuDetailsService.queryBySpuId(spuId).getSpecifications();
        List<Param> specsBySpuId = getSpecsBySpecStr(spec_str);

        return specsBySpuId;
    }

    /**
     *
     * @param cid
     * @return 返回值中，只有规格参数名，没有具体的值；
     */
    public List<Param> querySpecsByCid(Long cid) {
        String spec_str = this.queryByCategoryId(cid).getSpecifications();
        List<Param> specsByCid = getSpecsBySpecStr(spec_str);
        return specsByCid;
    }

    private List<Param> getSpecsBySpecStr(String spec_str) {
        List<Param> specs = new ArrayList<>();
        if(StringUtils.isEmpty(spec_str)) {
            return specs;
        }

        List<ParamGroup> paramGroups = getSpecGroupFromJsonStr(spec_str);

        if(!CollectionUtils.isEmpty(paramGroups)) {
            paramGroups.forEach( paramGroup -> {
                paramGroup.setId(paramGroup_counter++);
                paramGroup.getParams().forEach( param -> {param.setGroupId(paramGroup.getId());param.setId(param_counter++);});
                specs.addAll(paramGroup.getParams());
            });
        }

        List<Param> genericSpecs = specs.stream().filter(param -> param.isGlobal()).collect(Collectors.toList());
        List<Param> specialSpecs = specs.stream().filter(param -> !param.isGlobal()).collect(Collectors.toList());

        return specs;
    }

    private List<ParamGroup> getSpecGroupFromJsonStr(String spec_str) {
        //反序列化
        List<ParamGroup> paramGroups = null;
        if(!StringUtils.isBlank(spec_str)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                paramGroups = mapper.readValue(spec_str, new TypeReference<List<ParamGroup>>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return paramGroups;
    }


    public List<ParamGroup> queryParamGroupByCid(Long cid) {
        List<ParamGroup> paramGroups = new ArrayList<>();
        String spec_str = this.queryByCategoryId(cid).getSpecifications();
        if(!StringUtils.isBlank(spec_str)) {
            paramGroups = this.getSpecGroupFromJsonStr(spec_str);
        }
        return paramGroups;
    }


    public List<Param> queryParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        List<Param> result = new ArrayList<>();
        if(cid != null && cid >= 0) {
            result = this.querySpecsByCid(cid);
        }

        result = result.stream().filter(param -> {
            if (gid != null && gid >= 0 && param.getGroupId() != gid) {
                return false;
            }
            if (searching != null && searching != param.isSearchable()) {
                return false;
            }
            if (generic != null && generic != param.isGlobal()) {
                return false;
            }

            return true;
        }).collect(Collectors.toList());

        if(gid != null && gid >= 0 ) {

        }

        return  result;
    }

    public static List<String> mergeParams(List<List<String>> params) {
        for( int i = 1; i < params.size(); i++ ) {
            params.set( i, mergeTwoParams(params.get(i - 1), params.get(i)));
        }

        return params.get(params.size() - 1);
    }

    private static List<String> mergeTwoParams(List<String> p1, List<String> p2 ) {
        List<String> res = new ArrayList<>();

        for( String s1 : p1) {
            for(String s2: p2){
                res.add(s1 + "_" + s2);
            }
        }

        return res;
    }

    //后台实现求笛卡尔积,[ ["红","黄"],["2G"，"3G"],["64G","128G","256G"] ]
    public static List<String> join(List<List<String>> specs, String separator) {

        List<String> reduce = specs.stream().reduce(  (l1, l2) -> {
            List<String> result = new ArrayList<>();

            l1.forEach(
                s1 -> {
                    l2.forEach(s2 -> {
                        result.add(s1 + separator + s2);
                    });
                }
            );

            return result;

        }).get();

        return reduce;
    }


}
