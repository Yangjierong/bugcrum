/*
 * $Id: DefaultExceptionHandler.java 46 2014-12-08 10:27:54Z jierong $
 * 
 * Copyright (c) 2001-2008 Accentrix Company Limited. All Rights Reserved.
 * 
 * Accentrix Company Limited. ("Accentrix") retains copyright on all text, source
 * and binary code contained in this software and documentation. Accentrix grants
 * Licensee a limited license to use this software, provided that this copyright
 * notice and license appear on all copies of the software. The software source
 * code is provided for reference purposes only and may not be copied, modified 
 * or distributed.
 * 
 * THIS SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS," WITHOUT ANY WARRANTY OF
 * ANY KIND UNLESS A SEPARATE WARRANTIES IS PURCHASED FROM ACCENTRIX AND REMAINS
 * VALID.  ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. ACCENTRIX SHALL NOT BE LIABLE
 * FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING OR MODIFYING THE
 * SOFTWARE OR ITS DERIVATIVES.
 * 
 * IN NO EVENT WILL ACCENTRIX BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE
 * USE OF OR INABILITY TO USE SOFTWARE, EVEN IF ACCENTRIX HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 */
package com.accentrix.bugcrum.exception;

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.accentrix.nttca.dcms.common.exception.WsRuntimeException;
import com.accentrix.nttca.dcms.common.exception.handler.ExceptionHandler;
import com.accentrix.nttca.dcms.common.util.MessageUtil;
import com.accentrix.nttca.dcms.common.util.UIUtil;

public class DefaultExceptionHandler implements ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public void handle(Throwable t, String[] update, String message, String messageKeyPrefix) {
        log.info("handling Throwable: {}, update: {}", t.getMessage(), update);

        String exClassName = t.getClass().getName();

        if (t instanceof WsRuntimeException) {
            log.debug("it is WsRuntimeException");
            WsRuntimeException wsEx = (WsRuntimeException) t;
            exClassName = wsEx.getCauseClassName();
        }

        log.debug("exClassName: {}", exClassName);
        log.debug("exception msg: {}", t.getMessage());

        if (StringUtils.isNotBlank(message)) {
            UIUtil.addErrorMessage(null, message);
        } else {
            String detail = getDetail(t, exClassName);

            String localizedMessage = getLocalizedMessage(exClassName, messageKeyPrefix, detail);

            UIUtil.addErrorMessage(null, localizedMessage);
        }

        for (String up : update) {
            RequestContext.getCurrentInstance().update(up);
        }
    }

    private String getDetail(Throwable t, String exClassName) {
        return t.getMessage();
    }

    private String getLocalizedMessage(String exClassName, String messageKeyPrefix, String detail) {
        String msg = MessageUtil.getMessage(messageKeyPrefix + exClassName, exClassName, detail);

        if (!msg.equals(messageKeyPrefix + exClassName)) {
            return msg;
        }

        if (StringUtils.isNotBlank(messageKeyPrefix)) {
            msg = MessageUtil.getMessage(exClassName, exClassName, detail);

            if (!msg.equals(exClassName)) {
                return msg;
            }
        }

        msg = MessageUtil.getMessage("default_exception_msg", exClassName, detail);

        if (msg.equals("default_exception_msg")) {
            msg = detail;
        }

        return msg;
    }
}
