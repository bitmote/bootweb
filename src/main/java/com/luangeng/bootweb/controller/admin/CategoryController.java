package com.luangeng.bootweb.controller.admin;

import com.luangeng.bootweb.constant.WebConst;
import com.luangeng.bootweb.controller.AbstractController;
import com.luangeng.bootweb.controller.helper.ExceptionHelper;
import com.luangeng.bootweb.dto.Types;
import com.luangeng.bootweb.exception.TipException;
import com.luangeng.bootweb.modal.bo.RestResponseBo;
import com.luangeng.bootweb.modal.vo.MetaVo;
import com.luangeng.bootweb.service.IMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 分类标签管理
 *
 * @author tangj
 * @date 2018/1/31 22:56
 */
@Controller
@RequestMapping("admin/category")
public class CategoryController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private IMetaService metaService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        List<MetaVo> categories = metaService.getMetaList(Types.CATEGORY.getType(), WebConst.MAX_POSTS);
        List<MetaVo> tags = metaService.getMetaList(Types.TAG.getType(), WebConst.MAX_POSTS);
        request.setAttribute("categories", categories);
        request.setAttribute("tags", tags);
        return "admin/category";
    }

    @PostMapping(value = "save")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo saveCategory(@RequestParam String cname, @RequestParam Integer mid) {
        try {
            metaService.saveMeta(Types.CATEGORY.getType(), cname, mid);
        } catch (Exception e) {
            String msg = "分类保存失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam int mid) {
        try {
            metaService.delete(mid);
        } catch (Exception e) {
            String msg = "删除失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }
}
