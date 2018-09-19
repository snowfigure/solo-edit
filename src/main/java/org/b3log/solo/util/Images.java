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
package org.b3log.solo.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Image utilities.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Feb 14, 2018
 * @since 2.7.0
 */
public final class Images {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(Images.class);

    public static String IMG_PREFIX_URL = "https://img.791211.com/bind/";
    public static String IMG_DEFAULT_TIME = "20171104";
    public static String IMG_QINIU_PARA = "?imageView2/1/w/960/h/520/interlace/1/q/100";

    /**
     * Gets an image URL randomly. Sees https://github.com/b3log/bing for more details.
     *
     * @return an image URL
     */
    public static final String randImage() {
        try {
            final long min = DateUtils.parseDate(IMG_DEFAULT_TIME, new String[]{"yyyyMMdd"}).getTime();
            final long max = System.currentTimeMillis();
            final long delta = max - min;
            final long time = ThreadLocalRandom.current().nextLong(0, delta) + min;

            //https://img.hacpai.com/bing/
            return IMG_PREFIX_URL + DateFormatUtils.format(time, "yyyyMMdd") + ".jpg";
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Generates random image URL failed", e);

            return IMG_PREFIX_URL + IMG_DEFAULT_TIME + ".jpg";
        }
    }

    /**
     * Gets image URLs randomly.
     *
     * @param n the specified size
     * @return image URLs
     */
    public static List<String> randomImages(final int n) {
        final List<String> ret = new ArrayList<>();

        int i = 0;
        while (true) {
            if (i >= n * 5) {
                break;
            }

            final String url = randImage();
            if (!ret.contains(url)) {
                ret.add(url);
            }

            if (ret.size() >= n) {
                return ret;
            }

            i++;
        }

        return ret;
    }

    /**
     * Private constructor.
     */
    private Images() {
    }
}
