package com.luangeng.bootweb.dao;

import com.luangeng.bootweb.modal.vo.UserVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tangj
 * @date 2018/1/21 14:59
 */
@Component
public interface UserVoMapper {

    long countByName(String name);

    int deleteByPrimaryKey(Integer uid);

    int insert(UserVo record);

    List<UserVo> select();

    UserVo selectByPrimaryKey(Integer uid);

    int update(UserVo record);

    List<UserVo> selectByNamePwd(String name, String pwd);

}
