package ua.codeconveyor.coursework.model.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Message implements Parcelable{


    public int getMessageSize(String chanType) {
        if (chanType.equals("MSG")){
            return messageSize+headerSize;
        }
        return messageSize+headerSize*pocketCounter;
    }

    private int messageSize;

    public int getPocketSize() {
        return pocketSize;
    }

    private int pocketSize;

    public int getPocketCounter() {
        return pocketCounter;
    }

    private int pocketCounter;

    public double getPocketTime() {
        return pocketTime;
    }

    private double pocketTime;

    public double getMessageTime(String chanType) {
        return messageTime + getNodesDelayTime() + getHeaderTime(chanType);
    }

    private double messageTime;

    public int getMessageDistance() {
        return messageDistance;
    }

    private int messageDistance;

    public int getNodeCounter() {
        return nodeCounter;
    }

    public void setNodeCounter(int nodeCounter) {
        this.nodeCounter = nodeCounter;
    }

    private int nodeCounter;

    public double getNodesDelayTime() {
        return 0.02*nodeCounter;
    }

    public int getHeaderSize(String chanType) {
        if (chanType.equals("MSG")){
            return headerSize;
        }
        return headerSize*pocketCounter;

    }

    private int headerSize=8;

    public double getHeaderTime(String chanType) {
        if (chanType.equals("MSG")){
            return headerTime;
        }
        return headerTime*pocketCounter;
    }

    private double headerTime;



    public Message(int edgeWeight){
        Random random = new Random();
        pocketSize=(int)Math.pow(2,random.nextInt(5));
        pocketCounter=random.nextInt(10)+1;
        messageSize = pocketSize*pocketCounter;
        pocketTime=(double)edgeWeight/10;
        messageTime=pocketTime*pocketCounter;
        headerTime=messageTime/10;
        messageDistance=edgeWeight;
    }

    protected Message(Parcel in) {
        messageSize=in.readInt();
        pocketSize=in.readInt();
        pocketCounter=in.readInt();
        pocketTime=in.readDouble();
        messageTime=in.readDouble();
        messageDistance=in.readInt();
        headerSize=in.readInt();
        headerTime=in.readDouble();
        nodeCounter=in.readInt();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(messageSize);
        dest.writeInt(pocketSize);
        dest.writeInt(pocketCounter);
        dest.writeDouble(pocketTime);
        dest.writeDouble(messageTime);
        dest.writeInt(messageDistance);
        dest.writeInt(headerSize);
        dest.writeDouble(headerTime);
        dest.writeInt(nodeCounter);
    }
}
