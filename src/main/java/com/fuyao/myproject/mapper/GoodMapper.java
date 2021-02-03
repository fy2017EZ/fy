package com.fuyao.myproject.mapper;

import com.fuyao.myproject.entity.Good;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GoodMapper {
    @Insert(" insert into Medical_good_info(good_id,good_name,price,quality,nums,start_time,category_type,operation,category,warehousing_batch_number)" +
            " values(#{good_id},#{good_name},#{price},#{quality},#{nums},to_date(#{start_time},'yyyy/mm/dd hh:mi:ss'),#{category_type},#{operation},#{category}," +
            " #{warehousing_batch_number})")
    void saveGood(Good good);

    @Insert(" insert into Medical_${cityId}_good_info(good_id,good_name,price,quality,nums,start_time,category_type,operation,category,warehousing_batch_number)" +
            " values(#{good_id},#{good_name},#{price},#{quality},#{nums},to_date(#{start_time},'yyyy/mm/dd hh:mi:ss'),#{category_type},#{operation},#{category}," +
            " #{warehousing_batch_number})")
    void saveGoodByCity(Good good,String cityId);

    @Update("update Medical_good_info set good_name = #{good_name},price = #{price},quality = #{quality},nums = #{nums},start_time = to_date(#{start_time},'yyyy/mm/dd hh:mi:ss'), " +
            " category_type = #{category_type},operation = #{operation},category = #{category},warehousing_batch_number = #{warehousing_batch_number} where good_id = #{good_id} ")
    void updateGoodinfo(Good good);

    @Update("update Medical_${cityId}_good_info set good_name = #{good_name},price = #{price},quality = #{quality},nums = #{nums},start_time = to_date(#{start_time},'yyyy/mm/dd hh:mi:ss'),  " +
            " category_type = #{category_type},operation = #{operation},category = #{category},warehousing_batch_number = #{warehousing_batch_number} where good_id = #{good_id} ")
    void updateGoodinfoByCity(Good good,String cityId);

    @Delete("delete table Medical_good_info where good_id = #{goodId}")
    void deleteGoodInfoById(String goodId);

    @Delete("delete table Medical_${cityId}_good_info where good_id = #{goodId}")
    void deleteGoodInfoByCityAndId(String goodId,String cityId);

    @Select("select * from (select a.*,rownum as rn from Medical_good_info a)where rn between #{start} and #{end}")
    List<Good> findAllGood(String start,String end);
    @Select("select count(*) from (select a.*,rownum as rn from Medical_good_info a)")
    Integer findAllGoodCount();

    @Select("select * from (select a.*,rownum as rn from Medical_${cityId}_good_info a)where rn between #{start} and #{end}")
    List<Good> findCityGood(String cityId,String start,String end);
    @Select("select count(*) from (select a.*,rownum as rn from Medical_${cityId}_good_info a)")
   Integer findCityGoodCount(String cityId);

    @Select("select * from (select a.*,rownum as rn from Medical_good_info a ${WhereCol} ${ColValue} )where rn between #{start} and #{end}")
    List<Good> findAllGoodByOne(String WhereCol,String ColValue,String start,String end);
    @Select("select count(*) from (select a.*,rownum as rn from Medical_good_info a ${WhereCol} ${ColValue} )")
    Integer findAllGoodByOneCount(String WhereCol,String ColValue);

    @Select("select * from (select a.*,rownum as rn from Medical_${cityId}_good_info a ${WhereCol} ${ColValue})where rn between #{start} and #{end}")
    List<Good> findCityGoodByOne(String cityId,String WhereCol,String ColValue,String start,String end);
    @Select("select count(*) from (select a.*,rownum as rn from Medical_${cityId}_good_info a ${WhereCol} ${ColValue})")
   Integer findCityGoodByOneCount(String cityId,String WhereCol,String ColValue);

    @Select("select * from (select a.*,rownum as rn from Medical_good_info a ${WhereCol0} ${ColValue0} ${andCol1} ${Colvalue1} ${andCol2} ${ColValue2} ${andCol3} ${ColValue3} " +
            "  ${andCol4} ${ColValue4} ${andCol5} ${ColValue5} )where rn between #{start} and #{end}")
    List<Good> findAllGoodByMany(String WhereCol0,String ColValue0,String andCol1,String Colvalue1,String andCol2,String ColValue2,String andCol3,String ColValue3,
                                String andCol4,String ColValue4,String andCol5,String ColValue5,String start,String end);
    @Select("select count(*) from (select a.*,rownum as rn from Medical_good_info a ${WhereCol0} ${ColValue0} ${andCol1} ${Colvalue1} ${andCol2} ${ColValue2} ${andCol3} ${ColValue3} " +
            "  ${andCol4} ${ColValue4} ${andCol5} ${ColValue5} )")
    Integer findAllGoodByManyCount(String WhereCol0,String ColValue0,String andCol1,String Colvalue1,String andCol2,String ColValue2,String andCol3,String ColValue3,
                                  String andCol4,String ColValue4,String andCol5,String ColValue5);

    @Select("select * from (select a.*,rownum as rn from Medical_${cityId}_good_info a ${WhereCol0} ${ColValue0} ${andCol1} ${Colvalue1} ${andCol2} ${ColValue2} ${andCol3} ${ColValue3}  " +
            "  ${andCol4} ${ColValue4} ${andCol5} ${ColValue5})where rn between #{start} and #{end}")
    List<Good> findCityGoodByMany(String cityId,String WhereCol0,String ColValue0,String andCol1,String Colvalue1,String andCol2,String ColValue2,String andCol3,String ColValue3,
                                 String andCol4,String ColValue4,String andCol5,String ColValue5,String start,String end);
    @Select("select count(*) from (select a.*,rownum as rn from Medical_${cityId}_good_info a ${WhereCol0} ${ColValue0} ${andCol1} ${Colvalue1} ${andCol2} ${ColValue2} ${andCol3} ${ColValue3}  " +
            "   ${andCol4} ${ColValue4} ${andCol5} ${ColValue5})")
    Integer findCityGoodByManyCount(String cityId,String WhereCol0,String ColValue0,String andCol1,String Colvalue1,String andCol2,String ColValue2,String andCol3,String ColValue3,
                                   String andCol4,String ColValue4,String andCol5,String ColValue5);
    @Select("select good_name from Medical_good_info where good_id = #{goodId}")
    String findGoodNameById(String GoodId);
}