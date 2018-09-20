package com.yuan.posprint;

import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yuan.posprint.print.PrintHelper;
import com.yuan.posprint.usb.USBHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.ll_print);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_print:
                USBHelper.getInstance(this).sendData(PrintHelper.initPrinter());
                linearLayout.setDrawingCacheEnabled(true);
                linearLayout.buildDrawingCache();
                final Bitmap bmp = linearLayout.getDrawingCache(); // 获取图片

                ArrayList<byte[]> list = PrintHelper.decodeBitmapToDataList(bmp, 255);
                for (int i = 0; i < list.size(); i++) {
                    USBHelper.getInstance(this).sendData(list.get(i));
                }
                //切纸
                USBHelper.getInstance(this).sendData(PrintHelper.feedPaperCut());
                linearLayout.destroyDrawingCache();
                break;
            case R.id.btn_usb:
                List<UsbDevice> usblist = USBHelper.getInstance(this).getUsbDevices();
                for (UsbDevice device : usblist) {
                    int result = USBHelper.getInstance(this).connection(device.getVendorId(), device.getProductId());
                    Toast.makeText(this, "连接返回:" + result, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
