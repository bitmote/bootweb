package com.luangeng.bootweb.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.luangeng.bootweb.modal.vo.CommentVo;
import com.luangeng.bootweb.modal.vo.CommentVoExample;

import java.util.List;

@Component
public interface CommentVoMapper {
    long countByExample(CommentVoExample example);

    int deleteByExample(CommentVoExample example);

    int deleteByPrimaryKey(Integer coid);

    int insert(CommentVo record);

    int insertSelective(CommentVo record);

    List<CommentVo> selectByExampleWithBLOBs(CommentVoExample example);

    List<CommentVo> selectByExample(CommentVoExample example);

    CommentVo selectByPrimaryKey(Integer coid);

    int updateByExampleSelective(@Param("record") CommentVo record, @Param("example") CommentVoExample example);

    int updateByExampleWithBLOBs(@Param("record") CommentVo record, @Param("example") CommentVoExample example);

    int updateByExample(@Param("record") CommentVo record, @Param("example") CommentVoExample example);

    int updateByPrimaryKeySelective(CommentVo record);

    int updateByPrimaryKeyWithBLOBs(CommentVo record);

    int updateByPrimaryKey(CommentVo record);
}