package com.lyt.fix.library.utils;

import android.os.Environment;

import java.io.File;

public class Constants {
    public static final String DEX_DIR = "odex";//解压目录
    public static final String EXT_DEX_DIR = "fix_dex";//存放服务器下载下来的.dex文件
    public static final String DEX_SUFFIX = ".dex";//dex格式
    public static final String MAIN_DEX = "classes.dex";//主dex
    public static final String OPT_DEX_DIR = "opt_dex";//临时目录
    public static final String DEX_EXT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+Constants.EXT_DEX_DIR;//存放服务器下载下来的.dex文件

}
