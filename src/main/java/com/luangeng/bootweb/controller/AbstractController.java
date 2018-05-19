package com.luangeng.bootweb.controller;

import com.luangeng.bootweb.exception.TipException;
import com.luangeng.bootweb.modal.bo.RestResponseBo;
import com.luangeng.bootweb.modal.vo.UserVo;
import com.luangeng.bootweb.util.MapCache;
import com.luangeng.bootweb.util.MyUtils;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * 抽象类controller
 * 用于统一渲染页面url，页面名称，获取session中的用户
 * @author tangj
 * @date 2018/1/21 11:25
 */
public abstract class AbstractController {
    public static String THEME = "themes/simple";

    protected MapCache cache = MapCache.single();

    /**
     * 主页的页面主题
     *
     * @param viewName
     * @return
     */
    public String render(String viewName) {
        return THEME + "/" + viewName;
    }

    public AbstractController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    public AbstractController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }

    public UserVo user(HttpServletRequest request){
        return MyUtils.getLoginUser(request);
    }

    public Integer getUid(HttpServletRequest request){
        return this.user(request).getUid();
    }

    public String render_404() {
        return "comm/error_404";
    }

    public static RestResponseBo handlerException(Logger logger, String msg, Exception e) {
        if (e instanceof TipException) {
            msg = e.getMessage();
        } else {
            logger.error(msg, e);
        }
        return RestResponseBo.fail(msg);
    }
}
