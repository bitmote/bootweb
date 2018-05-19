package com.luangeng.bootweb.dao;

import com.luangeng.bootweb.modal.vo.AttachVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AttachVoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(AttachVo record);

    List<AttachVo> select();

    AttachVo selectByPrimaryKey(Integer id);

    long count();

}