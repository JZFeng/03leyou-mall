/**
 * @Author jzfeng
 * @Date 4/24/21-3:14 PM
 */

package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailsMapper spuDetailsMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private StockMapper stockMapper;

    public PageResult<SpuBo> querySpuByPageAndSort(Integer page, Integer rows, String key, Boolean saleable) {
        //准备查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.orEqualTo("saleable", saleable);
        }

        PageHelper.startPage(page, Math.min(rows, 100)); //最多允许查100条

        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<Spu>(spus);

        //把spu变成spuBo
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            List<String> names = this.categoryService.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "/"));

            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());

            return spuBo;
        }).collect(Collectors.toList());

        return new PageResult<SpuBo>(pageInfo.getTotal(), ((long) pageInfo.getPages()), spuBos);

    }

    @Transactional
    public void save(SpuBo spu) {
        //保存Spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        this.spuMapper.insertSelective(spu);

        //保存SpuDetails
        SpuDetails spuDetails = spu.getSpuDetails();
        spuDetails.setSpuId(spu.getId());
        this.spuDetailsMapper.insertSelective(spu.getSpuDetails());

        //保存Sku和Stork
        saveSkuAndStork(spu.getSkus(), spu.getId());
    }

    @Transactional
    private void saveSkuAndStork(List<Sku> skus, Long spuId) {
        for (Sku sku : skus) {
            //保存Sku
            if (!sku.getEnable()) {
                continue;
            }
            sku.setSpuId(spuId);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            //保存Stork
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        }
    }

    @Transactional
    public void updateGoods(SpuBo spu) {
        List<Sku> skus = this.skuService.queryBySpuId(spu.getId());

        //删除stock和sku
        if(!CollectionUtils.isEmpty(skus)) {
            List<Long> ids = skus.stream().map(sku -> {
                return sku.getId();
            }).collect(Collectors.toList());

            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId", ids);
            this.stockMapper.deleteByExample(example);

            Sku record = new Sku();
            record.setSpuId(spu.getId());
            this.skuMapper.delete(record);
        }

        //新建sku和stock
        saveSkuAndStork(spu.getSkus(), spu.getId());

        //更新spu
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spu);

        //更新SpuDetails
        this.spuDetailsMapper.updateByPrimaryKeySelective(spu.getSpuDetails());

    }

    /*
    //Sample SpuBo Request Body;
    {"bname":"OPPO","brandId":3177,"cid1":74,"cid2":75,"cid3":76,"cname":"手机/手机通讯/手机","skus":[{"title":"Oppo A9 青春版 金色 移动联通电信4G手机 双卡双待","images":"http://image.leyou.com/images/9/5/1524297314398.jpg","price":290000,"indexes":"0_0_0","ownSpec":"{\"机身颜色\":\"金\",\"内存\":\"4GB\",\"机身存储\":\"32GB\"}","stock":500,"enable":true}],"spuDetails":{"afterService":"3年","description":"<p>棒棒哒</p>","specTemplate":"{\"机身颜色\":[\"白色\",\"金色\",\"玫瑰金\"],\"内存\":[\"3GB\"],\"机身存储\":[\"16GB\"]}","specifications":"[{\"group\":\"主体\",\"params\":[{\"k\":\"品牌\",\"searchable\":false,\"global\":true,\"v\":\"三星（SAMSUNG）\"},{\"k\":\"型号\",\"searchable\":false,\"global\":true,\"v\":\"SM-C5000\"},{\"k\":\"上市年份\",\"searchable\":false,\"global\":true,\"numerical\":true,\"unit\":\"年\",\"v\":2016.0}]},{\"group\":\"基本信息\",\"params\":[{\"k\":\"机身颜色\",\"searchable\":false,\"global\":false,\"options\":[\"金\",\"粉\",\"灰\",\"银\"]},{\"k\":\"机身重量（g）\",\"searchable\":false,\"global\":true,\"numerical\":true,\"unit\":\"g\",\"v\":143},{\"k\":\"机身材质工艺\",\"searchable\":true,\"global\":true,\"v\":null}]},{\"group\":\"操作系统\",\"params\":[{\"k\":\"操作系统\",\"searchable\":true,\"global\":true,\"v\":\"Android\"}]},{\"group\":\"主芯片\",\"params\":[{\"k\":\"CPU品牌\",\"searchable\":true,\"global\":true,\"v\":\"骁龙（Snapdragon)\"},{\"k\":\"CPU型号\",\"searchable\":false,\"global\":true,\"v\":\"骁龙617（msm8952）\"},{\"k\":\"CPU核数\",\"searchable\":true,\"global\":true,\"v\":\"八核\"},{\"k\":\"CPU频率\",\"searchable\":true,\"global\":true,\"numerical\":true,\"unit\":\"GHz\",\"v\":1.5}]},{\"group\":\"存储\",\"params\":[{\"k\":\"内存\",\"searchable\":true,\"global\":false,\"numerical\":false,\"unit\":\"GB\",\"options\":[\"4GB\"]},{\"k\":\"机身存储\",\"searchable\":true,\"global\":false,\"numerical\":false,\"unit\":\"GB\",\"options\":[\"32GB\"]}]},{\"group\":\"屏幕\",\"params\":[{\"k\":\"主屏幕尺寸（英寸）\",\"searchable\":true,\"global\":true,\"numerical\":true,\"unit\":\"英寸\",\"v\":5.2},{\"k\":\"分辨率\",\"searchable\":false,\"global\":true,\"v\":\"1920*1080(FHD)\"}]},{\"group\":\"摄像头\",\"params\":[{\"k\":\"前置摄像头\",\"searchable\":true,\"global\":true,\"numerical\":true,\"unit\":\"万\",\"v\":800.0},{\"k\":\"后置摄像头\",\"searchable\":true,\"global\":true,\"numerical\":true,\"unit\":\"万\",\"v\":1600.0}]},{\"group\":\"电池信息\",\"params\":[{\"k\":\"电池容量（mAh）\",\"searchable\":true,\"global\":true,\"numerical\":true,\"unit\":\"mAh\",\"v\":2600}]}]","packingList":"手机（电池内置）*1，中式充电器*1，数据线*1，半入耳式线控耳机*1，华为手机凭证*1，快速指南*1，取卡针*1，屏幕保护膜（出厂已贴）*1"},"subTitle":"OPPO A9 8+128GB 雅银 双模5G 4300mAh大电池 30W疾速快充 OLED超清护眼屏 4800万三摄 全面屏轻薄拍照手机","title":"OPPO A9"}
     */

}
