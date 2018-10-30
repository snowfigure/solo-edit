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
package org.b3log.solo.processor.console;

import freemarker.template.Template;
import org.apache.commons.lang.time.DateFormatUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.renderer.AbstractFreeMarkerRenderer;
import org.b3log.solo.util.Skins;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.util.Map;

/**
 * <a href="http://freemarker.org">FreeMarker</a> HTTP response renderer for administrator console and initialization rendering.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.1.5, Sep 26, 2018
 * @since 0.4.1
 */
public final class ConsoleRenderer extends AbstractFreeMarkerRenderer {

    @Override
    protected Template getTemplate() {
        return Skins.getTemplate(getTemplateName());
    }

    @Override
    protected void beforeRender(final HTTPRequestContext context) {
    }

    @Override
    protected void afterRender(final HTTPRequestContext context) {
    }

    /**
     * Processes the specified FreeMarker template with the specified request, data model.
     *
     * @param request   the specified request
     * @param dataModel the specified data model
     * @param template  the specified FreeMarker template
     * @return generated HTML
     * @throws Exception exception
     */
    @Override
    protected String genHTML(final HttpServletRequest request, final Map<String, Object> dataModel, final Template template)
            throws Exception {
        final StringWriter stringWriter = new StringWriter();
        template.setOutputEncoding("UTF-8");
        template.process(dataModel, stringWriter);

        final StringBuilder pageContentBuilder = new StringBuilder(stringWriter.toString());
        final long endimeMillis = System.currentTimeMillis();
        final String dateString = DateFormatUtils.format(endimeMillis, "yyyy/MM/dd HH:mm:ss");
        final long startTimeMillis = (Long) request.getAttribute(Keys.HttpRequest.START_TIME_MILLIS);
        final String msg = String.format("\n<!-- Generated in %1$dms, %2$s -->",
                endimeMillis - startTimeMillis, dateString);
        pageContentBuilder.append(msg);

        return pageContentBuilder.toString();
    }
}
