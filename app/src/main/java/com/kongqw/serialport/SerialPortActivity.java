package com.kongqw.serialport;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;
import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortManager;

import java.io.File;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static java.lang.Thread.sleep;

//宣告一個類別為 SerialPortActivity 且繼承自 AppCompatActivity
public class SerialPortActivity extends AppCompatActivity implements OnOpenSerialPortListener {

    private static final String TAG = SerialPortActivity.class.getSimpleName();
    public static final String DEVICE = "device";
    private SerialPortManager mSerialPortManager;
    private TextView mReceiveTextView;
    private ByteBuf buffer = Unpooled.buffer(1024 * 1000);

    //設定簡單模式下封包長度
    //被宣告static的變數或方法 不會讓物件各自擁有，而是類別所擁有
    private static final int PACKAGE_LEN = 11;


    //@Override是一個annotation(註解),onCreate()是要複寫原本母類別
    //的function,強化母類別AppCompatActivity裡就已經有的onCreate()
    //super.onCreate(),super代表母類別
    //呼叫母類別的onCreate(),初始化的概念
    //setContentView()也是母類別裡面原有的function,功用是把
    //activity_serial_port這個layout實作

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_port);

        Device device = (Device) getIntent().getSerializableExtra(DEVICE);
        Log.i(TAG, "onCreate: device = " + device);
        if (null == device) {
            finish();
            return;
        }
        mReceiveTextView = (TextView) findViewById(R.id.textView_receive);

        //將SerialPortManager 實體化(instantiate)
        mSerialPortManager = new SerialPortManager();

        // 打开串口
        boolean openSerialPort = mSerialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(byte[] data) {
                        Log.v(TAG, "接收 " + byteArrayToHexString(data));

//                        Log.i(TAG, "onDataReceived [ byte[] ]: " + Arrays.toString(bytes));
//
//                        Log.i(TAG, "onDataReceived [ String ]: " + new String(bytes));
//                        final byte[] finalBytes = bytes;
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mReceiveTextView.append(byteArrayToHexString(finalBytes));
//
//                                Log.v(TAG,"接收\n%s" + new String(finalBytes));
//                            }
//                        });


                        //假如data不是空的 而且資料長度大於0
                        //就把資料寫進緩衝器
                        if (data != null && data.length > 0) {
                            buffer.writeBytes(data);

                            //當緩衝器資料(回傳值(int))長度大於等於PACKAGE_LEN(11)
                            //宣告緩衝器bufTemp,使用方法bufTemp.readBytes(1), 存入1筆資料給bufTemp
                            //宣告bytesTemp陣列容量為1
                            //使用方法bufTemp.readBytes()把值回傳存給bytesTemp陣列
                            while (buffer.readableBytes() >= PACKAGE_LEN) {
                                ByteBuf bufTemp = buffer.readBytes(1);
                                byte[] bytesTemp = new byte[1];
                                bufTemp.readBytes(bytesTemp);


                                //假如bytesTemp第一筆資料符合0x5a(readerIndex)
                                //把當前readerIndex賦值到markReaderIndex
                                //宣告緩衝器bufTemp1,存入3筆資料
                                //宣告陣列bytesTemp1,容量為3
                                //使用方法bufTemp.readBytes()把值回傳存給bytesTemp1陣列
                                if (bytesTemp[0] == (byte) 0x5a) {
                                    buffer.markReaderIndex();
                                    ByteBuf bufTemp1 = buffer.readBytes(3);
                                    byte[] bytesTemp1 = new byte[3];
                                    bufTemp1.readBytes(bytesTemp1);

                                    //假如bytesTemp1符合[0x5a,0x25,0x06]
                                    //宣告緩衝器bufTemp2,存入(PACKAGE_LEN - 4)筆資料
                                    //宣告陣列bytesTemp2,容量為(PACKAGE_LEN - 4)
                                    //在bufTemp2使用方法bufTemp.readBytes()把值回傳存給bytesTemp2陣列
                                    if (bytesTemp1[0] == (byte) 0x5a && bytesTemp1[1] == (byte) 0x25 && bytesTemp1[2] == (byte) 0x06) {
                                        ByteBuf bufTemp2 = buffer.readBytes(PACKAGE_LEN - 4);
                                        byte[] bytesTemp2 = new byte[PACKAGE_LEN - 4];
                                        bufTemp2.readBytes(bytesTemp2);

                                        //宣告變數sum,存入的資料為11筆的加總和
                                        //假如陣列bytesTemp2最後一筆資料不等於sum則
                                        //使用方法buffer.resetReaderIndex()
                                        //把writeIndex設置為markWriteIndex的值
                                        //當陣列bytesTemp2最後一筆資料不等於sum時就繼續迴圈if
                                        byte sum = (byte) (0x5a + 0x5a + 0x25 + 0x06 + bytesTemp2[0] + bytesTemp2[1] + bytesTemp2[2] + bytesTemp2[3] + bytesTemp2[4] + bytesTemp2[5]);
                                        if (bytesTemp2[bytesTemp2.length - 1] != sum) {
                                            buffer.resetReaderIndex();
                                            continue;
                                        }

                                        //宣告陣列bytesTemp3,容量為PACKAGE_LEN
                                        //指定bytesTemp3[0]為0x5a ... 指定bytesTemp3[3]為0x06
                                        //使用方法System.arraycopy()將陣列bytesTemp2複製到bytesTemp3
                                        //System.arraycopy(來源陣列，起始索引值，目的陣列，起始索引值，複製長度);
                                        //宣告不可變的陣列finalBytes,存入陣列bytesTemp3
                                        byte[] bytesTemp3 = new byte[PACKAGE_LEN];
                                        bytesTemp3[0] = (byte) 0x5a;
                                        bytesTemp3[1] = (byte) 0x5a;
                                        bytesTemp3[2] = (byte) 0x25;
                                        bytesTemp3[3] = (byte) 0x06;
                                        System.arraycopy(bytesTemp2, 0, bytesTemp3, 4, bytesTemp2.length);
                                        final byte[] finalBytes = bytesTemp3;

                                        //將陣列finalBytes不斷更新顯示在mReceiveTextView
                                        //每0.1秒更新送出一次
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mReceiveTextView.append(byteArrayToHexString(finalBytes) + "\r\n");
                                                try {
                                                    sleep(100);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });

//                                        synchronized (byteListInbuf) {
//                                            byteListInbuf.add(bytesTemp3);
//
//                                        }
                                        buffer.discardReadBytes();

                                    } else {
                                        buffer.resetReaderIndex();
                                        continue;
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onDataSent(byte[] bytes) {
                        Log.i(TAG, "onDataSent [ byte[] ]: " + Arrays.toString(bytes));
                        Log.i(TAG, "onDataSent [ String ]: " + new String(bytes));
                        final byte[] finalBytes = bytes;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG, "send\n%s" + new String(finalBytes));

                                showToast(String.format("发送\n%s", new String(finalBytes)));
                            }
                        });
                    }
                })
                .openSerialPort(device.getFile(), 9600);


        Log.i(TAG, "onCreate: openSerialPort = " + openSerialPort);
    }


    public static String byteArrayToHexString(final byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        //for()從bytes陣列中,從陣列第一個值取到最後一個值
        //append()將字串接在字串的最後方
        //String.format("%02X",i)取兩位數的16進制
        //0x表示是16進制,ff是兩個16進制的數
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        if (null != mSerialPortManager) {
            mSerialPortManager.closeSerialPort();
            mSerialPortManager = null;
        }
        super.onDestroy();
    }

    /**
     * 串口打开成功
     *
     * @param device 串口
     */
    @Override
    public void onSuccess(File device) {
        Toast.makeText(getApplicationContext(), String.format("串口 [%s] 打开成功", device.getPath()), Toast.LENGTH_SHORT).show();


    }

    /**
     * 串口打开失败
     *
     * @param device 串口
     * @param status status
     */
    @Override
    public void onFail(File device, Status status) {
        switch (status) {
            case NO_READ_WRITE_PERMISSION:
                showDialog(device.getPath(), "没有读写权限");
                break;
            case OPEN_FAIL:
            default:
                showDialog(device.getPath(), "串口打开失败");
                break;
        }
    }

    /**
     * 显示提示框
     *
     * @param title   title
     * @param message message
     */
    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    /**
     * 发送数据
     *
     * @param view view
     */
    public void onSend(View view) {
        byte[] cmdBytes = new byte[3];
        cmdBytes[0] = (byte) 0xA5;
        cmdBytes[1] = (byte) 0x6A;
        cmdBytes[2] = (byte) 0x0F;

        mSerialPortManager.sendBytes(cmdBytes);

        cmdBytes = new byte[3];
        cmdBytes[0] = (byte) 0xA5;
        cmdBytes[1] = (byte) 0x82;
        cmdBytes[2] = (byte) 0x27;

        mSerialPortManager.sendBytes(cmdBytes);
//        EditText editTextSendContent = (EditText) findViewById(R.id.et_send_content);
//        if (null == editTextSendContent) {
//            return;
//        }
//        String sendContent = editTextSendContent.getText().toString().trim();
//        if (TextUtils.isEmpty(sendContent)) {
//            Log.i(TAG, "onSend: 发送内容为 null");
//            return;
//        }
//
//        byte[] sendContentBytes = sendContent.getBytes();
//
//        boolean sendBytes = mSerialPortManager.sendBytes(sendContentBytes);
//        Log.i(TAG, "onSend: sendBytes = " + sendBytes);
//        showToast(sendBytes ? "发送成功" : "发送失败");
    }

    private Toast mToast;

    /**
     * Toast
     *
     * @param content content
     */
    private void showToast(String content) {
        if (null == mToast) {
            mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
        }
        mToast.setText(content);
        mToast.show();
    }
}
