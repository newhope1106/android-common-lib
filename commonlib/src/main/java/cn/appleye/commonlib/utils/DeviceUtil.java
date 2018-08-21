package cn.appleye.commonlib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author newhope1106
 * date 2018/8/16
 * 获取设备信息
 */
public class DeviceUtil {
    private static DecimalFormat sFileDecimalFormat = new DecimalFormat("#0.00");
    private static DecimalFormat sFileIntegerFormat = new DecimalFormat("#0");

    /**
     * 获取CPU个数
     * @return CPU个数
     * */
    public static int getCoresNumber(){
        try{
            File dir = new File("/sys/devices/system/cpu");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }
            });
            return files.length;
        }catch (Exception e){
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 获取最大CPU频率
     * @return CPU频率 e.g.:1.5GHZ
     * */
    public static String getMaxCputFreq(){
        FileReader fr = null;
        BufferedReader br = null;
        DecimalFormat df = sFileDecimalFormat;
        String cpuFreq = "0GHZ";
        try{
            fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
            br = new BufferedReader(fr);
            int result = Integer.parseInt(br.readLine().trim());
            cpuFreq = df.format((double)result / 1000000.0D) + "GHZ";
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //文件流的关闭顺序，先打开的后关闭
            if(br != null){
                try {
                    br.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            if(fr != null){
                try {
                    fr.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return cpuFreq;
    }

    /**
     * 获取可用内存
     * @return e.g.:2G
     * */
    public static String getAvailableMemorySize(Context context){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);

        return formatFileSize(memoryInfo.availMem, true);
    }

    /**
     * 获取总共的内存大小
     * @return e.g.:2G
     * */
    public static String getTotalMemorySize(){
        String dir = "/proc/meminfo";
        String memorySize = "0M";

        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(dir);
            br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            memorySize = formatFileSize((long)Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024L, true);
        } catch (Exception e) {
            e.printStackTrace();
            return "0M";
        } finally {
            if(br != null){
                try {
                    br.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(fr != null){
                try{
                    fr.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return memorySize;
    }

    /**
     * 格式化文件大小数值
     * @return xxx B/K/M/G
     * */
    public static String formatFileSize(long size, boolean isInteger) {
        DecimalFormat df = isInteger ? sFileIntegerFormat : sFileDecimalFormat;
        String fileSizeString;
        if (size < 1024L && size > 0L) {
            fileSizeString = df.format((double)size) + "B";
        } else if (size < 1048576L) {
            fileSizeString = df.format((double)size / 1024.0D) + "K";
        } else if (size < 1073741824L) {
            fileSizeString = df.format((double)size / 1048576.0D) + "M";
        } else {
            fileSizeString = df.format((double)size / 1.073741824E9D) + "G";
        }

        return fileSizeString;
    }

    /**
     * 外部存储是否可用
     * @return true 可用 false 不可用
     * */
    public static boolean isExternalStorageAvailable(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 外部存储可用空间
     * @return 外部存储可用字节大小
     * */
    public static long getExternalStorageAvailableSpace(){
        if(isExternalStorageAvailable()){
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks * blockSize;
        }else {
            return -1L;
        }
    }

    /**
     * 外部存储总共空间
     * @return 外部存储总共字节大小
     * */
    public static long getExternalStorageTotalSpace(){
        if(isExternalStorageAvailable()){
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlock = stat.getBlockCountLong();
            return totalBlock * blockSize;
        }else {
            return -1L;
        }
    }

    /**
     * 用户内部存储数据空间可用大小
     * @return 用户内部存储数据空间可用字节大小
     * */
    public static long getUserDataStorageAvailableSpace(){
        if(isExternalStorageAvailable()){
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks * blockSize;
        }else {
            return -1L;
        }
    }

    /**
     * 用户内部存储数据空间总共大小
     * @return 用户内部存储数据空间总共字节大小
     * */
    public static long getUserDataStorageTotalSpace(){
        if(isExternalStorageAvailable()){
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getBlockCountLong();
            return availableBlocks * blockSize;
        }else {
            return -1L;
        }
    }

    /**
     * 获取手机的IMEI
     * @return IMEI, 没有的话返回空字符串
     * */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getIMEI(Context context){
        TelephonyManager  tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        if(tm == null){
            return "";
        }

        if(Build.VERSION.SDK_INT >= 26){
            return tm.getImei();
        } else {
            return tm.getDeviceId();
        }
    }

    /**
     * 获取手机的SIM id
     * @return Sim id, 没有的话返回空字符串
     * */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getSimId(Context context){
        TelephonyManager  tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        if(tm == null){
            return "";
        }

        return tm.getSubscriberId();
    }

    /**
     * 获取手机号码
     * @return 手机号码, 没有的话返回空字符串
     * */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getPhoneNumber(Context context){
        TelephonyManager  tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        if(tm == null){
            return "";
        }
        return tm.getLine1Number();
    }

    /**
     * 获取sdk版本号
     * @return sdk版本
     * */
    public static int getSdkVersion(){
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机型号
     * @return 手机型号
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * Gps是否打开
     * @param context
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = locationManager.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }
}
