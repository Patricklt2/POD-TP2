package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;

public class RequestsCHIRow implements DataSerializable {
    private String srNumber;
    private String srShortCode;
    private String ownerDepartment;
    private String status;
    private LocalDateTime createdDate;
    //----------------------------------- [
    private int streetNumber;
    private char streetDirection;
    private String streetName;
    private String streetType;
    private String communityArea;
    //-----------------------------------  ] --> Could be turned into address class [
    private double latitude;
    private double longitude;
    //----------------------------------- ] --> Could be turned to geoLocation class
    public RequestsCHIRow(String srNumber, String srShortCode, String ownerDepartment, String status,
                          LocalDateTime createdDate, int streetNumber, char streetDirection, String streetName,
                          String streetType, String communityArea, double latitude, double longitude) {
        this.srNumber = srNumber;
        this.srShortCode = srShortCode;
        this.ownerDepartment = ownerDepartment;
        this.status = status;
        this.createdDate = createdDate;
        this.streetNumber = streetNumber;
        this.streetDirection = streetDirection;
        this.streetName = streetName;
        this.streetType = streetType;
        this.communityArea = communityArea;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(srNumber);
        out.writeUTF(srShortCode);
        out.writeUTF(ownerDepartment);
        out.writeUTF(status);
        out.writeObject(createdDate);
        out.writeInt(streetNumber);
        out.writeChar(streetDirection);
        out.writeUTF(streetName);
        out.writeUTF(streetType);
        out.writeUTF(communityArea);
        out.writeDouble(latitude);
        out.writeDouble(longitude);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        srNumber = in.readUTF();
        srShortCode = in.readUTF();
        ownerDepartment = in.readUTF();
        status = in.readUTF();
        createdDate = in.readObject();
        streetNumber = in.readInt();
        streetDirection = in.readChar();
        streetName = in.readUTF();
        streetType = in.readUTF();
        communityArea = in.readUTF();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public String getSrNumber() {
        return srNumber;
    }

    public String getSrShortCode() {
        return srShortCode;
    }

    public String getOwnerDepartment() {
        return ownerDepartment;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public char getStreetDirection() {
        return streetDirection;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetType() {
        return streetType;
    }

    public String getCommunityArea() {
        return communityArea;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
