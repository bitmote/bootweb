package com.luangeng.bootweb.controller.helper;

import com.luangeng.bootweb.exception.TipException;
import org.slf4j.Logger;
import com.luangeng.bootweb.exception.TipException;
import com.luangeng.bootweb.modal.bo.RestResponseBo;

/**
 * @author janti
 */
public class ExceptionHelper {
    /**
     * 统一处理异常
     *
     * @param logger
     * @param msg
     * @param e
     * @return
     */
    public static RestResponseBo handlerException(Logger logger, String msg, Exception e) {
        if (e instanceof TipException) {
            msg = e.getMessage();
        } else {
            logger.error(msg, e);
        }
        return RestResponseBo.fail(msg);
    }
}
