package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;

public class RequestsNYCRow implements DataSerializable {
    private int uniqueKey;
    private LocalDateTime createdDate;
    private String agencyName;
    private String complaintType;  // Maybe could be turned into enum or special type
    private String incidentAddress;
    private String status;  // Maybe could be turned into enum or special type
    private String borough;

    //----------------------------------- [
    private double latitude;
    private double longitude;
    //----------------------------------- ] --> Could be turned to geoLocation class
    public RequestsNYCRow(int uniqueKey, LocalDateTime createdDate, String agencyName, String complaintType,
                          String incidentAddress, String status, String borough, double latitude, double longitude) {
        this.uniqueKey = uniqueKey;
        this.createdDate = createdDate;
        this.agencyName = agencyName;
        this.complaintType = complaintType;
        this.incidentAddress = incidentAddress;
        this.status = status;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
            out.writeInt(uniqueKey);
            out.writeObject(createdDate);
            out.writeUTF(agencyName);
            out.writeUTF(complaintType);
            out.writeUTF(incidentAddress);
            out.writeUTF(status);
            out.writeUTF(borough);
            out.writeDouble(latitude);
            out.writeDouble(longitude);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
            uniqueKey = in.readInt();
            createdDate = in.readObject();
            agencyName = in.readUTF();
            complaintType = in.readUTF();
            incidentAddress = in.readUTF();
            status = in.readUTF();
            borough = in.readUTF();
            latitude = in.readDouble();
            longitude = in.readDouble();
    }

    public int getUniqueKey() {
        return uniqueKey;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public String getStatus() {
        return status;
    }

    public String getBorough() {
        return borough;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getIncidentAddress(){
        return incidentAddress;
    }
}
