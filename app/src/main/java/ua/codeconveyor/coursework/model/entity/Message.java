package ua.codeconveyor.coursework.model.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Message implements Parcelable {


    public int getMessageSize(String chanType) {

        return messageSize ;
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

        messageTime = pocketTime * (infoPocketCounter + getSpecialPocketCounter(chanType));

        return messageTime;
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
        return 0.02 * nodeCounter;
    }

    public int getHeaderSize(String chanType) {
        if (chanType.equals("MSG")) {
            return headerSize;
        }
        return headerSize * pocketCounter;

    }

    private int headerSize = 8;

    public double getHeaderTime(String chanType) {
        if (chanType.equals("MSG")) {
            return headerTime;
        }
        return headerTime * pocketCounter;
    }

    private double headerTime;

    public int getInfoPocketCounter() {
        return infoPocketCounter;
    }

    private int infoPocketCounter;

    public int getSpecialPocketCounter(String chanType) {
        if (chanType.equals("MSG")) {
            specialPocketCounter = infoPocketCounter + 2 * (nodeCounter - 1);
            return specialPocketCounter;
        }
        return infoPocketCounter;

    }

    private int specialPocketCounter;


    public Message(int edgeWeight) {
        Random random = new Random();
        pocketSize = (int) Math.pow(2, random.nextInt(10));
        pocketCounter = random.nextInt(10) + 1;
        infoPocketCounter = pocketCounter;
        messageSize = pocketSize * infoPocketCounter;
        pocketTime = (double) edgeWeight / 10;
        messageTime = pocketTime * pocketCounter;
        headerTime = messageTime / 10;
        messageDistance = edgeWeight;
    }

    protected Message(Parcel in) {
        messageSize = in.readInt();
        pocketSize = in.readInt();
        pocketCounter = in.readInt();
        pocketTime = in.readDouble();
        messageTime = in.readDouble();
        messageDistance = in.readInt();
        headerSize = in.readInt();
        headerTime = in.readDouble();
        nodeCounter = in.readInt();
        specialPocketCounter = in.readInt();
        infoPocketCounter = in.readInt();
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
        dest.writeInt(specialPocketCounter);
        dest.writeInt(infoPocketCounter);
    }
}
