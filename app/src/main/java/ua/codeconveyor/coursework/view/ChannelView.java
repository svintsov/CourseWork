package ua.codeconveyor.coursework.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.View;

import ua.codeconveyor.coursework.model.entity.Channel;

public class ChannelView extends View {
    private Channel channel;
    private Path path;
    private Paint paint;
    private ChannelType channelType;

    public enum ChannelType{NORMAL, SATELLITE}

    public ChannelView(Channel channel, Context context,ChannelType channelType){
        super(context);
        this.channel = channel;
        this.channelType=channelType;
        path = new Path();
        paint = new Paint();
        if (channelType==ChannelType.NORMAL){
            paint.setColor(Color.BLACK);
        }
        else {
            paint.setColor(Color.RED);
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    private Point calculateMiddleOfVector(Point p1, Point p2){
        Point result = new Point();
        result.x = (p1.x + p2.x)/2;
        result.y = (p1.y + p2.y)/2;
        return result;
    }
    public void draw(Canvas canvas) {
        path.reset();
        path.moveTo(channel.getNode1().getX(),channel.getNode1().getY());
        path.lineTo(channel.getNode2().getX(),channel.getNode2().getY());
        //paint.setStyle(Paint.Style.FILL);
        //paint.setStrokeWidth(20);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //paint.setColor(Color.rgb(0,127,255));
        paint.setTextSize(70);
        Point middlePoint = calculateMiddleOfVector(channel.getP1(),channel.getP2());
        canvas.drawText(String.valueOf(channel.getPrice()),middlePoint.x,middlePoint.y,paint);
        canvas.drawPath(path,paint);
        //canvas.drawTextOnPath(String.valueOf(channel.getPrice()),path,0,0,paint);

    }

    public Channel getChannel() {
        return channel;
    }
}
