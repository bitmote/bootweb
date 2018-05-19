package com.luangeng.bootweb.dao;

import com.luangeng.bootweb.modal.vo.MetaVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MetaVoMapper {

    long countByType(String type);

    int countById(int mid);

    int deleteByPrimaryKey(Integer mid);

    List<MetaVo> selectByNameAndType(String name, String type);

    MetaVo selectByPrimaryKey(Integer mid);

    List<MetaVo> selectByType(String type);

    int insert(MetaVo metaVo);

    int updateByPrimaryKey(MetaVo record);

    int update(MetaVo metaVo);

}