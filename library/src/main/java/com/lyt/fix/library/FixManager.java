package com.lyt.fix.library;

import android.content.Context;
import android.text.TextUtils;

import com.lyt.fix.library.utils.ArrayUitls;
import com.lyt.fix.library.utils.Constants;
import com.lyt.fix.library.utils.FileUtils;
import com.lyt.fix.library.utils.ReflectUitls;

import java.io.File;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixManager {

    //需要复制的dex
    private static HashSet<File> loadedDex = new HashSet<>();

    static {
        loadedDex.clear();
    }

    private static FixManager instance;

    private FixManager() {
    }

    public static FixManager getInstance() {
        if (instance == null) {
            synchronized (FixManager.class) {
                if (instance == null) {
                    instance = new FixManager();
                }
            }
        }
        return instance;
    }

    /**
     * 开始热修复
     *
     * @param context 上下文对象
     */
    public void loadFixedDex(Context context) {
        File fileDir = getPrivateDexDir(context);
        File[] listFiles = fileDir.listFiles();
        for (File file : listFiles) {
            if (file.getName().endsWith(Constants.DEX_SUFFIX) && !Constants.MAIN_DEX.equals(file.getName())) {
                loadedDex.add(file);
            }
        }
        //1.创建自有的类加载器
        createDexClassLoader(context.getApplicationContext(), fileDir);
    }

    /**
     * 创建Dex类加载器
     *
     * @param context 上下文对象
     * @param fileDir 私有目录  存放修复好的Dex文件
     */
    private void createDexClassLoader(Context context, File fileDir) {
        //临时目录
        String optimizedDirectory = fileDir.getAbsoluteFile() + File.separator + Constants.OPT_DEX_DIR;
        File fopt = new File(optimizedDirectory);
        if (!fopt.exists()) {
            fopt.mkdirs();
        }
        for (File dex : loadedDex) {
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(), optimizedDirectory, null, context.getClassLoader());
            hotFix(context, classLoader);
        }
    }

    /**
     * 热修复
     *
     * @param context     上下文对象
     * @param classLoader Dex类加载器
     */
    private void hotFix(Context context, DexClassLoader classLoader) {
        //最核心的6行代码
        try {
            //1.获取系统的pathClassLoader
            PathClassLoader pathLoader = (PathClassLoader) context.getClassLoader();

            //2.获取自有的dexElements[]
            Object myDexElements = ReflectUitls.getDexElements(ReflectUitls.getPathList(classLoader));

            //3.获取系统的dexElements[]
            Object sysDexElements = ReflectUitls.getDexElements(ReflectUitls.getPathList(pathLoader));

            //4.合并新的dexElements[]
            Object newDexElements = ArrayUitls.combineArray(myDexElements, sysDexElements);

            //5.获取系统的pathList
            Object sysPathList = ReflectUitls.getPathList(pathLoader);

            //6.将新的dexElements[] 通过反射赋值给pathList
            ReflectUitls.setField(sysPathList, sysPathList.getClass(), newDexElements);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加 Dex文件
     *
     * @param context    上下文对象
     * @param sourceName 文件名 classes*.dex
     */
    public void addDex(Context context, String sourceName) {
        if (!TextUtils.isEmpty(sourceName) && sourceName.endsWith(Constants.DEX_SUFFIX)) {
            //dex文件
            File sourceFile = new File(getDexDir(), sourceName);
            //私有目录下存放的dex文件
            File targetFile = new File(getPrivateDexDir(context), sourceName);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            try {
                FileUtils.copyFile(sourceFile, targetFile);
                if (sourceFile.exists()) {
                    sourceFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除私有目录odex下所有的dex文件 当版本更新时需要删除Dex文件
     *
     * @param context 上下文对象
     */
    public void delete(Context context) {
        deleteExtDex();
        File[] listFiles = getPrivateDexDir(context).listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {
                file.delete();
            }
        }
    }

    /**
     * 删除外部目录dex文件夹下的所有dex文件 当从服务器上下载dex文件时应该调用
     */
    public void deleteExtDex() {
        File[] listFiles = getDexDir().listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {
                file.delete();
            }
        }
    }

    /**
     * 存放网络下载的Dex目录
     *
     * @return
     */
    public File getDexDir() {
        File dexFileDir = new File(Constants.DEX_EXT_DIR);
        if (!dexFileDir.exists()) {
            dexFileDir.mkdirs();
        }
        return dexFileDir;
    }

    /**
     * 获取私有dex存储目录
     *
     * @param context
     * @return
     */
    private File getPrivateDexDir(Context context) {
        File file_private_dir = context.getApplicationContext().getDir(Constants.DEX_DIR, Context.MODE_PRIVATE);
        return file_private_dir;
    }


}
