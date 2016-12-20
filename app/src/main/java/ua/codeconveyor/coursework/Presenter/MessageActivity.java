package ua.codeconveyor.coursework.Presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.NumberPicker;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ua.codeconveyor.coursework.R;
import ua.codeconveyor.coursework.model.engine.DijkstraPath;
import ua.codeconveyor.coursework.model.entity.Message;
import ua.codeconveyor.coursework.model.entity.Node;

import static ua.codeconveyor.coursework.Presenter.MainActivity.NODES_CONTAINER_FOR_MATRIX_INTENT_KEY;

public class MessageActivity extends AppCompatActivity {

    private NumberPicker numberPicker;
    private NumberPicker numberPicker2;
    private Node[] nodes;
    private Node startNode;
    private Node endNode;
    private DijkstraPath dijkstraPath;
    private Message message;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        setSupportActionBar(toolbar);

        setPickers();

        dijkstraPath = new DijkstraPath();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNode = findNodeById(numberPicker.getValue());
                endNode = findNodeById(numberPicker2.getValue());
                if ((startNode!=null)&&(endNode!=null)){
                    dijkstraPath.findPath(startNode,nodes);
                    Map<Node,Integer> distance = dijkstraPath.distance;
                    message = new Message(distance.get(endNode));
                    String strToToast = String.valueOf(distance.get(endNode));
                    Log.i("VALUE",strToToast);

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.msg_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.path_id: {
                Intent intent = new Intent(this,PathActivity.class);
                List<Node> lst = new LinkedList<Node>();
                lst= dijkstraPath.getPath(endNode);
                message.setNodeCounter(lst.size());
                Node[] path = lst.toArray(new Node[lst.size()]);
                if (checkBox.isChecked()){
                    intent.putExtra("CHANNEL_MODE","DAT");
                }
                else{
                    intent.putExtra("CHANNEL_MODE","MSG");
                }
                intent.putExtra("PATH_CONTAINER",
                        path);
                intent.putExtra("MESSAGE_CONTAINER",message);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void initCounters(NumberPicker numberPicker, Node[] nodes){
        String[] dataForPicker = new String[nodes.length];
        for (int i=0;i<nodes.length;i++){
            dataForPicker[i]=String.valueOf(nodes[i].getId());
        }
        numberPicker.setMaxValue(nodes.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(dataForPicker);

    }

    private Node findNodeById(int id){
        for (Node node:nodes){
            if (node.getId()==id)
                return  node;
        }
        return null;
    }

    private  void setPickers(){
        numberPicker = (NumberPicker)findViewById(R.id.numberPicker);
        numberPicker2 = (NumberPicker)findViewById(R.id.numberPicker2);
        Bundle bundle = getIntent().getExtras();
        Parcelable[] parcelableNodes = bundle.
                getParcelableArray(NODES_CONTAINER_FOR_MATRIX_INTENT_KEY);
        if(parcelableNodes != null && parcelableNodes.length > 0) {
            nodes = new Node[parcelableNodes.length];
            System.arraycopy(parcelableNodes, 0, nodes, 0, parcelableNodes.length);
            initCounters(numberPicker,nodes);
            initCounters(numberPicker2,nodes);
        }
    }

}
