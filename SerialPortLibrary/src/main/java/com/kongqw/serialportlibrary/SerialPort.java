package com.kongqw.serialportlibrary;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;

public class SerialPort {

    static {
        System.loadLibrary("SerialPort");
    }

    private static final String TAG = SerialPort.class.getSimpleName();

    /**
     * 文件设置最高权限 777 可读 可写 可执行
     *
     * @param file 文件
     * @return 权限修改是否成功
     */
    boolean chmod777(File file) {
        if (null == file || !file.exists()) {
            return false;
        }
        Process process = null;

        process = null;
        try {
            //process = Runtime.getRuntime().exec( "su" );
            process = new ProcessBuilder( new String[]{ "su", "-c", "chmod 777 "+file.getAbsolutePath()} ).redirectErrorStream( true ).start();

            BufferedReader in = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
            String back = in.readLine();
            if( back != null ) {
                Log.e("process", back);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(process != null){
                try{
                    process.destroy();
                }catch (Exception e) {
                }
            }
        }
        return false;
    }

    // 打开串口
    protected native FileDescriptor open(String path, int baudRate, int flags);

    // 关闭串口
    protected native void close();
}
