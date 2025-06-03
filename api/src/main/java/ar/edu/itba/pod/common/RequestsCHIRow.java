package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;

public class RequestsCHIRow extends RequestRow implements DataSerializable {
    private String srNumber;
    private Address address;

    public RequestsCHIRow() {}

    public RequestsCHIRow(String srNumber, String srShortCode, String ownerDepartment, String status,
                          LocalDateTime createdDate, int streetNumber, char streetDirection, String streetName,
                          String streetType, String communityArea, double latitude, double longitude) {
        super(createdDate, status, communityArea, new GeoLocation(latitude, longitude), srShortCode, ownerDepartment);

        this.srNumber = srNumber;
        this.address = new Address(streetNumber, streetDirection, streetName, streetType);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(srNumber);
        out.writeUTF(getComplaintType());
        out.writeUTF(getAgency());
        out.writeUTF(getStatus());
        out.writeObject(getCreatedDate());
        out.writeInt(address.getStreetNumber());
        out.writeChar(address.getStreetDirection());
        out.writeUTF(address.getStreetName());
        out.writeUTF(address.getStreetType());
        out.writeUTF(getNeighbourhood());
        out.writeDouble(getGeoLocation().getLatitude());
        out.writeDouble(getGeoLocation().getLongitude());
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        srNumber = in.readUTF();
        setComplaintType(in.readUTF());
        setAgency(in.readUTF());
        setStatus(in.readUTF());
        setCreatedDate(in.readObject());

        int streetNumber = in.readInt();
        char streetDirection = in.readChar();
        String streetName = in.readUTF();
        String streetType = in.readUTF();
        address = new Address(streetNumber, streetDirection, streetName, streetType);

        setNeighbourhood(in.readUTF());

        double latitude = in.readDouble();
        double longitude = in.readDouble();
        setGeoLocation(new GeoLocation(latitude, longitude));
    }

    public String getSrNumber() {
        return srNumber;
    }

    public Address getAddress() {
        return address;
    }
}
