package com.luangeng.bootweb.service.impl;

import com.github.pagehelper.PageHelper;
import com.luangeng.bootweb.constant.WebConst;
import com.luangeng.bootweb.dao.MetaVoMapper;
import com.luangeng.bootweb.dto.Types;
import com.luangeng.bootweb.exception.TipException;
import com.luangeng.bootweb.modal.vo.ContentVo;
import com.luangeng.bootweb.modal.vo.MetaVo;
import com.luangeng.bootweb.modal.vo.RelationshipVoKey;
import com.luangeng.bootweb.service.IContentService;
import com.luangeng.bootweb.service.IMetaService;
import com.luangeng.bootweb.service.IRelationshipService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tangj
 * @date 2018/1/24 21:59
 */
@Service
public class MetaServiceImpl implements IMetaService {

    @Autowired
    private MetaVoMapper metaDao;

    @Autowired
    private IRelationshipService relationshipService;

    @Autowired
    private IContentService contentService;

    @Override
    public MetaVo getMeta(String type, String name) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
            List<MetaVo> list = metaDao.selectByNameAndType(name, type);
            if (list.size() == 1) {
                return list.get(0);
            }
        }
        return null;
    }

    @Override
    public Integer countMeta(Integer mid) {
        return metaDao.countById(mid);
    }

    @Override
    public List<MetaVo> getMetas(String types) {
        if (StringUtils.isNotBlank(types)) {
            return metaDao.selectByType(types);
        }
        return null;
    }

    @Override
    public void saveMetas(Integer cid, String names, String type) {
        if (null == cid) {
            throw new TipException("项目关联id不能为空");
        }
        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArr = StringUtils.split(names, ",");
            for (String name : nameArr) {
                this.saveOrUpdate(cid, name, type);
            }
        }
    }

    private void saveOrUpdate(Integer cid, String name, String type) {
        List<MetaVo> metaVos = metaDao.selectByNameAndType(name, type);

        int mid;
        MetaVo metas;
        if (metaVos.size() == 1) {
            metas = metaVos.get(0);
            mid = metas.getMid();
        } else if (metaVos.size() > 1) {
            throw new TipException("查询到多条数据");
        } else {
            metas = new MetaVo();
            metas.setSlug(name);
            metas.setName(name);
            metas.setType(type);
            metaDao.insert(metas);
            mid = metas.getMid();
        }
        if (mid != 0) {
            Long count = relationshipService.countById(cid, mid);
            if (count == 0) {
                RelationshipVoKey relationships = new RelationshipVoKey();
                relationships.setCid(cid);
                relationships.setMid(mid);
                relationshipService.insertVo(relationships);
            }
        }
    }

    @Override
    public void saveMeta(String type, String name, Integer mid) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
            List<MetaVo> metaVos = metaDao.selectByNameAndType(name, type);
            MetaVo metas;
            if (metaVos.size() != 0) {
                throw new TipException("已经存在该项");
            } else {
                metas = new MetaVo();
                metas.setName(name);
                if (null != mid) {
                    MetaVo original = metaDao.selectByPrimaryKey(mid);
                    metas.setMid(mid);
                    metaDao.updateByPrimaryKey(metas);
//                    更新原有文章的categories
                    contentService.updateCategory(original.getName(),name);
                } else {
                    metas.setType(type);
                    metaDao.insert(metas);
                }
            }
        }
    }

    @Override
    public List<MetaVo> getMetaList(String type, int limit) {
        if (StringUtils.isNotBlank(type)) {
            if (limit < 1 || limit > WebConst.MAX_POSTS) {
                limit = 10;
            }
            PageHelper.startPage(1, limit);
            return metaDao.selectByType(type);
        }
        return null;
    }

    @Override
    public void delete(int mid) {
        MetaVo metas = metaDao.selectByPrimaryKey(mid);
        if (null != metas) {
            String type = metas.getType();
            String name = metas.getName();

            metaDao.deleteByPrimaryKey(mid);

            List<RelationshipVoKey> rlist = relationshipService.getRelationshipById(null, mid);
            if (null != rlist) {
                for (RelationshipVoKey r : rlist) {
                    ContentVo contents = contentService.getContents(String.valueOf(r.getCid()));
                    if (null != contents) {
                        ContentVo temp = new ContentVo();
                        temp.setCid(r.getCid());
                        if (type.equals(Types.CATEGORY.getType())) {
                            temp.setCategories(reMeta(name, contents.getCategories()));
                        }
                        if (type.equals(Types.TAG.getType())) {
                            temp.setTags(reMeta(name, contents.getTags()));
                        }
                        contentService.updateContentByCid(temp);
                    }
                }
            }
            relationshipService.deleteById(null, mid);
        }
    }

    private String reMeta(String name, String metas) {
        String[] ms = StringUtils.split(metas, ",");
        StringBuilder sbuf = new StringBuilder();
        for (String m : ms) {
            if (!name.equals(m)) {
                sbuf.append(",").append(m);
            }
        }
        if (sbuf.length() > 0) {
            return sbuf.substring(1);
        }
        return "";
    }

    @Override
    public void saveMeta(MetaVo metas) {
        if (null != metas) {
            metaDao.insert(metas);
        }
    }

    @Override
    public void update(MetaVo metas) {
        if (null != metas && null != metas.getMid()) {
            metaDao.update(metas);
        }
    }
}
