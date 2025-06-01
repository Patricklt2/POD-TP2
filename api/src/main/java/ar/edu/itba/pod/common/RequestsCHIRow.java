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
    private Address address;
    private GeoLocation geoLocation;

    public RequestsCHIRow(String srNumber, String srShortCode, String ownerDepartment, String status,
                          LocalDateTime createdDate, int streetNumber, char streetDirection, String streetName,
                          String streetType, String communityArea, double latitude, double longitude) {
        this.srNumber = srNumber;
        this.srShortCode = srShortCode;
        this.ownerDepartment = ownerDepartment;
        this.status = status;
        this.createdDate = createdDate;
        this.address = new Address(streetNumber, streetDirection, streetName, streetType, communityArea);
        this.geoLocation = new GeoLocation(latitude, longitude);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(srNumber);
        out.writeUTF(srShortCode);
        out.writeUTF(ownerDepartment);
        out.writeUTF(status);
        out.writeObject(createdDate);
        out.writeInt(address.getStreetNumber());
        out.writeChar(address.getStreetDirection());
        out.writeUTF(address.getStreetName());
        out.writeUTF(address.getStreetType());
        out.writeUTF(address.getCommunityArea());
        out.writeDouble(geoLocation.getLatitude());
        out.writeDouble(geoLocation.getLongitude());
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        srNumber = in.readUTF();
        srShortCode = in.readUTF();
        ownerDepartment = in.readUTF();
        status = in.readUTF();
        createdDate = in.readObject();

        int streetNumber = in.readInt();
        char streetDirection = in.readChar();
        String streetName = in.readUTF();
        String streetType = in.readUTF();
        String communityArea = in.readUTF();

        address = new Address(streetNumber, streetDirection, streetName, streetType, communityArea);

        double latitude = in.readDouble();
        double longitude = in.readDouble();

        geoLocation = new GeoLocation(latitude, longitude);
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

    public Address getAddress() {
        return address;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }
}
