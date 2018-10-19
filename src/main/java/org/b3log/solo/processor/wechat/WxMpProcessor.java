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


import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.TextHTMLRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequestProcessor
public class WxMpProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(WxMpProcessor.class);

    @RequestProcessing(value = "/wechat/get", method = HTTPRequestMethod.GET)
    public void wxGet(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        final TextHTMLRenderer renderer = new TextHTMLRenderer();

        renderer.setContent("wechat测试数据");

        context.setRenderer(renderer);

    }
}
