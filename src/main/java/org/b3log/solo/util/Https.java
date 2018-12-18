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

import java.net.HttpURLConnection;
import java.net.URL;

public class Https {

    /**
     * 给定图片URL地址，检查是否可以访问
     * @param image_url
     * @return
     */
    public static boolean checkImageUrlExist(String image_url){
        try{
            URL url = new URL(image_url.trim());
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                return true;
            }
            return false;
        }catch (Exception ex){
            return false;
        }
    }
}
