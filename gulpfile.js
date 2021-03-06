/*
 * Symphony - A modern community (forum/SNS/blog) platform written in Java.
 * Copyright (C) 2012-2017,  b3log.org & hacpai.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @file frontend tool.
 *
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.7.0.0, Oct 2, 2018
 */

'use strict'
const gulp = require('gulp')
const concat = require('gulp-concat')
const uglify = require('gulp-uglify')
const sass = require('gulp-sass')
const rename = require('gulp-rename')
const minifycss = require('gulp-minify-css')
const del = require('del')

function sassProcess () {
  return gulp.src('./src/main/webapp/skins/*/css/*.scss').
    pipe(sass().on('error', sass.logError)).
    pipe(gulp.dest('./src/main/webapp/skins/'))
}

function sassProcessWatch () {
  gulp.watch('./src/main/webapp/skins/*/css/*.scss', sassProcess)
}

gulp.task('watch', gulp.series(sassProcessWatch))


function miniAdmin () {
  // concat js
  const jsJqueryUpload = [
    './src/main/webapp/js/lib/jquery/jquery.min.js',
    './src/main/webapp/js/lib/jquery/file-upload-9.10.1/vendor/jquery.ui.widget.js',
    './src/main/webapp/js/lib/jquery/file-upload-9.10.1/jquery.iframe-transport.js',
    './src/main/webapp/js/lib/jquery/file-upload-9.10.1/jquery.fileupload.js',
    './src/main/webapp/js/lib/jquery/jquery.bowknot.min.js',
    // codemirror
    './src/main/webapp/js/lib/CodeMirrorEditor/codemirror.js',
    './src/main/webapp/js/lib/CodeMirrorEditor/placeholder.js',
    './src/main/webapp/js/overwrite/codemirror/addon/hint/show-hint.js',
    './src/main/webapp/js/lib/CodeMirrorEditor/editor.js',
    './src/main/webapp/js/lib/to-markdown.js',
    './src/main/webapp/js/lib/highlight.js-9.6.0/highlight.pack.js'
  ]
  return gulp.src(jsJqueryUpload).
    pipe(uglify({output: {ascii_only: true}})).
    // https://github.com/b3log/solo/issues/12522
    pipe(concat('admin-lib.min.js')).
    pipe(gulp.dest('./src/main/webapp/js/lib/compress/'))

}


function mergeLatkeAdmin() {
    const jsJqueryAdmin = [
        './src/main/webapp/js/admin/admin.js',
        './src/main/webapp/js/admin/editor.js',
        './src/main/webapp/js/admin/editorTinyMCE.js',
        './src/main/webapp/js/admin/editorKindEditor.js',
        './src/main/webapp/js/admin/editorCodeMirror.js',
        './src/main/webapp/js/admin/tablePaginate.js',
        './src/main/webapp/js/admin/article.js',
        './src/main/webapp/js/admin/comment.js',
        './src/main/webapp/js/admin/articleList.js',
        './src/main/webapp/js/admin/draftList.js',
        './src/main/webapp/js/admin/pageList.js',
        './src/main/webapp/js/admin/others.js',
        './src/main/webapp/js/admin/linkList.js',
        './src/main/webapp/js/admin/preference.js',
        './src/main/webapp/js/admin/pluginList.js',
        './src/main/webapp/js/admin/userList.js',
        './src/main/webapp/js/admin/categoryList.js',
        './src/main/webapp/js/admin/commentList.js',
        './src/main/webapp/js/admin/plugin.js',
        './src/main/webapp/js/admin/main.js',
        './src/main/webapp/js/admin/about.js'
    ]

    return gulp.src(jsJqueryAdmin).
    pipe(concat('latkeAdmin.js')).
    pipe(gulp.dest('./src/main/webapp/js/admin/'))
}

function miniLatkeAdmin() {

    return gulp.src('./src/main/webapp/js/admin/latkeAdmin.js').
      pipe(uglify({output: {ascii_only: true}})).
      pipe(concat('latkeAdmin.min.js')).
      pipe(gulp.dest('./src/main/webapp/js/admin/'))
}

function miniPjax (){
  // concat js
  const jsPjax = [
    './src/main/webapp/js/lib/jquery/jquery-3.1.0.min.js',
    './src/main/webapp/js/lib/jquery/jquery.pjax.js',
    './src/main/webapp/js/lib/nprogress/nprogress.js']
  return gulp.src(jsPjax).
    pipe(uglify()).
    pipe(concat('pjax.min.js')).
    pipe(gulp.dest('./src/main/webapp/js/lib/compress/'))
}

function common_scripts() {
    const jsfiles = [
        './src/main/webapp/js/common.js',
        './src/main/webapp/js/page.js'
    ]
    return gulp.src(jsfiles).
        pipe(rename({suffix:'.min'})).
        pipe(uglify()).
        pipe(gulp.dest('./src/main/webapp/js/'))
}

function skin_scripts () {
  // minify js
  return gulp.src('./src/main/webapp/skins/*/js/*.js').
    pipe(rename({suffix: '.min'})).
    pipe(uglify()).
    pipe(gulp.dest('./src/main/webapp/skins/'))
}

function admin_styles(){
    const cssfiles = [
        './src/main/webapp/css/default-admin.css',
        './src/main/webapp/css/default-base.css',
        './src/main/webapp/css/default-init.css'
    ]
    return gulp.src(cssfiles).
    pipe(rename({suffix: '.min'})).
    pipe(minifycss()).
    pipe(gulp.dest('./src/main/webapp/css/'))
}

function skin_styles () {
  // minify css
  return gulp.src('./src/main/webapp/skins/*/css/*.css').
    pipe(rename({suffix: '.min'})).
    pipe(minifycss()).
    pipe(gulp.dest('./src/main/webapp/skins/'))
}


function cleanProcess () {
  return del([
    './src/main/webapp/skins/*/css/*.min.css',
    './src/main/webapp/skins/*/js/*.min.js'])
}

gulp.task('default',
  gulp.series(cleanProcess, sassProcess,
      gulp.parallel(skin_scripts, skin_styles),
      gulp.parallel(miniPjax, miniAdmin, mergeLatkeAdmin, admin_styles),
      gulp.parallel(common_scripts, miniLatkeAdmin)
  )
)