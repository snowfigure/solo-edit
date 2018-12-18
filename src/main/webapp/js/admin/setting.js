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
/**
 * preference for admin.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.2.1.11, Oct 17, 2018
 */

/* setting 相关操作 */
admin.setting = {
    locale: "",
    editorType: "",
    /*
     * 初始化
     */
    init: function (type) {

        $("#tabPreferencePanel_" + type).css("display", "");
        $.ajax({
            url: latkeConfig.servePath + "/console/preference/extra",
            type: "GET",
            cache: false,
            success: function (result, textStatus) {
                $("#tipMsg").text(result.msg);
                if (!result.sc) {
                    $("#loadMsg").text("");
                    return;
                }

                $("#qiniuAccessKey").val(result.qiniu.qiniuAccessKey);
                $("#qiniuSecretKey").val(result.qiniu.qiniuSecretKey);
                $("#qiniuDomain").val(result.qiniu.qiniuDomain);
                $("#qiniuBucket").val(result.qiniu.qiniuBucket);
                $("#qiniuImageView").val(result.qiniu.qiniuImageView);
                $("#qiniuLocalUrlPrefix").val(result.qiniu.qiniuLocalUrlPrefix);
                $("#qiniuEnableACAODomainList").val(result.qiniu.qiniuEnableACAODomainList);


                /*如果本地目录没有设置，则不可以配置"使能同步本地目录"*/
                if("true" === result.qiniu.localPathIsSet){
                    $("#qiniuEnableSyncLocal").removeAttr("readonly");
                    "true" === result.qiniu.qiniuEnableSyncLocal ? $("#qiniuEnableSyncLocal").attr("checked", "checked") : $("#qiniuEnableSyncLocal").removeAttr("checked");

                }else{
                    $("#qiniuSyncLocalSavePath").attr("readonly", "readonly");
                    $("#qiniuEnableSyncLocal").removeAttr("checked")
                }


                /*百度参数*/
                $("#baiduHMCode").val(result.baidu.baiduHMCode);
                "true" === result.baidu.baiduPushEnable ? $("#baiduPushEnable").attr("checked", "checked") : $("#baiduPushEnable").removeAttr("checked");
                "true" === result.baidu.baiduHMEnable ? $("#baiduHMEnable").attr("checked", "checked") : $("#baiduHMEnable").removeAttr("checked");

                /*微信参数*/
                $("#wechatAppID").val(result.wechat.wechatAppID);
                $("#wechatAppSecret").val(result.wechat.wechatAppSecret);
                $("#wechatAppEncodingAESKey").val(result.wechat.wechatAppEncodingAESKey);
                $("#wechatToken").val(result.wechat.wechatToken);
                $("#wechatMsgEncodeMode").val(result.wechat.wechatMsgEncodeMode);


                /*文件上传*/
                $("#uploadFileMode").val(result.uploadfile.uploadFileMode);
                $("#uploadFileLocalPath").val(result.uploadfile.uploadFileLocalPath);
                $("#uploadFileQiniuCDNURLEncodeDomain").val(result.uploadfile.uploadFileQiniuCDNURLEncodeDomain);

                "true" === result.uploadfile.uploadFileEnableCDNSyncToLocal ? $("#uploadFileEnableCDNSyncToLocal").attr("checked", "checked") : $("#uploadFileEnableCDNSyncToLocal").removeAttr("checked");
                "true" === result.uploadfile.uploadFileEnableCDNUploadURLEncode ? $("#uploadFileEnableCDNUploadURLEncode").attr("checked", "checked") : $("#uploadFileEnableCDNUploadURLEncode").removeAttr("checked");
                "true" === result.uploadfile.uploadFileEnableCDNUploadURLAcao ? $("#uploadFileEnableCDNUploadURLAcao").attr("checked", "checked") : $("#uploadFileEnableCDNUploadURLAcao").removeAttr("checked");


                $("#uploadFileCDNUploadURLAcaoWhiteList").val(result.uploadfile.uploadFileCDNUploadURLAcaoWhiteList);

                var select_val = $("#uploadFileMode").children('option:selected').val();
                if("1" === select_val){
                    $("#uploadFileEnableCDNSyncToLocal").removeAttr("disabled");
                    $("#uploadFileEnableCDNUploadURLEncode").removeAttr("disabled");
                    $("#uploadFileEnableCDNUploadURLAcao").removeAttr("disabled");
                    $("#uploadFileCDNUploadURLAcaoWhiteList").removeAttr("disabled");
                    $("#uploadFileQiniuCDNURLEncodeDomain").removeAttr("disabled");
                }

                if("" === $("#qiniuSyncLocalSavePath").val()){
                    $("#uploadFileEnableCDNSyncToLocal").attr("disabled", "disabled");
                }

            }
        });

        $("#uploadFileMode").change(function(){
            var select_val = $("#uploadFileMode").children('option:selected').val();
            if("0" === select_val){
                $("#uploadFileEnableCDNSyncToLocal").attr("disabled", "disabled");
                $("#uploadFileEnableCDNUploadURLEncode").attr("disabled", "disabled");
                $("#uploadFileEnableCDNUploadURLAcao").attr("disabled", "disabled");
                $("#uploadFileCDNUploadURLAcaoWhiteList").attr("disabled", "disabled");
            }

            if("1" === select_val){
                $("#uploadFileEnableCDNSyncToLocal").removeAttr("disabled");
                $("#uploadFileEnableCDNUploadURLEncode").removeAttr("disabled");
                $("#uploadFileEnableCDNUploadURLAcao").removeAttr("disabled");
                $("#uploadFileCDNUploadURLAcaoWhiteList").removeAttr("disabled");
            }

            if("" === $("#qiniuSyncLocalSavePath").val()){
                $("#uploadFileEnableCDNSyncToLocal").attr("disabled", "disabled");
            }
        });
    },



    /*
     * @description 更新 Qiniu 参数
     */
    updateExtra: function () {
        $("#tipMsg").text("");
        $("#loadMsg").text(Label.loadingLabel);

        var requestJSONObject = {
            "qiniuAccessKey": $("#qiniuAccessKey").val(),
            "qiniuSecretKey": $("#qiniuSecretKey").val(),
            "qiniuDomain": $("#qiniuDomain").val(),
            "qiniuBucket": $("#qiniuBucket").val(),
            "qiniuImageView": $("#qiniuImageView").val(),


            "baiduPushEnable": $("#baiduPushEnable").prop("checked"),
            "baiduHMEnable": $("#baiduHMEnable").prop("checked"),
            "baiduHMCode": $("#baiduHMCode").val(),

            "wechatAppID": $("#wechatAppID").val(),
            "wechatAppSecret": $("#wechatAppSecret").val(),
            "wechatAppEncodingAESKey": $("#wechatAppEncodingAESKey").val(),
            "wechatToken": $("#wechatToken").val(),
            "wechatMsgEncodeMode": $("#wechatMsgEncodeMode").val(),


            "uploadFileMode": $("#uploadFileMode").val(),

            "uploadFileEnableCDNSyncToLocal": $("#uploadFileEnableCDNSyncToLocal").prop("checked"),
            "uploadFileEnableCDNUploadURLEncode": $("#uploadFileEnableCDNUploadURLEncode").prop("checked"),
            "uploadFileQiniuCDNURLEncodeDomain": $("#uploadFileQiniuCDNURLEncodeDomain").val(),
            "uploadFileEnableCDNUploadURLAcao": $("#uploadFileEnableCDNUploadURLAcao").prop("checked"),

            "uploadFileCDNUploadURLAcaoWhiteList": $("#uploadFileCDNUploadURLAcaoWhiteList").val(),

        };

        $.ajax({
            url: latkeConfig.servePath + "/console/preference/extra",
            type: "PUT",
            cache: false,
            data: JSON.stringify(requestJSONObject),
            success: function (result, textStatus) {
                $("#tipMsg").html(result.msg);
                $("#loadMsg").text("");
            }
        });
    }
};

/*
 * 注册到 admin 进行管理 
 */
admin.register["setting-upload"] = {
    "obj": admin.setting,
    "init": function(){
        admin.setting.init("uploadFile")
    },
    "refresh": function () {
        admin.clearTip();
    }
};
admin.register["setting-baidu"] = {
    "obj": admin.setting,
    "init": function(){
        admin.setting.init("baidu")
    },
    "refresh": function () {
        admin.clearTip();
    }
};
admin.register["setting-qiniu"] = {
    "obj": admin.setting,
    "init": function(){
        admin.setting.init("qiniu")
    },
    "refresh": function () {
        admin.clearTip();
    }
};
admin.register["setting-wechat"] = {
    "obj": admin.setting,
    "init": function(){
        admin.setting.init("wechat")
    },
    "refresh": function () {
        admin.clearTip();
    }
};
