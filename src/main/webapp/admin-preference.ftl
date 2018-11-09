<#--

    Solo - A small and beautiful blogging system written in Java.
    Copyright (c) 2010-2018, b3log.org & hacpai.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

-->
<div id="tabPreference" class="sub-tabs fn-clear">
    <ul>
        <li>
            <div id="tabPreference_config">
                <a class="tab-current" href="#tools/preference/config">${configSettingsLabel}</a>
            </div>
        </li>
        <li>
            <div id="tabPreference_webHtmlExtend">
                <a href="#tools/preference/webHtmlExtend">${webHtmlExtendLabel}</a>
            </div>
        </li>
        <li>
            <div id="tabPreference_skins">
                <a href="#tools/preference/skins">${skinLabel}</a>
            </div>
        </li>
        <li>
            <div id="tabPreference_signs">
                <a href="#tools/preference/signs">${signLabel}</a>
            </div>
        </li>
        <li>
            <div id="tabPreference_setting">
                <a href="#tools/preference/setting">${paramSettingsLabel}</a>
            </div>
        </li>
        <li>
            <div id="tabPreference_uploadFile">
                <a href="#tools/preference/uploadFile">${uploadFileConfigLabel}</a>
            </div>
        </li>
        <li>
            <div id="tabPreference_qiniu">
                <a href="#tools/preference/qiniu">${qiniuLabel}</a>
            </div>
        </li>
        <li>
            <div id="tabPreference_baidu">
                <a href="#tools/preference/baidu">${baiduSiteLabel}</a>
            </div>
        </li>
        <li>
            <div id="tabPreference_wechat">
                <a href="#tools/preference/wechat">${wechatConfigLabel}</a>
            </div>
        </li>
        <li>
            <div id="tabPreference_solo">
                <a href="#tools/preference/solo">B3log</a>
            </div>
        </li>
    </ul>
</div>
<div id="tabPreferencePanel" class="sub-tabs-main">
    <div id="tabPreferencePanel_config" class="form">
        <button onclick="admin.preference.update()" class="right">${updateLabel}</button>
        <div class="clear"></div>
        <label for="blogTitle">${blogTitle1Label}</label>
        <input id="blogTitle" type="text"/>

        <label for="blogSubtitle">${blogSubtitle1Label}</label>
        <input id="blogSubtitle" type="text"/>

        <label for="blogHost">${blogHost1Label}</label>
        <input id="blogHost" type="text" value="${servePath}" readonly="true"/>

        <label for="metaKeywords">${metaKeywords1Label}</label>
        <input id="metaKeywords" type="text"/>

        <label for="metaDescription">${metaDescription1Label}</label>
        <input id="metaDescription" type="text"/>

        <label for="noticeBoard">${noticeBoard1Label}</label>
        <textarea rows="6" id="noticeBoard"></textarea>

        <label for="footerContent">${footerContent1Label}</label>
        <textarea rows="2" id="footerContent"></textarea>

        <br><br>
        <button onclick="admin.preference.update()" class="right">${updateLabel}</button>
        <div class="clear"></div>
    </div>
    <div id="tabPreferencePanel_solo" class="none form">
        <button class="right" onclick="admin.preference.update()">${updateLabel}</button>
        <div class="clear"></div>

        <label for="keyOfSolo">${keyOfSolo1Label}</label>
        <input id="keyOfSolo" class="normalInput" type="text" readonly="readonly"/><br><br>
        <a href="https://hacpai.com/article/1457158841475" target="_blank">${APILabel}</a>

        <label for="enableSendArticelToB3">${enableSendArticelToB3Label}</label>
        <input id="enableSendArticelToB3" type="checkbox" class="normalInput"/>

        <br><br>
        <button onclick="admin.preference.update()" class="right">${updateLabel}</button>
        <div class="clear"></div>
    </div>
    <div id="tabPreferencePanel_setting" class="none form">
        <button class="right" onclick="admin.preference.update()">${updateLabel}</button>
        <div class="clear"></div>
        <label for="localeString">${localeString1Label}</label>
        <select id="localeString">
            <option value="zh_CN">简体中文</option>
            <option value="en_US">Englisth(US)</option>
        </select>
        <label for="timeZoneId">${timeZoneId1Label}</label>
        <select id="timeZoneId">
        ${timeZoneIdOptions}
        </select>
        <label for="articleListDisplay">${articleListDisplay1Label}</label>
        <select id="articleListDisplay">
            <option value="titleOnly">${titleOnlyLabel}</option>
            <option value="titleAndAbstract">${titleAndAbstractLabel}</option>
            <option value="titleAndContent">${titleAndContentLabel}</option>
        </select>
        <label for="mostUsedTagDisplayCount">${indexTagDisplayCnt1Label}</label>
        <input id="mostUsedTagDisplayCount" class="normalInput" type="text"/>
        <label for="recentArticleDisplayCount">${indexRecentArticleDisplayCnt1Label}</label>
        <input id="recentArticleDisplayCount" class="normalInput" type="text"/>
        <label for="recentArticleDisplayCount">${indexRecentCommentDisplayCnt1Label}</label>
        <input id="recentCommentDisplayCount" class="normalInput" type="text"/>
        <label for="mostCommentArticleDisplayCount">${indexMostCommentArticleDisplayCnt1Label}</label>
        <input id="mostCommentArticleDisplayCount" class="normalInput" type="text"/>
        <label for="mostViewArticleDisplayCount">${indexMostViewArticleDisplayCnt1Label}</label>
        <input id="mostViewArticleDisplayCount" class="normalInput" type="text"/>
        <label for="articleListDisplayCount">${pageSize1Label}</label>
        <input id="articleListDisplayCount" class="normalInput" type="text"/>
        <label for="articleListPaginationWindowSize">${windowSize1Label}</label>
        <input id="articleListPaginationWindowSize" class="normalInput" type="text"/>
        <label for="randomArticlesDisplayCount">${randomArticlesDisplayCnt1Label}</label>
        <input id="randomArticlesDisplayCount" class="normalInput" type="text"/>
        <label for="relevantArticlesDisplayCount">${relevantArticlesDisplayCnt1Label}</label>
        <input id="relevantArticlesDisplayCount" class="normalInput" type="text"/>
        <label for="externalRelevantArticlesDisplayCount">${externalRelevantArticlesDisplayCnt1Label}</label>
        <input id="externalRelevantArticlesDisplayCount" class="normalInput" type="text"/>
        <label for="enableArticleUpdateHint">${enableArticleUpdateHint1Label}</label>
        <input id="enableArticleUpdateHint" type="checkbox" class="normalInput"/>
        <label for="allowVisitDraftViaPermalink">${allowVisitDraftViaPermalink1Label}</label>
        <input id="allowVisitDraftViaPermalink" type="checkbox" class="normalInput"/>
        <label for="commentable">${allowComment1Label}</label>
        <input id="commentable" type="checkbox" class="normalInput"/>
        <label for="allowRegister">${allowRegister1Label}</label>
        <input id="allowRegister" type="checkbox" class="normalInput"/>
        <label for="feedOutputMode">${feedOutputModel1Label}</label>
        <select id="feedOutputMode">
            <option value="abstract">${abstractLabel}</option>
            <option value="fullContent">${fullContentLabel}</option>
        </select>
        <label for="feedOutputCnt">${feedOutputCnt1Label}</label>
        <input id="feedOutputCnt" class="normalInput" type="text"/><br/><br/>
        <button class="right" onclick="admin.preference.update()">${updateLabel}</button>
        <div class="clear"></div>
    </div>
    <div id="tabPreferencePanel_skins" class="none form">
        <table class="form" width="100%" cellpadding="0" cellspacing="0">
            <tbody>
            <tr>
                <td>
                    <a href="https://github.com/b3log/solo/issues/12449" target="_blank">新皮肤推荐</a> •
                    <a href="https://hacpai.com/article/1493814851007" target="_blank">皮肤开发指南</a>
                    <button style="float: right" onclick="admin.preference.update()">${updateLabel}</button>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="skinMain"></div>
                </td>
            </tr>
            <tr>
                <td>
                    <a href="https://github.com/b3log/solo/issues/12449" target="_blank">新皮肤推荐</a> •
                    <a href="https://hacpai.com/article/1493814851007" target="_blank">皮肤开发指南</a>
                    <button style="float: right" onclick="admin.preference.update()">${updateLabel}</button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="tabPreferencePanel_signs" class="none form">
        <button onclick="admin.preference.update()" class="right">${updateLabel}</button>
        <div class="clear"></div>
        <button id="preferenceSignButton1">${signLabel}1</button>
        <textarea rows="8" id="preferenceSign1"></textarea>
        <button id="preferenceSignButton2">${signLabel}2</button>
        <textarea rows="8" id="preferenceSign2"></textarea>
        <button id="preferenceSignButton3">${signLabel}3</button>
        <textarea rows="8" id="preferenceSign3"></textarea><br><br>
        <button onclick="admin.preference.update()" class="right">${updateLabel}</button>
        <div class="clear"></div>
    </div>



    <div id="tabPreferencePanel_qiniu" class="none form">
        <span class="right">
            <a href="https://hacpai.com/article/1442418791213" target="_blank">${howConfigLabel}</a>
            &nbsp;
            <button onclick="admin.preference.updateExtra()">${updateLabel}</button>
        </span>
        <div class="clear"></div>
        <label for="qiniuAccessKey">${accessKey1Label}</label>
        <input id="qiniuAccessKey" type="text"/>
        <label for="qiniuSecretKey">${secretKey1Label}</label>
        <input id="qiniuSecretKey" type="text"/>
        <label for="qiniuDomain">${domain1Label}</label>
        <input id="qiniuDomain" type="text"/>
        <label for="qiniuBucket">${bucket1Label}</label>
        <input id="qiniuBucket" type="text"/>
        <label for="qiniuImageView">${qiniuImageViewLabel}</label>
        <textarea rows="3" id="qiniuImageView"></textarea>





        <br/><br/>

        <button onclick="admin.preference.updateExtra()" class="right">${updateLabel}</button>
        <div class="clear"></div>


    </div>
    <div id="tabPreferencePanel_baidu" class="none form">
        <button onclick="admin.preference.updateExtra()" class="right">${updateLabel}</button>
        <div class="clear"></div>

        <label for="baiduHMCode">${baiduHMCodeLabel}</label>
        <input id="baiduHMCode" type="text"/>

        <label for="baiduPushEnable">${baiduPushEnableLabel}</label>
        <input id="baiduPushEnable" type="checkbox" class="normalInput"/>

        <label for="baiduHMEnable">${baiduHMEnableLabel}</label>
        <input id="baiduHMEnable" type="checkbox" class="normalInput"/>

        <br><br>
        <button onclick="admin.preference.updateExtra()" class="right">${updateLabel}</button>
        <div class="clear"></div>
    </div>

    <div id="tabPreferencePanel_webHtmlExtend" class="none form">
        <button onclick="admin.preference.update()" class="right">${updateLabel}</button>
        <div class="clear"></div>

        <label for="htmlHead">${htmlhead1Label}</label>
        <textarea rows="6" id="htmlHead"></textarea>

        <label for="cdnjQueryJS">${cdnjQueryJSLabel}</label>
        <textarea rows="2" id="cdnjQueryJS"></textarea>

        <label for="cdnIconfont">${cdnIconfontLabel}</label>
        <textarea rows="2" id="cdnIconfont"></textarea>

        <label for="bannerImageUrl">${bannerImageUrlLabel}</label>
        <textarea rows="2" id="bannerImageUrl"></textarea>

        <br><br>
        <button onclick="admin.preference.update()" class="right">${updateLabel}</button>
        <div class="clear"></div>
    </div>

    <div id="tabPreferencePanel_wechat" class="none form">
        <button onclick="admin.preference.updateExtra()" class="right">${updateLabel}</button>
        <div class="clear"></div>

        <label for="wechatAppID">${wechatAppIDLabel}</label>
        <input id="wechatAppID" type="text"/>

        <label for="wechatAppSecret">${wechatAppSecretLabel}</label>
        <input id="wechatAppSecret" type="text"/>

        <label for="wechatAppEncodingAESKey">${wechatAppEncodingAESKeyLabel}</label>
        <input id="wechatAppEncodingAESKey" type="text"/>

        <label for="wechatToken">${wechatTokenLabel}</label>
        <input id="wechatToken" type="text"/>

        <label for="wechatMsgEncodeMode">${wechatMsgEncodeModeLabel}</label>
        <select id="wechatMsgEncodeMode">
            <option value="0">${wechatEncodeModePlainLabel}</option>
            <option value="1">${wechatEncodeModeCompatibilityLabel}</option>
            <option value="2">${wechatEncodeModeEncryptionLabel}</option>
        </select>

        <br><br>
        <button onclick="admin.preference.updateExtra()" class="right">${updateLabel}</button>
        <div class="clear"></div>
    </div>

    <div id="tabPreferencePanel_webHtmlExtend" class="none form">
        <button onclick="admin.preference.update()" class="right">${updateLabel}</button>
        <div class="clear"></div>

        <label for="htmlHead">${htmlhead1Label}</label>
        <textarea rows="6" id="htmlHead"></textarea>

        <label for="cdnjQueryJS">${cdnjQueryJSLabel}</label>
        <textarea rows="2" id="cdnjQueryJS"></textarea>

        <label for="cdnIconfont">${cdnIconfontLabel}</label>
        <textarea rows="2" id="cdnIconfont"></textarea>

        <label for="bannerImageUrl">${bannerImageUrlLabel}</label>
        <textarea rows="2" id="bannerImageUrl"></textarea>

        <br><br>
        <button onclick="admin.preference.update()" class="right">${updateLabel}</button>
        <div class="clear"></div>
    </div>

    <div id="tabPreferencePanel_uploadFile" class="none form">
        <button onclick="admin.preference.updateExtra()" class="right">${updateLabel}</button>
        <div class="clear"></div>

        <!--本地上传路径 -->

        <label for="uploadFileLocalPath">${uploadFileLocalPathLabel}</label>
        <input id="uploadFileLocalPath" type="text" disabled readonly/>

        <label for="uploadFileMode">${uploadFileModeLabel}</label>
        <select id="uploadFileMode">
            <option value="0">${uploadFileSelectLocalLabel}</option>
            <option value="1">${uploadFileSelectQiniuLabel}</option>
        </select>

        <label for="uploadFileEnableCDNSyncToLocal">${uploadFileEnableCDNSyncToLocalLabel}</label>
        <input id="uploadFileEnableCDNSyncToLocal" type="checkbox" class="normalInput" disabled/>

        <label for="uploadFileEnableCDNUploadURLEncode">${uploadFileEnableCDNUploadURLEncodeLabel}</label>
        <input id="uploadFileEnableCDNUploadURLEncode" type="checkbox" class="normalInput" disabled/>

        <label for="uploadFileQiniuCDNURLEncodeDomain">${uploadFileQiniuCDNURLEncodeDomainLabel}</label>
        <input id="uploadFileQiniuCDNURLEncodeDomain" type="text" disabled/>


        <label for="uploadFileEnableCDNUploadURLAcao">${uploadFileEnableCDNUploadURLAcaoLabel}</label>
        <input id="uploadFileEnableCDNUploadURLAcao" type="checkbox" class="normalInput" disabled/>

        <label for="uploadFileCDNUploadURLAcaoWhiteList">${uploadFileCDNUploadURLAcaoWhiteListLabel}</label>
        <textarea rows="3" id="uploadFileCDNUploadURLAcaoWhiteList" disabled></textarea>

        <br><br>
        <button onclick="admin.preference.updateExtra()" class="right">${updateLabel}</button>
        <div class="clear"></div>
    </div>



</div>
${plugins}