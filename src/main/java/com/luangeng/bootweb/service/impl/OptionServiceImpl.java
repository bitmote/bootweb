package com.luangeng.bootweb.service.impl;

import com.luangeng.bootweb.dao.OptionVoMapper;
import com.luangeng.bootweb.modal.vo.OptionVo;
import com.luangeng.bootweb.service.IOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OptionServiceImpl implements IOptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionServiceImpl.class);

    @Autowired
    private OptionVoMapper optionalDao;

    @Override
    public void insertOption(OptionVo optionVo) {
        optionalDao.insert(optionVo);
    }

    @Override
    public void insertOption(String name, String value) {
        LOGGER.debug("Enter insertOption method:name={},value={}", name, value);
        OptionVo optionVo = new OptionVo();

        optionVo.setName(name);
        optionVo.setValue(value);
        optionVo = optionalDao.selectByPrimaryKey(name);
        if ( optionVo == null) {
            optionVo = new OptionVo();
            optionVo.setName(name);
            optionVo.setValue(value);
            optionalDao.insert(optionVo);
        } else {
            optionVo = new OptionVo();
            optionVo.setName(name);
            optionVo.setValue(value);
            optionalDao.update(optionVo);
        }
        LOGGER.debug("Exit insertOption method.");
    }

    @Override
    public List<OptionVo> getOptions() {
        return optionalDao.select();
    }

    @Override
    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::insertOption);
        }
    }

    @Override
    public OptionVo getOptionByName(String name) {
        return null;
    }
}
