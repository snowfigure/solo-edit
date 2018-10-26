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
package org.b3log.solo.service;

import org.b3log.latke.Latkes;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.Transaction;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.Locales;
import org.b3log.latke.util.Stopwatchs;
import org.b3log.solo.model.Option;
import org.b3log.solo.model.Skin;
import org.b3log.solo.repository.OptionRepository;
import org.b3log.solo.util.Skins;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static org.b3log.solo.model.Skin.*;
import static org.b3log.solo.util.Skins.getSkinDirNames;

/**
 * Preference management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.3.2.16, Sep 26, 2018
 * @since 0.4.0
 */
@Service
public class PreferenceMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PreferenceMgmtService.class);

    /**
     * Preference query service.
     */
    @Inject
    private PreferenceQueryService preferenceQueryService;

    /**
     * Option repository.
     */
    @Inject
    private OptionRepository optionRepository;

    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Loads skins for the specified preference and initializes templates loading.
     * <p>
     * If the skins directory has been changed, persists the change into preference.
     * </p>
     *
     * @param preference the specified preference
     * @throws Exception exception
     */
    public void loadSkins(final JSONObject preference) throws Exception {
        Stopwatchs.start("Load Skins");

        LOGGER.debug("Loading skins....");

        final Set<String> skinDirNames = getSkinDirNames();

        LOGGER.log(Level.DEBUG, "Loaded skins[dirNames={0}]", skinDirNames);
        final JSONArray skinArray = new JSONArray();

        for (final String dirName : skinDirNames) {
            final JSONObject skin = new JSONObject();
            final String name = Latkes.getSkinName(dirName);
            if (null == name) {
                LOGGER.log(Level.WARN, "The directory [{0}] does not contain any skin, ignored it", dirName);

                continue;
            }

            skin.put(SKIN_NAME, name);
            skin.put(SKIN_DIR_NAME, dirName);

            skinArray.put(skin);
        }

        final String currentSkinDirName = preference.optString(SKIN_DIR_NAME);
        final String skinName = preference.optString(SKIN_NAME);

        LOGGER.log(Level.DEBUG, "Current skin[name={0}]", skinName);

        if (!skinDirNames.contains(currentSkinDirName)) {
            LOGGER.log(Level.WARN, "Configured skin [dirName={0}] can not find, try to use " + "default skin [dirName="
                    + Option.DefaultPreference.DEFAULT_SKIN_DIR_NAME + "] instead.", currentSkinDirName);
            if (!skinDirNames.contains(Option.DefaultPreference.DEFAULT_SKIN_DIR_NAME)) {
                LOGGER.log(Level.ERROR, "Can not find default skin [dirName=" + Option.DefaultPreference.DEFAULT_SKIN_DIR_NAME
                        + "], please redeploy your Solo and make sure contains the default skin. If you are using git, try to re-pull with 'git pull --recurse-submodules'");
                System.exit(-1);
            }

            preference.put(SKIN_DIR_NAME, Option.DefaultPreference.DEFAULT_SKIN_DIR_NAME);
            preference.put(SKIN_NAME, Latkes.getSkinName(Option.DefaultPreference.DEFAULT_SKIN_DIR_NAME));

            updatePreference(preference);
        }

        final String skinsString = skinArray.toString();
        if (!skinsString.equals(preference.getString(SKINS))) {
            LOGGER.debug("The skins directory has been changed, persists the change into preference");
            preference.put(SKINS, skinsString);
            updatePreference(preference);
        }

        LOGGER.debug("Loaded skins....");

        Stopwatchs.end();
    }

    /**
     * Updates the reply notification template with the specified reply notification template.
     *
     * @param replyNotificationTemplate the specified reply notification template
     * @throws ServiceException service exception
     */
    public void updateReplyNotificationTemplate(final JSONObject replyNotificationTemplate) throws ServiceException {
        final Transaction transaction = optionRepository.beginTransaction();

        try {
            final JSONObject bodyOpt = optionRepository.get(Option.ID_C_REPLY_NOTI_TPL_BODY);
            bodyOpt.put(Option.OPTION_VALUE, replyNotificationTemplate.optString("body"));
            optionRepository.update(Option.ID_C_REPLY_NOTI_TPL_BODY, bodyOpt);

            final JSONObject subjectOpt = optionRepository.get(Option.ID_C_REPLY_NOTI_TPL_SUBJECT);
            subjectOpt.put(Option.OPTION_VALUE, replyNotificationTemplate.optString("subject"));
            optionRepository.update(Option.ID_C_REPLY_NOTI_TPL_SUBJECT, subjectOpt);

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.ERROR, "Updates reply notification failed", e);
            throw new ServiceException(e);
        }
    }

    /**
     * Updates the preference with the specified preference.
     *
     * @param preference the specified preference
     * @throws ServiceException service exception
     */
    public void updatePreference(final JSONObject preference) throws ServiceException {
        final Iterator<String> keys = preference.keys();
        while (keys.hasNext()) {
            final String key = keys.next();

            if (preference.isNull(key)) {
                throw new ServiceException("A value is null of preference[key=" + key + "]");
            }
        }

        final Transaction transaction = optionRepository.beginTransaction();

        try {
            final String skinDirName = preference.getString(Skin.SKIN_DIR_NAME);
            final String skinName = Latkes.getSkinName(skinDirName);

            preference.put(Skin.SKIN_NAME, skinName);
            final Set<String> skinDirNames = Skins.getSkinDirNames();
            final JSONArray skinArray = new JSONArray();

            for (final String dirName : skinDirNames) {
                final JSONObject skin = new JSONObject();
                skinArray.put(skin);

                final String name = Latkes.getSkinName(dirName);
                skin.put(Skin.SKIN_NAME, name);
                skin.put(Skin.SKIN_DIR_NAME, dirName);
            }

            preference.put(Skin.SKINS, skinArray.toString());

            preference.put(Option.ID_C_SIGNS, preference.get(Option.ID_C_SIGNS).toString());

            final JSONObject oldPreference = preferenceQueryService.getPreference();
            final String adminEmail = oldPreference.getString(Option.ID_C_ADMIN_EMAIL);
            preference.put(Option.ID_C_ADMIN_EMAIL, adminEmail);

            final String version = oldPreference.optString(Option.ID_C_VERSION);
            preference.put(Option.ID_C_VERSION, version);

            final String localeString = preference.getString(Option.ID_C_LOCALE_STRING);
            LOGGER.log(Level.DEBUG, "Current locale[string={0}]", localeString);
            Latkes.setLocale(new Locale(Locales.getLanguage(localeString), Locales.getCountry(localeString)));



            final JSONObject adminEmailOpt = optionRepository.get(Option.ID_C_ADMIN_EMAIL);
            adminEmailOpt.put(Option.OPTION_VALUE, adminEmail);
            optionRepository.update(Option.ID_C_ADMIN_EMAIL, adminEmailOpt);

            final JSONObject editorTypeOpt = optionRepository.get(Option.ID_C_EDITOR_TYPE);
            // https://github.com/b3log/solo/issues/12285
            // editorTypeOpt.put(Option.OPTION_VALUE, preference.optString(Option.ID_C_EDITOR_TYPE));
            editorTypeOpt.put(Option.OPTION_VALUE, Option.DefaultPreference.DEFAULT_EDITOR_TYPE);
            optionRepository.update(Option.ID_C_EDITOR_TYPE, editorTypeOpt);


            final List<String> option_list = new ArrayList<>();
            option_list.add(Option.ID_C_ALLOW_VISIT_DRAFT_VIA_PERMALINK);
            option_list.add(Option.ID_C_ALLOW_REGISTER);

            option_list.add(Option.ID_C_ARTICLE_LIST_DISPLAY_COUNT);
            option_list.add(Option.ID_C_ARTICLE_LIST_PAGINATION_WINDOW_SIZE);
            option_list.add(Option.ID_C_ARTICLE_LIST_STYLE);

            option_list.add(Option.ID_C_BLOG_SUBTITLE);
            option_list.add(Option.ID_C_BLOG_TITLE);

            option_list.add(Option.ID_C_COMMENTABLE);
            option_list.add(Option.ID_C_ENABLE_ARTICLE_UPDATE_HINT);
            option_list.add(Option.ID_C_EXTERNAL_RELEVANT_ARTICLES_DISPLAY_CNT);
            option_list.add(Option.ID_C_FEED_OUTPUT_CNT);
            option_list.add(Option.ID_C_FEED_OUTPUT_MODE);
            option_list.add(Option.ID_C_FOOTER_CONTENT);

            option_list.add(Option.ID_C_HTML_HEAD);
            option_list.add(Option.ID_C_CDN_JQUERY_JS);
            option_list.add(Option.ID_C_CDN_ICONFONT);
            option_list.add(Option.ID_C_BANNER_IMAGE_URL);

            option_list.add(Option.ID_C_BAIDU_HM_CODE);
            option_list.add(Option.ID_C_BAIDU_HM_ENABLE);
            option_list.add(Option.ID_C_BAIDU_PUSH_ENABLE);

            option_list.add(Option.ID_C_KEY_OF_SOLO);
            option_list.add(Option.ID_C_LOCALE_STRING);
            option_list.add(Option.ID_C_META_DESCRIPTION);
            option_list.add(Option.ID_C_META_KEYWORDS);

            option_list.add(Option.ID_C_MOST_COMMENT_ARTICLE_DISPLAY_CNT);
            option_list.add(Option.ID_C_MOST_USED_TAG_DISPLAY_CNT);
            option_list.add(Option.ID_C_MOST_VIEW_ARTICLE_DISPLAY_CNT);

            option_list.add(Option.ID_C_NOTICE_BOARD);
            option_list.add(Option.ID_C_RANDOM_ARTICLES_DISPLAY_CNT);
            option_list.add(Option.ID_C_RECENT_ARTICLE_DISPLAY_CNT);
            option_list.add(Option.ID_C_RECENT_COMMENT_DISPLAY_CNT);
            option_list.add(Option.ID_C_RELEVANT_ARTICLES_DISPLAY_CNT);

            option_list.add(Option.ID_C_SIGNS);
            option_list.add(Option.ID_C_SKIN_DIR_NAME);
            option_list.add(Option.ID_C_SKIN_NAME);
            option_list.add(Option.ID_C_SKINS);

            option_list.add(Option.ID_C_TIME_ZONE_ID);
            option_list.add(Option.ID_C_VERSION);


            for (String key: option_list) {

                final JSONObject optionValueOpt = optionRepository.get(key);
                optionValueOpt.put(Option.OPTION_VALUE, preference.optString(key));
                optionRepository.update(key, optionValueOpt);
            }
            

            transaction.commit();
        } catch (final Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            LOGGER.log(Level.ERROR, "Updates preference failed", e);
            throw new ServiceException(langPropsService.get("updateFailLabel"));
        }

        LOGGER.log(Level.DEBUG, "Updates preference successfully");
    }

    /**
     * Sets the language service with the specified language service.
     *
     * @param langPropsService the specified language service
     */
    public void setLangPropsService(final LangPropsService langPropsService) {
        this.langPropsService = langPropsService;
    }
}
