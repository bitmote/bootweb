package com.luangeng.bootweb.service.impl;

import com.github.pagehelper.PageHelper;
import com.luangeng.bootweb.constant.WebConst;
import com.luangeng.bootweb.dao.LogVoMapper;
import com.luangeng.bootweb.modal.vo.LogVo;
import com.luangeng.bootweb.service.ILogService;
import com.luangeng.bootweb.util.DateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tangj
 * @date 2018/1/21 20:52
 */
@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private LogVoMapper logDao;

    @Override
    public void insertLog(LogVo logVo) {
        logDao.insert(logVo);
    }

    @Override
    public void insertLog(String action, String data, String ip, Integer authorId) {
        LogVo logs = new LogVo();
        logs.setAction(action);
        logs.setData(data);
        logs.setIp(ip);
        logs.setAuthorId(authorId);
        logs.setCreated(DateKit.getCurrentUnixTime());
        logDao.insert(logs);
    }

    @Override
    public List<LogVo> getLogs(int page, int limit) {
        if (page <= 0) {
            page = 1;
        }
        if (limit < 1 || limit > WebConst.MAX_POSTS) {
            limit = 10;
        }
        PageHelper.startPage((page - 1) * limit, limit);
        List<LogVo> logVos = logDao.select();
        return logVos;
    }
}
