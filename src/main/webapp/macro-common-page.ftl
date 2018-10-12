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
<#macro commonPage title>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="initial-scale=1.0,user-scalable=no,maximum-scale=1,width=device-width">
    <meta name="viewport" content="initial-scale=1.0,user-scalable=no,maximum-scale=1" media="(device-height: 568px)">
    <meta name="robots" content="none"/>
    <title><#if blogTitle??>${blogTitle} - </#if>${title}</title>
    <link type="text/css" rel="stylesheet"
          href="${staticServePath}/css/default-init${miniPostfix}.css?${staticResourceVersion}" charset="utf-8"/>
    <link rel="icon" type="image/png" href="${staticServePath}/favicon.png"/>
    <link rel="apple-touch-icon" href="${staticServePath}/favicon.png">
</head>
<body>
<div class="wrap">
    <div class="content">
        <div class="logo">
            <a href="${servePath}" target="_blank">
                <img width="128" border="0" alt="${blogTitle}" title="${blogTitle}" src="${staticServePath}/images/logo.png"/>
            </a>
        </div>
        <div class="main">
            <#nested >
        </div>
        <span class="clear"></span>
    </div>
</div>
<div class="footerWrapper">

    <footer class="footer">
        <div class="fn-clear">
            <span>
                ${viewCount1Label}${statistic.statisticBlogViewCount}
                &nbsp;
                    ${articleCount1Label}${statistic.statisticPublishedBlogArticleCount}
                &nbsp;
                    ${commentCount1Label}${statistic.statisticPublishedBlogCommentCount}
                &nbsp;
                    ${onlineVisitor1Label}${onlineVisitorCnt}
            </span>
        </div>
        <div class="fn-clear">
            <span>
                &copy; 2013 - ${year} <a href="${servePath}">${blogTitle}</a>
                ${footerContent}
            </span>
            <span>
                Powered by <a href="https://b3log.org" target="_blank">B3log</a> •
                <a href="https://solo.b3log.org" target="_blank">Solo</a> ${version}
            </span>
        </div>
        <span onclick="Util.goTop()" class="icon-goup"></span>
    </footer>
</div>
</body>
</html>
</#macro>
