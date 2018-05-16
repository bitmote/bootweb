package com.luangeng.bootweb.controller.admin;

import com.github.pagehelper.PageInfo;
import com.luangeng.bootweb.constant.WebConst;
import com.luangeng.bootweb.controller.AbstractController;
import com.luangeng.bootweb.controller.helper.ExceptionHelper;
import com.luangeng.bootweb.dto.LogActions;
import com.luangeng.bootweb.dto.Types;
import com.luangeng.bootweb.exception.TipException;
import com.luangeng.bootweb.service.IContentService;
import com.luangeng.bootweb.service.ILogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.luangeng.bootweb.constant.WebConst;
import com.luangeng.bootweb.controller.AbstractController;
import com.luangeng.bootweb.controller.helper.ExceptionHelper;
import com.luangeng.bootweb.dto.LogActions;
import com.luangeng.bootweb.dto.Types;
import com.luangeng.bootweb.exception.TipException;
import com.luangeng.bootweb.modal.bo.RestResponseBo;
import com.luangeng.bootweb.modal.vo.ContentVo;
import com.luangeng.bootweb.modal.vo.ContentVoExample;
import com.luangeng.bootweb.modal.vo.UserVo;
import com.luangeng.bootweb.service.IContentService;
import com.luangeng.bootweb.service.ILogService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 页面管理
 *
 * @author tangj
 * @date 2018/1/27 14:43
 */
@Controller
@RequestMapping("admin/page")
public class PageController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(PageController.class);

    @Resource
    private IContentService contentService;

    @Resource
    private ILogService logService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("created desc");
        contentVoExample.createCriteria().andTypeEqualTo(Types.PAGE.getType());
        PageInfo<ContentVo> contentVoPageInfo = contentService.getArticlesWithpage(contentVoExample, 1, WebConst.MAX_POSTS);
        request.setAttribute("articles", contentVoPageInfo);
        return "admin/page_list";
    }

    @GetMapping(value = "new")
    public String newPage(HttpServletRequest request) {
        return "admin/page_edit";
    }

    @GetMapping(value = "/{cid}")
    public String editPage(@PathVariable String cid, HttpServletRequest request) {
        ContentVo contents = contentService.getContents(cid);
        request.setAttribute("contents", contents);
        return "admin/page_edit";
    }

    @PostMapping(value = "publish")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo publishPage(@RequestParam String title, @RequestParam String content,
                                      @RequestParam String status, @RequestParam String slug,
                                      @RequestParam(required = false) Integer allowComment, @RequestParam(required = false) Integer allowPing, HttpServletRequest request) {

        UserVo users = this.user(request);
        ContentVo contents = new ContentVo();
        contents.setTitle(title);
        contents.setContent(content);
        contents.setStatus(status);
        contents.setSlug(slug);
        contents.setType(Types.PAGE.getType());
        if (null != allowComment) {
            contents.setAllowComment(allowComment == 1);
        }
        if (null != allowPing) {
            contents.setAllowPing(allowPing == 1);
        }
        contents.setAuthorId(users.getUid());

        try {
            contentService.publish(contents);
        } catch (Exception e) {
            String msg = "页面发布失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }

    @PostMapping(value = "modify")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo modifyArticle(@RequestParam Integer cid, @RequestParam String title,
                                        @RequestParam String content,
                                        @RequestParam String status, @RequestParam String slug,
                                        @RequestParam(required = false) Integer allowComment, @RequestParam(required = false) Integer allowPing, HttpServletRequest request) {

        UserVo users = this.user(request);
        ContentVo contents = new ContentVo();
        contents.setCid(cid);
        contents.setTitle(title);
        contents.setContent(content);
        contents.setStatus(status);
        contents.setSlug(slug);
        contents.setType(Types.PAGE.getType());
        if (null != allowComment) {
            contents.setAllowComment(allowComment == 1);
        }
        if (null != allowPing) {
            contents.setAllowPing(allowPing == 1);
        }
        contents.setAuthorId(users.getUid());
        try {
            contentService.updateArticle(contents);
        } catch (Exception e) {
            String msg = "页面编辑失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam int cid, HttpServletRequest request) {
        try {
            contentService.deleteByCid(cid);
            logService.insertLog(LogActions.DEL_PAGE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "页面删除失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }


}
