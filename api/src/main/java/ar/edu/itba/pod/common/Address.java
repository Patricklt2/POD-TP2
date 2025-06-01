package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

//Might not need to be serializable, will keep it like that for now
public class Address implements DataSerializable {
    private int streetNumber;
    private char streetDirection;
    private String streetName;
    private String streetType;

    public Address(int streetNumber, char streetDirection, String streetName, String streetType) {
        this.streetNumber = streetNumber;
        this.streetDirection = streetDirection;
        this.streetName = streetName;
        this.streetType = streetType;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(streetNumber);
        out.writeChar(streetDirection);
        out.writeUTF(streetName);
        out.writeUTF(streetType);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        streetNumber = in.readInt();
        streetDirection = in.readChar();
        streetName = in.readUTF();
        streetType = in.readUTF();
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

    @Override
    public int hashCode() {
        return Objects.hash(streetNumber, streetDirection, streetName, streetType);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Address that &&
                this.streetNumber == that.streetNumber &&
                this.streetDirection == that.streetDirection &&
                this.streetName.equals(that.streetName) &&
                this.streetType.equals(that.streetType);
    }
}