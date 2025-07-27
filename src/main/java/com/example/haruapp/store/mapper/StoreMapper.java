package com.example.haruapp.store.mapper;

import com.example.haruapp.store.dto.response.StoreSearchResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StoreMapper {

    /**
     * 카테고리, 이름으로 매장 검색 동적 sql
     *
     * 1. 카테고리만 선택한 경우
     * WHERE category IN ('카페 · 베이커리 · 디저트', 'F&B')
     *
     * 2. 카테고리 + 매장명 입력 경우
     * WHERE category IN ('카페 · 베이커리 · 디저트', 'F&B')
     *   AND store_name LIKE '%스타%'
     * */
    @Select({
            "<script>",
            "SELECT store_id, store_name, category, floor, image",
            "FROM store",
            "<where>",
            "  <if test='categories != null and categories.size > 0'>",
            "    category IN",
            "    <foreach collection='categories' item='cat' open='(' separator=',' close=')'>",
            "      #{cat}",
            "    </foreach>",
            "  </if>",
            "  <if test='(keyword != null and keyword != \"\")'>",
            "    <if test='categories != null and categories.size > 0'> AND </if>",
            "    store_name LIKE #{keyword} || '%'",
            "  </if>",
            "</where>",
            "ORDER BY store_name",
            "OFFSET #{offset} ROWS FETCH NEXT #{size} ROWS ONLY",
            "</script>"
    })
    List<StoreSearchResponse> findStores(@Param("categories") List<String> categories,
                                         @Param("keyword") String keyword,
                                         @Param("offset") int offset,
                                         @Param("size") int size);

}
