package com.luangeng.bootweb.controller.admin;

import com.github.pagehelper.PageInfo;
import com.luangeng.bootweb.controller.AbstractController;
import com.luangeng.bootweb.dto.LogActions;
import com.luangeng.bootweb.dto.Types;
import com.luangeng.bootweb.exception.TipException;
import com.luangeng.bootweb.modal.bo.RestResponseBo;
import com.luangeng.bootweb.modal.vo.ContentVo;
import com.luangeng.bootweb.modal.vo.ContentVoExample;
import com.luangeng.bootweb.modal.vo.MetaVo;
import com.luangeng.bootweb.modal.vo.UserVo;
import com.luangeng.bootweb.service.IContentService;
import com.luangeng.bootweb.service.ILogService;
import com.luangeng.bootweb.service.IMetaService;
import com.luangeng.bootweb.util.Commons;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章管理
 *
 * @author tangj
 * @date 2018/1/24 21:01
 */
@Controller
@RequestMapping("/admin/article")
@Transactional(rollbackFor = TipException.class)
public class ArticleController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private IContentService contentService;

    @Autowired
    private IMetaService metaService;

    @Autowired
    private ILogService logService;

    /**
     * 文章列表页面
     *
     * @param page
     * @param limit
     * @param request
     * @return
     */
    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit,
                        HttpServletRequest request) {
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("created desc");
        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType());
        PageInfo<ContentVo> contentsPaginator = contentService.getArticlesWithpage(contentVoExample, page, limit);
        request.setAttribute("articles", contentsPaginator);
        return "admin/article_list";
    }

    /**
     * 文章发表页面
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/publish")
    public String newArticle(HttpServletRequest request) {
        List<MetaVo> categories = metaService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories", categories);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType()));
        return "admin/article_edit";
    }

    /**
     * 文章编辑页面
     *
     * @param cid
     * @param request
     * @return
     */
    @GetMapping(value = "/{cid}")
    public String editArticle(@PathVariable String cid, HttpServletRequest request) {
        ContentVo contents = contentService.getContents(cid);
        request.setAttribute("contents", contents);
        List<MetaVo> categories = metaService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories", categories);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType()));
        request.setAttribute("active", "article");
        return "admin/article_edit";
    }

    /**
     * 文章发表 post
     *
     * @param contents
     * @param request
     * @return
     */
    @PostMapping(value = "/publish")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo publishArticle(ContentVo contents, HttpServletRequest request) {
        UserVo users = this.user(request);
        contents.setAuthorId(users.getUid());
        contents.setType(Types.ARTICLE.getType());
        if (StringUtils.isBlank(contents.getCategories())) {
            contents.setCategories("默认分类");
        }
        try {
            contentService.publish(contents);
        } catch (Exception e) {
            String msg = "文章发布失败";
            return handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }

    /**
     * 文章更新 post
     *
     * @param contents
     * @param request
     * @return
     */
    @PostMapping(value = "/modify")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo modifyArticle(ContentVo contents, HttpServletRequest request) {
        UserVo users = this.user(request);
        contents.setAuthorId(users.getUid());
        contents.setType(Types.ARTICLE.getType());
        try {
            contentService.updateArticle(contents);
        } catch (Exception e) {
            String msg = "文章编辑失败";
            return handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }

    /**
     * 删除文章 post
     *
     * @param cid
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam int cid, HttpServletRequest request) {
        try {
            contentService.deleteByCid(cid);
            logService.insertLog(LogActions.DEL_ARTICLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "文章删除失败";
            return handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }
}
