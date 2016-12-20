package ua.codeconveyor.coursework.model.entity;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Node implements Parcelable {
    private int id = 0;
    private Point point;
    private List<Channel> channels;
    private String region;

    public Node(Point point,String region) {
        this.point = point;
        channels = new LinkedList<>();
        id++;
        this.region=region;
    }

    public Channel isHaveCommonEdge(Node endNode){
        for (Channel channel:channels){
            if (channel.getNode2Id()==endNode.getId()){
                return channel;
            }
        }
        return null;
    }

    public Channel addChannelTo(Node node) {
        Channel result = new Channel(this, node);
        channels.add(result);
        return result;
    }

    public Channel removeChannelWith(Node node) {
        Channel channel = null;
        for (Channel chan : channels) {
            if (chan.getNode2().equals(node)) {
                channel = chan;
                channels.remove(chan);
                break;
            }
        }
        return channel;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Point getPoint() {
        return point;
    }

    public void setX(int x) {
        this.point.x = x;
    }

    public void setY(int y) {
        this.point.y = y;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    protected Node(Parcel in) {
        this.point = new Point();
        this.id = in.readInt();
        this.region=in.readString();
        this.point.x = in.readInt();
        this.point.y = in.readInt();
//        channels = Arrays.asList((Channel[]) in.readArray(Channel.class.getClassLoader()));
        channels = new LinkedList<>();
        channels = Arrays.asList(in.createTypedArray(Channel.CREATOR));
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(region);
        parcel.writeInt(point.x);
        parcel.writeInt(point.y);
        parcel.writeTypedList(channels);

        //parcel.writeList(channels);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
