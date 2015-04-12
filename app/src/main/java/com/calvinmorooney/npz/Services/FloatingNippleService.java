package com.calvinmorooney.npz.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.calvinmorooney.npz.R;

public class FloatingNippleService extends Service {

    private WindowManager windowManager;
    WindowManager.LayoutParams nippleParams;
    private ImageView floatingNippleImage;
    float deviceScreenHeight;

    @Override
    public void onCreate()
    {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        floatingNippleImage = new ImageView (this);
        floatingNippleImage.setImageResource (R.mipmap.nipple);

        nippleParams = new WindowManager.LayoutParams(
                200,
                200,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        nippleParams.gravity = Gravity.TOP | Gravity.LEFT;
        nippleParams.x = 0;
        nippleParams.y = 100;

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceScreenHeight = size.y;

        windowManager.addView(floatingNippleImage, nippleParams);

        setUpDrag ();
    }

    void setUpDrag ()
    {
        floatingNippleImage.setOnTouchListener(new View.OnTouchListener() {
            boolean shouldClose = false;
            private WindowManager.LayoutParams paramsF = nippleParams;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y = event.getRawY ();

                if (y > deviceScreenHeight - floatingNippleImage.getHeight ())
                {
                    shouldClose = true;
                }
                else
                {
                    shouldClose = false;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = paramsF.x;
                        initialY = paramsF.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (shouldClose)
                        {
                            stopService();
                        }
                         break;
                    case MotionEvent.ACTION_MOVE:
                        paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                        paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);

                        if (shouldClose)
                        {
                            floatingNippleImage.setBackgroundColor (Color.RED);
                        }
                        else
                        {
                            floatingNippleImage.setBackgroundColor(Color.TRANSPARENT);
                        }

                        windowManager.updateViewLayout(floatingNippleImage, paramsF);
                        break;
                }
                return false;
            }
        });
    }

    void stopService()
    {
        this.stopSelf ();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingNippleImage != null)
        {
            windowManager.removeView(floatingNippleImage);
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
