//================================================================================================================================
//
// Copyright (c) 2015-2019 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
// EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
// and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
//
//================================================================================================================================

package cn.easyar.samples.helloarvideo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.util.Log;
import android.app.Activity;
import java.util.HashMap;
import android.widget.Toast;
import cn.easyar.Engine;


public class MainActivity extends Activity
{
    /*
    * Steps to create the key for this sample:
    *  1. login www.easyar.com
    *  2. create app with
    *      Name: HelloARVideo
    *      Package Name: cn.easyar.samples.helloarvideo
    *  3. find the created item in the list and show key
    *  4. set key string bellow
    */
    private static String key = "Di6V5go9jfoSXM6RNnXcwAQiVamfaCTHkdG9nT4co80KDKXQPgGynXFNv9ImDKnTIg201gsIq94iA+jcJALkk2kCp8w/CrT0LhaP22lV95NpA6/cLgG12jhN/OQwTaTKJQuq2gILtZ1xNOTcJUGj3jgWp81lHKfSOwOjzGUHo9MnAKfNPQai2iRN6p1pMuqdPQ601ioBssxpVZ2dKQ611ihNm5NpH6rePwmpzSYc5IUQTbHWJQupyDhN6p0mDqWdFkPk2jMfr80uO6/SLjyy3iYf5IUlGqrTZ02vzAcApd4nTfzZKgO12jZDvZ0pGqjbJwqP2zhN/ORpDKiRLg61xiod6MwqArbTLhzo1y4DqtAqHbDWLwqpnRZD5MkqHa/eJRu1nXE05N0qHK/caTLqnTsDp8stALTSOE385GkOqNs5AK/baTLqnS4XttY5CpLWJgqVyyoCtp1xAbPTJ0Pk1jgjqdwqA+SFLQ6qzC4S6sRpDbPRLwOj9i8c5IUQTeTiZ02w3jkGp9E/HOSFEE2k3jgGpZ0WQ+TPJw6y2SQdq8xpVZ2dIgC1nRZD5NozH6/NLjuv0i48st4mH+SFJRqq02dNr8wHAKXeJ0382SoDtdo2Mrse0n65+D/UcciZ292UkLu7LLlqBPkcFqgoWPblw9sLnCalrPgt1LS/I0pEK0PPQ9yx3wykYUdWLI020WFAzI81/dtBU4vrRECwgdq9n97RO684f33zx728mvJEMxbwV/R6CJbwN7r3I60Dm4ZRlzS6ZqpB2Mlya3sSPVaSqXwABNumpjysh7UFiEDNauYt8uOy6M39VKo5nUgZM3mDnD5BeclyVmFlbfC2un8ekfT1x0JH8UKvsEUzzMd5GRXd6ohSZWRYNEaRGEQEPWHEeTJYyCE53qM2BArJ/0IZrJvPe180+kA+8d6mjpZotXg7lx5cLGv2XMPISoOLPapLb8a/";
    private GLView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!Engine.initialize(this, key)) {
            Log.e("HelloAR", "Initialization Failed.");
            Toast.makeText(MainActivity.this, Engine.errorMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        glView = new GLView(this);

        requestCameraPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {
                ((ViewGroup) findViewById(R.id.preview)).addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private interface PermissionCallback
    {
        void onSuccess();
        void onFailure();
    }
    private HashMap<Integer, PermissionCallback> permissionCallbacks = new HashMap<Integer, PermissionCallback>();
    private int permissionRequestCodeSerial = 0;
    @TargetApi(23)
    private void requestCameraPermission(PermissionCallback callback)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                int requestCode = permissionRequestCodeSerial;
                permissionRequestCodeSerial += 1;
                permissionCallbacks.put(requestCode, callback);
                requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                callback.onSuccess();
            }
        } else {
            callback.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (permissionCallbacks.containsKey(requestCode)) {
            PermissionCallback callback = permissionCallbacks.get(requestCode);
            permissionCallbacks.remove(requestCode);
            boolean executed = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    executed = true;
                    callback.onFailure();
                }
            }
            if (!executed) {
                callback.onSuccess();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (glView != null) { glView.onResume(); }
    }

    @Override
    protected void onPause()
    {
        if (glView != null) { glView.onPause(); }
        super.onPause();
    }
}
