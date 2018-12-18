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
package org.b3log.solo.service.wechat;

import org.b3log.latke.ioc.Inject;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.solo.model.Option;
import org.b3log.solo.model.wechat.WechatConfig;
import org.b3log.solo.service.OptionQueryService;
import org.b3log.solo.service.PreferenceQueryService;
import org.json.JSONObject;

@Service
public class WechatOptionService {
    /**
     * Preference query service.
     */
    @Inject
    private PreferenceQueryService preferenceQueryService;

    /**
     * Optiona query service.
     */
    @Inject
    private OptionQueryService optionQueryService;

    public WechatConfig getWechatConfig() throws ServiceException {

        try{
            final JSONObject wechat_opt = optionQueryService.getOptions(Option.CATEGORY_C_WECHAT);

            final WechatConfig config = new WechatConfig();
            config.app_id = wechat_opt.optString(Option.ID_C_WECHAT_APP_ID);
            config.app_secret = wechat_opt.optString(Option.ID_C_WECHAT_APP_SECERT);
            config.app_token = wechat_opt.optString(Option.ID_C_WECHAT_TOKEN);
            //config.encode_mode= opt.optString(Option.ID_C_WECHAT_APP_ENCODING_AES_KEY );
            config.encoding_ase_key = wechat_opt.optString(Option.ID_C_WECHAT_APP_ENCODING_AES_KEY);

            return config;
        } catch (final Exception e){
            return  null;
        }

    }
}
