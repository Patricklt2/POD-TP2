package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class Resolutions implements DataSerializable {
    private String agency;
    private String status;
    private int year;
    private int month;

    public Resolutions() {}

    //chusmear YearMonth (recomendado al fondo de la consigna)
    public Resolutions(String agency, String status, LocalDateTime resolutionDate) {
        this.agency = agency;
        this.status = status;
        this.year = resolutionDate.getYear();
        this.month = resolutionDate.getMonthValue();
    }

    public String getAgency() {
        return agency;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(agency);
        out.writeUTF(status);
        out.writeInt(year);
        out.writeInt(month);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        agency = in.readUTF();
        status = in.readUTF();
        year = in.readInt();
        month = in.readInt();
    }

    @Override
    public int hashCode() {
        return Objects.hash(agency, status, year, month);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Resolutions that &&
                agency.equals(that.getAgency()) &&
                status.equals(that.getStatus()) &&
                year == that.getYear() &&
                month == that.getMonth();
    }
}
