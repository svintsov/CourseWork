package ua.codeconveyor.coursework.Presenter;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ua.codeconveyor.coursework.R;
import ua.codeconveyor.coursework.model.entity.Message;
import ua.codeconveyor.coursework.model.entity.Node;

public class PathActivity extends AppCompatActivity {

    private Node[] nodes;
    private Message message;
    TableLayout pathTable;
    TableLayout messageTable;
    TableRow firstRowPath;
    TableRow secondRowPath;
    TableRow thirdRowPath;
    TableRow fourthRowPath;

    TableRow firstRowMessage;
    TableRow secondRowMessage;


    private String channelMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        pathTable = (TableLayout) findViewById(R.id.path_table_id);
        pathTable.setHorizontalScrollBarEnabled(true);
        messageTable = (TableLayout)findViewById(R.id.message_table_id);
        messageTable.setHorizontalScrollBarEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        Parcelable[] parcelableNodes = bundle.
                getParcelableArray("PATH_CONTAINER");

        channelMode = bundle.getString("CHANNEL_MODE");

        message = (Message) bundle.get("MESSAGE_CONTAINER");
        if (parcelableNodes != null && parcelableNodes.length > 0) {
            nodes = new Node[parcelableNodes.length];
            System.arraycopy(parcelableNodes, 0, nodes, 0, parcelableNodes.length);
            fillPathTable();

        }

        initMessage();
    }

    private void fillPathTable(){
        initPath();
        for (Node node : nodes) {
            if (node.getRegion().equals("Region1")) {
                secondRowPath.addView(createSimpleColumn(String.valueOf(node.getId())));
                thirdRowPath.addView(createSimpleColumn(""));
                fourthRowPath.addView(createSimpleColumn(""));
            }
            if (node.getRegion().equals("Region2")) {
                thirdRowPath.addView(createSimpleColumn(String.valueOf(node.getId())));
                secondRowPath.addView(createSimpleColumn(""));
                fourthRowPath.addView(createSimpleColumn(""));
            }
            if (node.getRegion().equals("Region3")) {
                fourthRowPath.addView(createSimpleColumn(String.valueOf(node.getId())));
                thirdRowPath.addView(createSimpleColumn(""));
                secondRowPath.addView(createSimpleColumn(""));
            }
        }

        pathTable.addView(secondRowPath);
        pathTable.addView(thirdRowPath);
        pathTable.addView(fourthRowPath);
    }


    private TextView createSimpleColumn(String str) {
        TextView column = new TextView(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(100,100);
        layoutParams.setMargins(4,4,4,4);
        column.setLayoutParams(layoutParams);
        column.setText(str);
        return column;
    }

    private void initMessage(){
        firstRowMessage = new TableRow(this);
        secondRowMessage = new TableRow(this);
        firstRowMessage.addView(createMainColumnMSG("Message size"));
        firstRowMessage.addView(createMainColumnMSG("Data pocket N "));
        firstRowMessage.addView(createMainColumnMSG("Manage pocket N "));
        firstRowMessage.addView(createMainColumnMSG("Message time"));

        secondRowMessage.addView(createSimpleColumnMSG(String.valueOf(message.getMessageSize(channelMode))));
        secondRowMessage.addView(createSimpleColumnMSG(String.valueOf(message.getInfoPocketCounter())));
        secondRowMessage.addView(createSimpleColumnMSG(String.valueOf(message.getSpecialPocketCounter(channelMode))));
        secondRowMessage.addView(createSimpleColumnMSG(String.valueOf(message.getMessageTime(channelMode))));

        messageTable.addView(firstRowMessage);
        messageTable.addView(secondRowMessage);
    }

    private void initPath() {
        secondRowPath = new TableRow(this);
        secondRowPath.addView(createMainColumn("SA1"));
        thirdRowPath = new TableRow(this);
        thirdRowPath.addView((createMainColumn("SA2")));
        fourthRowPath = new TableRow(this);
        fourthRowPath.addView(createMainColumn("SA3"));
    }

    private TextView createSimpleColumnMSG(String str) {
        TextView column = new TextView(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(200,100);
        layoutParams.setMargins(4,4,4,4);
        column.setLayoutParams(layoutParams);
        column.setText(str);
        return column;
    }

    private TextView createMainColumnMSG(String str) {
        TextView column = new TextView(this);
        column.setText(str);
        column.setBackgroundColor(Color.rgb(180, 180, 180));
        column.setTextColor(Color.rgb(0, 127, 255));
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(200, 100);
        layoutParams.setMargins(4, 4, 4, 4);
        column.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            column.setElevation(15);
        }
        return column;
    }


    private TextView createMainColumn(String str) {
        TextView column = new TextView(this);
        column.setText(str);
        column.setBackgroundColor(Color.rgb(180, 180, 180));
        column.setTextColor(Color.rgb(0, 127, 255));
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(100, 100);
        layoutParams.setMargins(4, 4, 4, 4);
        column.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            column.setElevation(15);
        }
        return column;
    }

}
