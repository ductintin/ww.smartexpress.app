package ww.smartexpress.app.data.model.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookLocation implements Parcelable {
    private SearchLocation origin;
    private SearchLocation destination;
    private Double originLat;
    private Double originLng;
    private Double desLat;
    private Double desLng;
    private Long distance;
    private String originLatLng;
    private String desLatLng;

    protected BookLocation(Parcel in) {
        if (in.readByte() == 0) {
            originLat = null;
        } else {
            originLat = in.readDouble();
        }
        if (in.readByte() == 0) {
            originLng = null;
        } else {
            originLng = in.readDouble();
        }
        if (in.readByte() == 0) {
            desLat = null;
        } else {
            desLat = in.readDouble();
        }
        if (in.readByte() == 0) {
            desLng = null;
        } else {
            desLng = in.readDouble();
        }
        if (in.readByte() == 0) {
            distance = null;
        } else {
            distance = in.readLong();
        }
        originLatLng = in.readString();
        desLatLng = in.readString();
    }

    public static final Creator<BookLocation> CREATOR = new Creator<BookLocation>() {
        @Override
        public BookLocation createFromParcel(Parcel in) {
            return new BookLocation(in);
        }

        @Override
        public BookLocation[] newArray(int size) {
            return new BookLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (originLat == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(originLat);
        }
        if (originLng == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(originLng);
        }
        if (desLat == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(desLat);
        }
        if (desLng == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(desLng);
        }
        if (distance == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(distance);
        }
        parcel.writeString(originLatLng);
        parcel.writeString(desLatLng);
    }
}
