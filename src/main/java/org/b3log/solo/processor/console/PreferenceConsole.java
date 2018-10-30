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

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.JSONRenderer;
import org.b3log.solo.model.Option;
import org.b3log.solo.model.Sign;
import org.b3log.solo.model.Skin;
import org.b3log.solo.service.OptionMgmtService;
import org.b3log.solo.service.OptionQueryService;
import org.b3log.solo.service.PreferenceMgmtService;
import org.b3log.solo.service.PreferenceQueryService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Preference console request processing.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.2.0.14, Oct 17, 2018
 * @since 0.4.0
 */
@RequestProcessor
@Before(adviceClass = ConsoleAdminAuthAdvice.class)
public class PreferenceConsole {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PreferenceConsole.class);

    /**
     * Preference URI prefix.
     */
    private static final String PREFERENCE_URI_PREFIX = "/console/preference/";

    /**
     * Preference query service.
     */
    @Inject
    private PreferenceQueryService preferenceQueryService;

    /**
     * Preference management service.
     */
    @Inject
    private PreferenceMgmtService preferenceMgmtService;

    /**
     * Option management service.
     */
    @Inject
    private OptionMgmtService optionMgmtService;

    /**
     * Option query service.
     */
    @Inject
    private OptionQueryService optionQueryService;

    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Gets reply template.
     * <p>
     * Renders the response with a json object, for example,
     * <pre>
     * {
     *     "sc": boolean,
     *     "replyNotificationTemplate": {
     *         "subject": "",
     *         "body": ""
     *     }
     * }
     * </pre>
     * </p>
     *
     * @param request  the specified http servlet request
     * @param response the specified http servlet response
     * @param context  the specified http request context
     */
    @RequestProcessing(value = "/console/reply/notification/template", method = HTTPRequestMethod.GET)
    public void getReplyNotificationTemplate(final HttpServletRequest request,
                                             final HttpServletResponse response,
                                             final HTTPRequestContext context) {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);

        try {
            final JSONObject replyNotificationTemplate = preferenceQueryService.getReplyNotificationTemplate();

            final JSONObject ret = new JSONObject();
            renderer.setJSONObject(ret);
            ret.put("replyNotificationTemplate", replyNotificationTemplate);
            ret.put(Keys.STATUS_CODE, true);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);

            final JSONObject jsonObject = new JSONObject().put(Keys.STATUS_CODE, false);
            renderer.setJSONObject(jsonObject);
            jsonObject.put(Keys.MSG, langPropsService.get("getFailLabel"));
        }
    }

    /**
     * Updates reply template.
     *
     * @param request           the specified http servlet request
     * @param response          the specified http servlet response
     * @param context           the specified http request context
     * @param requestJSONObject the specified request json object, for example,
     *                          "replyNotificationTemplate": {
     *                          "subject": "",
     *                          "body": ""
     *                          }
     */
    @RequestProcessing(value = "/console/reply/notification/template", method = HTTPRequestMethod.PUT)
    public void updateReplyNotificationTemplate(final HttpServletRequest request,
                                                final HttpServletResponse response,
                                                final HTTPRequestContext context,
                                                final JSONObject requestJSONObject) {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);

        try {
            final JSONObject replyNotificationTemplate = requestJSONObject.getJSONObject("replyNotificationTemplate");
            preferenceMgmtService.updateReplyNotificationTemplate(replyNotificationTemplate);

            final JSONObject ret = new JSONObject();
            ret.put(Keys.STATUS_CODE, true);
            ret.put(Keys.MSG, langPropsService.get("updateSuccLabel"));
            renderer.setJSONObject(ret);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);

            final JSONObject jsonObject = new JSONObject().put(Keys.STATUS_CODE, false);
            renderer.setJSONObject(jsonObject);
            jsonObject.put(Keys.MSG, langPropsService.get("updateFailLabel"));
        }
    }

    /**
     * Gets signs.
     * <p>
     * Renders the response with a json object, for example,
     * <pre>
     * {
     *     "sc": boolean,
     *     "signs": [{
     *         "oId": "",
     *         "signHTML": ""
     *      }, ...]
     * }
     * </pre>
     * </p>
     *
     * @param request  the specified http servlet request
     * @param response the specified http servlet response
     * @param context  the specified http request context
     */
    @RequestProcessing(value = "/console/signs/", method = HTTPRequestMethod.GET)
    public void getSigns(final HttpServletRequest request, final HttpServletResponse response, final HTTPRequestContext context) {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);

        try {
            final JSONObject preference = preferenceQueryService.getPreference();
            final JSONArray signs = new JSONArray();
            final JSONArray allSigns
                    = // includes the empty sign(id=0)
                    new JSONArray(preference.getString(Option.ID_C_SIGNS));

            for (int i = 1; i < allSigns.length(); i++) { // excludes the empty sign
                signs.put(allSigns.getJSONObject(i));
            }

            final JSONObject ret = new JSONObject();
            renderer.setJSONObject(ret);
            ret.put(Sign.SIGNS, signs);
            ret.put(Keys.STATUS_CODE, true);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);

            final JSONObject jsonObject = new JSONObject().put(Keys.STATUS_CODE, false);
            renderer.setJSONObject(jsonObject);
            jsonObject.put(Keys.MSG, langPropsService.get("getFailLabel"));
        }
    }

    /**
     * Gets preference.
     * <p>
     * Renders the response with a json object, for example,
     * <pre>
     * {
     *     "sc": boolean,
     *     "preference": {
     *         "mostViewArticleDisplayCount": int,
     *         "recentCommentDisplayCount": int,
     *         "mostUsedTagDisplayCount": int,
     *         "articleListDisplayCount": int,
     *         "articleListPaginationWindowSize": int,
     *         "mostCommentArticleDisplayCount": int,
     *         "externalRelevantArticlesDisplayCount": int,
     *         "relevantArticlesDisplayCount": int,
     *         "randomArticlesDisplayCount": int,
     *         "blogTitle": "",
     *         "blogSubtitle": "",
     *         "localeString": "",
     *         "timeZoneId": "",
     *         "skinName": "",
     *         "skinDirName": "",
     *         "skins": "[{
     *             "skinName": "",
     *             "skinDirName": ""
     *         }, ....]",
     *         "noticeBoard": "",
     *         "footerContent": "",
     *         "htmlHead": "",
     *         "adminEmail": "",
     *         "metaKeywords": "",
     *         "metaDescription": "",
     *         "enableArticleUpdateHint": boolean,
     *         "signs": "[{
     *             "oId": "",
     *             "signHTML": ""
     *         }, ...]",
     *         "allowVisitDraftViaPermalink": boolean,
     *         "allowRegister": boolean,
     *         "version": "",
     *         "articleListStyle": "", // Optional values: "titleOnly"/"titleAndContent"/"titleAndAbstract"
     *         "commentable": boolean,
     *         "feedOutputMode: "" // Optional values: "abstract"/"full"
     *         "feedOutputCnt": int
     *     }
     * }
     * </pre>
     * </p>
     *
     * @param request  the specified http servlet request
     * @param response the specified http servlet response
     * @param context  the specified http request context
     */
    @RequestProcessing(value = PREFERENCE_URI_PREFIX, method = HTTPRequestMethod.GET)
    public void getPreference(final HttpServletRequest request, final HttpServletResponse response, final HTTPRequestContext context) {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);

        try {
            final JSONObject preference = preferenceQueryService.getPreference();
            if (null == preference) {
                renderer.setJSONObject(new JSONObject().put(Keys.STATUS_CODE, false));

                return;
            }

            String footerContent = "";
            final JSONObject opt = optionQueryService.getOptionById(Option.ID_C_FOOTER_CONTENT);
            if (null != opt) {
                footerContent = opt.optString(Option.OPTION_VALUE);
            }
            preference.put(Option.ID_C_FOOTER_CONTENT, footerContent);

            final JSONObject ret = new JSONObject();
            renderer.setJSONObject(ret);
            ret.put(Option.CATEGORY_C_PREFERENCE, preference);
            ret.put(Keys.STATUS_CODE, true);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);

            final JSONObject jsonObject = new JSONObject().put(Keys.STATUS_CODE, false);
            renderer.setJSONObject(jsonObject);
            jsonObject.put(Keys.MSG, langPropsService.get("getFailLabel"));
        }
    }

    /**
     * Updates the preference by the specified request.
     *
     * @param request           the specified http servlet request
     * @param response          the specified http servlet response
     * @param context           the specified http request context
     * @param requestJSONObject the specified reuqest json object, for example,
     *                          "preference": {
     *                          "mostViewArticleDisplayCount": int,
     *                          "recentCommentDisplayCount": int,
     *                          "mostUsedTagDisplayCount": int,
     *                          "articleListDisplayCount": int,
     *                          "articleListPaginationWindowSize": int,
     *                          "mostCommentArticleDisplayCount": int,
     *                          "externalRelevantArticlesDisplayCount": int,
     *                          "relevantArticlesDisplayCount": int,
     *                          "randomArticlesDisplayCount": int,
     *                          "blogTitle": "",
     *                          "blogSubtitle": "",
     *                          "skinDirName": "",
     *                          "localeString": "",
     *                          "timeZoneId": "",
     *                          "noticeBoard": "",
     *                          "footerContent": "",
     *                          "htmlHead": "",
     *                          "metaKeywords": "",
     *                          "metaDescription": "",
     *                          "enableArticleUpdateHint": boolean,
     *                          "signs": [{
     *                          "oId": "",
     *                          "signHTML": ""
     *                          }, ...],
     *                          "allowVisitDraftViaPermalink": boolean,
     *                          "allowRegister": boolean,
     *                          "articleListStyle": "",
     *                          "editorType": "",
     *                          "commentable": boolean,
     *                          "feedOutputMode: "",
     *                          "feedOutputCnt": int
     *                          }
     * @throws Exception exception
     */
    @RequestProcessing(value = PREFERENCE_URI_PREFIX, method = HTTPRequestMethod.PUT)
    public void updatePreference(final HttpServletRequest request, final HttpServletResponse response, final HTTPRequestContext context,
                                 final JSONObject requestJSONObject) throws Exception {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);

        try {
            final JSONObject preference = requestJSONObject.getJSONObject(Option.CATEGORY_C_PREFERENCE);
            final JSONObject ret = new JSONObject();
            renderer.setJSONObject(ret);
            if (isInvalid(preference, ret)) {
                return;
            }

            preferenceMgmtService.updatePreference(preference);

            final Cookie cookie = new Cookie(Skin.SKIN, preference.getString(Skin.SKIN_DIR_NAME));
            cookie.setMaxAge(60 * 60); // 1 hour
            cookie.setPath("/");
            response.addCookie(cookie);

            ret.put(Keys.STATUS_CODE, true);
            ret.put(Keys.MSG, langPropsService.get("updateSuccLabel"));
        } catch (final ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);

            final JSONObject jsonObject = new JSONObject().put(Keys.STATUS_CODE, false);
            renderer.setJSONObject(jsonObject);
            jsonObject.put(Keys.MSG, e.getMessage());
        }
    }

    /**
     * Gets Qiniu preference.
     * <p>
     * Renders the response with a json object, for example,
     * <pre>
     * {
     *     "sc": boolean,
     *     "qiniuAccessKey": "",
     *     "qiniuSecretKey": "",
     *     "qiniuDomain": "",
     *     "qiniuBucket": ""
     * }
     * </pre>
     * </p>
     *
     * @param request  the specified http servlet request
     * @param response the specified http servlet response
     * @param context  the specified http request context
     */
    @RequestProcessing(value = PREFERENCE_URI_PREFIX + "qiniu", method = HTTPRequestMethod.GET)
    public void getQiniuPreference(final HttpServletRequest request, final HttpServletResponse response,
                                   final HTTPRequestContext context) {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);

        try {
            final JSONObject qiniu = optionQueryService.getOptions(Option.CATEGORY_C_QINIU);
            final JSONObject baidu = optionQueryService.getOptions(Option.CATEGORY_C_BAIDU);
            final JSONObject wechat = optionQueryService.getOptions(Option.CATEGORY_C_WECHAT);
            if (null == qiniu || null == baidu || null == wechat) {
                renderer.setJSONObject(new JSONObject().put(Keys.STATUS_CODE, false));
                return;
            }

            final JSONObject ret = new JSONObject();
            renderer.setJSONObject(ret);
            ret.put(Option.CATEGORY_C_QINIU, qiniu);
            ret.put(Option.CATEGORY_C_BAIDU, baidu);
            ret.put(Option.CATEGORY_C_WECHAT, wechat);

            ret.put(Keys.STATUS_CODE, true);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);

            final JSONObject jsonObject = new JSONObject().put(Keys.STATUS_CODE, false);
            renderer.setJSONObject(jsonObject);
            jsonObject.put(Keys.MSG, langPropsService.get("getFailLabel"));
        }
    }

    /**
     * Updates the Qiniu preference by the specified request.
     *
     * @param request           the specified http servlet request
     * @param response          the specified http servlet response
     * @param context           the specified http request context
     * @param requestJSONObject the specified request json object, for example,
     *                          "qiniuAccessKey": "",
     *                          "qiniuSecretKey": "",
     *                          "qiniuDomain": "",
     *                          "qiniuBucket": ""
     */
    @RequestProcessing(value = PREFERENCE_URI_PREFIX + "qiniu", method = HTTPRequestMethod.PUT)
    public void updateQiniu(final HttpServletRequest request, final HttpServletResponse response,
                            final HTTPRequestContext context, final JSONObject requestJSONObject) {
        final JSONRenderer renderer = new JSONRenderer();
        context.setRenderer(renderer);
        final JSONObject ret = new JSONObject();
        renderer.setJSONObject(ret);
        try {

            String domain = requestJSONObject.optString(Option.ID_C_QINIU_DOMAIN).trim();
            domain = StringUtils.lowerCase(domain);
            if (StringUtils.isNotBlank(domain) && !StringUtils.endsWith(domain, "/")) {
                domain += "/";
            }
            if (StringUtils.isNotBlank(domain) && !StringUtils.startsWithAny(domain, new String[]{"http", "https"})) {
                domain = "http://" + domain;
            }

            final List<String> option_list_qiniu = new ArrayList<>();
            option_list_qiniu.add(Option.ID_C_QINIU_ACCESS_KEY);
            option_list_qiniu.add(Option.ID_C_QINIU_SECRET_KEY);
            option_list_qiniu.add(Option.ID_C_QINIU_DOMAIN);
            option_list_qiniu.add(Option.ID_C_QINIU_BUCKET);


            /*百度统计*/
            final List<String> option_list_baidu = new ArrayList<>();
            option_list_baidu.add(Option.ID_C_BAIDU_HM_CODE);
            option_list_baidu.add(Option.ID_C_BAIDU_HM_ENABLE);
            option_list_baidu.add(Option.ID_C_BAIDU_PUSH_ENABLE);


            /*微信公众号*/
            final List<String> option_list_wechat = new ArrayList<>();
            option_list_wechat.add(Option.ID_C_WECHAT_APP_ID);
            option_list_wechat.add(Option.ID_C_WECHAT_APP_SECERT);
            option_list_wechat.add(Option.ID_C_WECHAT_APP_ENCODING_AES_KEY);
            option_list_wechat.add(Option.ID_C_WECHAT_TOKEN);
            option_list_wechat.add(Option.ID_C_WECHAT_MSG_ENCODE_MODE);


            updateCategory(option_list_qiniu, Option.CATEGORY_C_QINIU, requestJSONObject);
            updateCategory(option_list_baidu, Option.CATEGORY_C_BAIDU, requestJSONObject);
            updateCategory(option_list_wechat, Option.CATEGORY_C_WECHAT, requestJSONObject);

            ret.put(Keys.STATUS_CODE, true);
            ret.put(Keys.MSG, langPropsService.get("updateSuccLabel"));
            if (isQiniuTestDomain(domain)) {
                ret.put(Keys.MSG, langPropsService.get("donotUseQiniuTestDoaminLabel"));
            }
        } catch (final ServiceException e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);

            final JSONObject jsonObject = new JSONObject().put(Keys.STATUS_CODE, false);
            renderer.setJSONObject(jsonObject);
            jsonObject.put(Keys.MSG, e.getMessage());
        }
    }

    private void updateCategory(final List<String> option_list, final String category, final JSONObject requestJSONObject) throws  ServiceException {
        try {
            for (String key : option_list) {

                final JSONObject jsonObject = new JSONObject();
                jsonObject.put(Keys.OBJECT_ID, key);
                jsonObject.put(Option.OPTION_CATEGORY, category);
                jsonObject.put(Option.OPTION_VALUE, requestJSONObject.optString(key));

                optionMgmtService.addOrUpdateOption(jsonObject);
            }
        }catch (final ServiceException e) {
            throw e;
        }
    }

    /**
     * Checks whether the specified preference is invalid and sets the specified response object.
     *
     * @param preference     the specified preference
     * @param responseObject the specified response object
     * @return {@code true} if the specified preference is invalid, returns {@code false} otherwise
     */
    private boolean isInvalid(final JSONObject preference, final JSONObject responseObject) {
        responseObject.put(Keys.STATUS_CODE, false);

        final StringBuilder errMsgBuilder = new StringBuilder('[' + langPropsService.get("paramSettingsLabel"));
        errMsgBuilder.append(" - ");

        Map<String,String>  validMap = new HashMap<>();

        validMap.put(Option.ID_C_EXTERNAL_RELEVANT_ARTICLES_DISPLAY_CNT, "externalRelevantArticlesDisplayCntLabel");
        validMap.put(Option.ID_C_RELEVANT_ARTICLES_DISPLAY_CNT,         "relevantArticlesDisplayCntLabel");
        validMap.put(Option.ID_C_RANDOM_ARTICLES_DISPLAY_CNT,           "randomArticlesDisplayCntLabel");
        validMap.put(Option.ID_C_MOST_COMMENT_ARTICLE_DISPLAY_CNT,      "indexMostCommentArticleDisplayCntLabel");
        validMap.put(Option.ID_C_MOST_VIEW_ARTICLE_DISPLAY_CNT,         "indexMostViewArticleDisplayCntLabel");
        validMap.put(Option.ID_C_RECENT_COMMENT_DISPLAY_CNT,            "indexRecentCommentDisplayCntLabel");
        validMap.put(Option.ID_C_MOST_USED_TAG_DISPLAY_CNT,             "indexTagDisplayCntLabel");
        validMap.put(Option.ID_C_ARTICLE_LIST_DISPLAY_COUNT,            "pageSizeLabel");
        validMap.put(Option.ID_C_ARTICLE_LIST_PAGINATION_WINDOW_SIZE,   "windowSizeLabel");
        validMap.put(Option.ID_C_FEED_OUTPUT_CNT,                       "externalRelevantArticlesDisplayCntLabel");

        Iterator iterator = validMap.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String option_key = entry.getKey().toString();
            String label_key = entry.getValue().toString();

            String input = preference.optString(option_key);
            if (!isNonNegativeInteger(input)) {
                errMsgBuilder.append(langPropsService.get(label_key)).
                        append("]  ").
                        append(langPropsService.get("nonNegativeIntegerOnlyLabel"));

                responseObject.put(Keys.MSG, errMsgBuilder.toString());
                return true;
            }

        }

        return false;
    }

    /**
     * Checks whether the specified input is a non-negative integer.
     *
     * @param input the specified input
     * @return {@code true} if it is, returns {@code false} otherwise
     */
    private boolean isNonNegativeInteger(final String input) {
        try {
            return 0 <= Integer.valueOf(input);
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Checks whether the specified domain is a qiniu test domain.
     *
     * @param domain the specified domain
     * @return {@code true} if it is, returns {@code false} otherwise
     */
    private boolean isQiniuTestDomain(final String domain) {
        return Arrays.asList("clouddn.com", "qiniucdn.com", "qiniudn.com", "qnssl.com", "qbox.me").stream().
                anyMatch(testDomain -> StringUtils.containsIgnoreCase(domain, testDomain));
    }
}
