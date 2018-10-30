/*
 * Solo - A small and beautiful blogging system written in Java.
 * Copyright (c) 2010-2018, b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.solo.processor.wechat;


import org.apache.commons.codec.digest.DigestUtils;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.TextHTMLRenderer;
import org.b3log.solo.service.wechat.WechatOptionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.util.Arrays;


@RequestProcessor
public class WxMpProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(WxMpProcessor.class);

    @Inject
    private WechatOptionService wechatOptionService;

    /**
     * 微信令牌认证 服务器地址(URL)
     * @param context
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestProcessing(value = "/wechat/init", method = HTTPRequestMethod.GET)
    public void wxGet(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        final TextHTMLRenderer renderer = new TextHTMLRenderer();
        final String signature =  request.getParameter("signature");
        final String timestamp =  request.getParameter("timestamp");
        final String nonce =  request.getParameter("nonce");
        final String echostr = request.getParameter("echostr");

        final String token = wechatOptionService.getWechatConfig().app_token;

        if(null == signature || null == timestamp || null == nonce || null == echostr || null == token)
        {
            renderer.setContent("false");
            context.setRenderer(renderer);
            return;
        }
        String[] array = {token,timestamp,nonce};
        Arrays.sort(array);
        StringBuffer sb = new StringBuffer();

        for(String s : array){
            sb.append(s);
        }
        final String sha_string = getSha1(sb.toString());

        if(signature.equals(sha_string)) {
            renderer.setContent(echostr);
        }
        else{
            renderer.setContent("false");
        }

        context.setRenderer(renderer);

    }

    /**
     * 生成sha1值
     * @param str
     * @return
     */
    private static String getSha1(String str){

        if(str==null||str.length()==0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            String out_str = new String(buf);
            return out_str;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

    }

}
