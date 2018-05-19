package com.luangeng.bootweb.dao;

import com.luangeng.bootweb.modal.vo.LogVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LogVoMapper {

    int insert(LogVo record);

    List<LogVo> select();

}