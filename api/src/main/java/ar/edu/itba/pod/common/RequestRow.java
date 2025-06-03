package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;

public abstract class RequestRow {
    private LocalDateTime createdDate;
    private String status;
    private String neighbourhood;
    private String complaintType;
    private String agency;
    private GeoLocation geoLocation;

    // Constructor for shared fields
    protected RequestRow(LocalDateTime createdDate, String status, String neighbourhood, GeoLocation geoLocation, String complaintType, String agency) {
        this.createdDate = createdDate;
        this.status = status;
        this.neighbourhood = neighbourhood;
        this.complaintType = complaintType;
        this.agency = agency;
        this.geoLocation = geoLocation;
    }

    public RequestRow() {}

    public LocalDateTime getCreatedDate() { return createdDate; }
    public String getStatus() { return status; }
    public String getNeighbourhood() { return neighbourhood; }
    public String getComplaintType() { return complaintType; }
    public String getAgency() { return agency; }
    public GeoLocation getGeoLocation() { return geoLocation; }

    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public void setStatus(String status) { this.status = status; }
    public void setNeighbourhood(String neighbourhood) { this.neighbourhood = neighbourhood; }
    public void setComplaintType(String complaintType) { this.complaintType = complaintType; }
    public void setAgency(String agency) { this.agency = agency; }
    public void setGeoLocation(GeoLocation geoLocation) { this.geoLocation = geoLocation; }
}
