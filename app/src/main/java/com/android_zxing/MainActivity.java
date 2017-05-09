package com.android_zxing;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.android_zxing_utils.QRCodeUtil;

import java.io.File;


/**
 * 二维码生成
 */
public class MainActivity extends Activity {
    private Button createQRBtn;
    private CheckBox logo;
    private EditText contentET;
    private ImageView imageQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createQRBtn = (Button) findViewById(R.id.QRCodeBtn);
        logo = (CheckBox) findViewById(R.id.checkbox);
        contentET = (EditText) findViewById(R.id.contentET);
        imageQR = (ImageView) findViewById(R.id.QRImageView);

        createQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String filePath = getFileRoot(MainActivity.this) + File.separator
                        + "qr_" + System.currentTimeMillis() + ".jpg";

                //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        boolean success = QRCodeUtil.createQRImage(contentET.getText().toString().trim(), 800, 800,
                                logo.isChecked() ? BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher) : null,
                                filePath);

                        if (success) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageQR.setImageBitmap(BitmapFactory.decodeFile(filePath));
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }
}
