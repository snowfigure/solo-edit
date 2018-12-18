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

<div id="tabPreferencePanel" class="sub-tabs-main">
    <div id="tabPreferencePanel_qiniu" class="form" style="display: none">
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
    <div id="tabPreferencePanel_baidu" class="form" style="display: none">
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


    <div id="tabPreferencePanel_wechat" class="form" style="display: none">
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



    <div id="tabPreferencePanel_uploadFile" class="form" style="display: none">
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