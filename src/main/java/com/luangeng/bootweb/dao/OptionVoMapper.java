package com.luangeng.bootweb.dao;

import com.luangeng.bootweb.modal.vo.OptionVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OptionVoMapper {

    int insert(OptionVo record);

    List<OptionVo> select();

    OptionVo selectByPrimaryKey(String name);

    int update(OptionVo record);

}