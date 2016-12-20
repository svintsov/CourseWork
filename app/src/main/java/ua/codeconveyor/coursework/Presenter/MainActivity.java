package ua.codeconveyor.coursework.Presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import ua.codeconveyor.coursework.R;
import ua.codeconveyor.coursework.model.engine.SurfaceEngine;
import ua.codeconveyor.coursework.view.ChannelView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyDialogFragment.NoticeDialogListener {
    private FrameLayout fl;
    public static final String NODES_CONTAINER_FOR_MATRIX_INTENT_KEY =
            "nodes_object_array";
    private ImageButton addBtn;
    private ImageButton delBtn;
    private ImageButton chanBtn;
    private SurfaceEngine engine;
    MyDialogFragment myDialogFragmentl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // перевод приложения в полноэкранный режим
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            engine = new SurfaceEngine(this);
            fl = (FrameLayout) findViewById(R.id.canvas_region);
            fl.addView(engine);

            addBtn = (ImageButton) findViewById(R.id.add_button);
            delBtn = (ImageButton) findViewById(R.id.remove_button);
            chanBtn = (ImageButton) findViewById(R.id.channel_button);
            addBtn.setOnClickListener(this);
            delBtn.setOnClickListener(this);
            chanBtn.setOnClickListener(this);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.matrix_id: {
                Intent intent = new Intent(this, MatrixActivity.class);

                intent.putExtra(NODES_CONTAINER_FOR_MATRIX_INTENT_KEY,
                        engine.getNodesContainer());
                startActivity(intent);
                break;
            }
            case R.id.message_id: {
                Intent intent = new Intent(this, MessageActivity.class);
                intent.putExtra(NODES_CONTAINER_FOR_MATRIX_INTENT_KEY,
                        engine.getNodesContainer());
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (engine != null) {
            engine.pauseThread();
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        engine.resumeThread();

    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_button:
                engine.setAdd(true);
                break;
            case R.id.remove_button:
                engine.setDel(true);
                break;
            case R.id.channel_button:
                myDialogFragmentl = new MyDialogFragment();
                myDialogFragmentl.show(getFragmentManager(), "channel type");
                //engine.setAddChannel(true);
                break;
        }
    }

    @Override
    public void onDialogClick(int position) {
        switch (position) {
            case 0: {
                engine.setAddChannel(true, ChannelView.ChannelType.NORMAL);
                break;
            }
            case 1: {
                engine.setAddChannel(true, ChannelView.ChannelType.SATELLITE);
            }

        }
    }
}
