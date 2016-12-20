package ua.codeconveyor.coursework.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.Button;

import java.util.LinkedList;
import java.util.List;

import ua.codeconveyor.coursework.model.entity.Channel;
import ua.codeconveyor.coursework.model.entity.Node;


public class NodeView extends Button {
    private static final String LOG_TAG = NodeView.class.getSimpleName();
    private static int radius = 75;
    private static final int STROKE_WIDTH = 10;
    private Node node;
    private List<ChannelView> channelViewList;
    private Path path;
    private Paint paint;
    private Context context;
    private boolean touched;// переменная состояния

    public NodeView(Node node, Context context) {
        super(context);
        this.context = context;
        channelViewList = new LinkedList<>();
        this.node = node;
        path = new Path();
        paint = new Paint();
    }

    public void draw(Canvas canvas) {

        path.reset();
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(0,127,255));
        path.addCircle(node.getX(), node.getY(), radius, Path.Direction.CW);
        paint.setTextSize(70);
        canvas.drawPath(path, paint);
        path.close();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(String.valueOf(node.getId()),node.getX()-20,node.getY()+10,paint);
    }

    public boolean handleActionDown(int eventX, int eventY) {
        if (eventX >= (node.getX() - radius) &&
                (eventX <= (node.getX() + radius)) &&
                (eventY >= (node.getY() - radius)) &&
                (eventY <= (node.getY() + radius))) {

            touched = true;
        } else {
            touched = false;
        }
        return touched;
    }

    public Node getNode() {
        return node;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setTouched(boolean touched){
        this.touched = touched;
    }
    public boolean isTouched() {
        return touched;
    }

    public void addChannelWith(NodeView node, ChannelView.ChannelType channelType){
        Channel channelTo = this.node.addChannelTo(node.getNode());
        channelTo.initPrice();
        channelViewList.add(new ChannelView(channelTo,context,channelType));

        Channel channelFrom = node.getNode().addChannelTo(this.node);
        channelFrom.setPrice(channelTo.getPrice());
        node.getChannelViewList().add(new ChannelView(channelFrom,context,channelType));
    }

    public void removeChannelWith(NodeView node){
        Channel chan = this.node.removeChannelWith(node.getNode());
        for (ChannelView ch:channelViewList) {
            if(ch.getChannel().equals(chan)){
                channelViewList.remove(ch);
                break;
            }
        }
    }
    public List<ChannelView> getChannelViewList() {
        return channelViewList;
    }
    public void setX(int x){
        node.setX(x);
    }
    public void setY(int y){
        node.setY(y);
    }
}
