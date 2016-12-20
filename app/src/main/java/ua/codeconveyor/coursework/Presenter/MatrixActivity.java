package ua.codeconveyor.coursework.Presenter;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ua.codeconveyor.coursework.R;
import ua.codeconveyor.coursework.model.entity.Channel;
import ua.codeconveyor.coursework.model.entity.Node;


public class MatrixActivity extends AppCompatActivity {
    TableLayout matrixTable;
    ScrollView scrollView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matrix_layout);
        matrixTable = (TableLayout) findViewById(R.id.matrix_table_id);
        matrixTable.setHorizontalScrollBarEnabled(true);
        // Getting all nodes with channels from previous intent.
        Bundle bundle = getIntent().getExtras();
        Parcelable[] parcelableNodes = bundle.
                getParcelableArray(MainActivity.NODES_CONTAINER_FOR_MATRIX_INTENT_KEY);

        if(parcelableNodes != null && parcelableNodes.length > 0) {
            Node[] nodes = new Node[parcelableNodes.length];
            System.arraycopy(parcelableNodes, 0, nodes, 0, parcelableNodes.length);
            TableRow firstRow = new TableRow(this);
            firstRow.setOrientation(LinearLayout.HORIZONTAL);
            firstRow.addView(createMainColumn("-"));
            for (int i = 0; i < nodes.length; i++) {
                firstRow.addView(createMainColumn(String.valueOf(i)));
            }
            matrixTable.addView(firstRow);
            //pathTable.addView(firstRow);
            for (int i = 0; i < nodes.length; i++) {
                TableRow tableRow = new TableRow(this);
                tableRow.setOrientation(LinearLayout.HORIZONTAL);
                tableRow.addView(createMainColumn(String.valueOf(i)));
                //pathTable.addView(tableRow);

                for (int j = 0; j < nodes.length; j++) {
                    TextView column = createSimpleColumn();
                    if (nodes[i].getId() == j) {
                        column.setText("0");
                    } else {
                        for (Channel channel : nodes[i].getChannels()) {
                            if (channel.getNode2Id() == j) {
                                column.setText(String.valueOf(channel.getPrice()));
                                break;
                            }
                        }
                        if (column.getText().length() == 0) {
                            column.setText("*");
                        }
                    }
                    tableRow.addView(column);
                }
                matrixTable.addView(tableRow);
            }
        } else {
            matrixTable.setVisibility(View.GONE);
            //findViewById(R.id.empty_grid_id).setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    private TextView createSimpleColumn() {
        TextView column = new TextView(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(100,100);
        layoutParams.setMargins(4,4,4,4);
        column.setLayoutParams(layoutParams);
        return column;
    }

    @NonNull
    private TextView createMainColumn(String str) {
        TextView column = new TextView(this);
        column.setText(str);
        column.setBackgroundColor(Color.rgb(180,180,180));
        column.setTextColor(Color.rgb(0,127,255));
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(100,100);
        layoutParams.setMargins(4,4,4,4);
        column.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            column.setElevation(15);
        }
        return column;
    }


}
