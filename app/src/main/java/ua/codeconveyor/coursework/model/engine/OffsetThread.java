package ua.codeconveyor.coursework.model.engine;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import static android.content.ContentValues.TAG;


public class OffsetThread extends Thread {
    private static final String LOG_TAG = OffsetThread.class.getSimpleName();

    //флаг, указывающий на то, что игра запущена.
    private boolean running;

    private SurfaceHolder surfaceHolder;
    private SurfaceEngine surface;

    public OffsetThread(SurfaceHolder surfaceHolder, SurfaceEngine surface) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.surface = surface;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Starting game loop");
        while (running) {
            canvas = null;
            // пытаемся заблокировать canvas
            // для изменение картинки на поверхности
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    // здесь будет обновляться состояние игры
                    // и формироваться кадр для вывода на экран
                    this.surface.onDraw(canvas);//Вызываем метод для рисования
                }
            } finally {
                // в случае ошибки, плоскость не перешла в
                //требуемое состояние
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

    }

}

