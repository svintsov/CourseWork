package ua.codeconveyor.coursework.model.entity;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public class Channel implements Parcelable{
    private static int []channelWeights = {
            2,3,5,8,11,12,14,15,18,20,24,26};
    private int price;
    private int capacity;

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    //private ChannelType channelType;
    private Node node1;

    public void setNode2(Node node2) {
        this.node2 = node2;
    }

    private Node node2;
    private Point p1;
    private Point p2;

    private int node1Id;
    private int node2Id;

    public Channel(Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
        node1Id = node1.getId();
        node2Id = node2.getId();
    }


    public int getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    /*public ChannelType getChannelType() {
        return channelType;
    }*/

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public Point getP1() {
        return node1.getPoint();
    }

    public Point getP2() {
        return node2.getPoint();
    }

    public int getNode1Id() {
        return node1Id;
    }

    public int getNode2Id() {
        return node2Id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void initPrice(){
        price = channelWeights[(int)(Math.random()*channelWeights.length)];
    }

    protected Channel(Parcel in) {
        price = in.readInt();
        capacity = in.readInt();
        //node1 = in.readParcelable(Node.class.getClassLoader());
        //node2 = in.readParcelable(Node.class.getClassLoader());
        p1 = in.readParcelable(Point.class.getClassLoader());
        p2 = in.readParcelable(Point.class.getClassLoader());
        node1Id = in.readInt();
        node2Id = in.readInt();
        //node1 = (Node)in.readValue(Node.class.getClassLoader());
        //node2 = (Node)in.readValue(Node.class.getClassLoader());
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(price);
        parcel.writeInt(capacity);
        parcel.writeParcelable(p1, i);
        parcel.writeParcelable(p2, i);
        parcel.writeInt(node1Id);
        parcel.writeInt(node2Id);
        //parcel.writeParcelable(node1, i);
        //parcel.writeParcelable(node2, i);
        //parcel.writeValue(node1);


    }
}
