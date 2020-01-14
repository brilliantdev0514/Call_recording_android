package vn.harry.callrecorder.entity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Table(name = "Recording")
public class Recording extends Model implements Comparable<Recording>, Parcelable {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
    @Column(name = "date")
    private Date date;
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "userName")
    private String userName;
    @Column(name = "phoneNumber")
    public String phoneNumber;
    @Column(name = "uri")
    private String uri;
    @Column(name = "isOutGoing")
    private boolean isOutGoing;
    @Column(name = "isImportant")
    private boolean isImportant;
    @Column(name = "idCall")
    private String idCall;
    @Column(name = "isDelete")
    private boolean isDelete;
    @Column(name = "note")
    private String note;

    public Recording() {
        super();
    }

    public Recording(String fileName) {
        this.fileName = fileName;
        String dateStr = fileName.substring(0, 14);

        try {
            this.date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        phoneNumber = fileName.substring(15, fileName.indexOf('.'));
    }

    protected Recording(Parcel in) {
        fileName = in.readString();
        userName = in.readString();
        phoneNumber = in.readString();
        uri = in.readString();
        isOutGoing = in.readByte() != 0;
        isImportant = in.readByte() != 0;
        idCall = in.readString();
        isDelete = in.readByte() != 0;
    }

    public static final Creator<Recording> CREATOR = new Creator<Recording>() {
        @Override
        public Recording createFromParcel(Parcel in) {
            return new Recording(in);
        }

        @Override
        public Recording[] newArray(int size) {
            return new Recording[size];
        }
    };

    private ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", this.getDate().toString());
        contentValues.put("fileName", this.getFileName());
        contentValues.put("userName", this.getUserName());
        contentValues.put("uri", this.getUri());
        contentValues.put("phoneNumber", this.getPhoneNumber());
        return contentValues;
    }

    private ContentValues getContentValuesInsert() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", this.getDate().toString());
        contentValues.put("fileName", this.getFileName());
        contentValues.put("userName", this.getUserName());
        contentValues.put("uri", this.getUri());
        contentValues.put("phoneNumber", this.getPhoneNumber());
        contentValues.put("idCall", formatter.format(this.getDate()));
        contentValues.put("isOutGoing", this.isOutGoing());
        contentValues.put("isDelete", this.isDelete());
        contentValues.put("note", this.getNote());
        return contentValues;
    }

    public static void insertList(List<Recording> listDir) {
        SQLiteDatabase sqLiteDatabase = Cache.openDatabase();
        if (listDir.size() > 0) {
            sqLiteDatabase.beginTransaction();
            for (Recording recording : listDir) {
                Recording exist = Recording.findById(formatter.format(recording.getDate()));
                if (exist != null) {
                    sqLiteDatabase.update("Recording", recording.getContentValues(), "idCall=?", new String[]{formatter.format(recording.getDate()) + ""});
                } else {
                    sqLiteDatabase.insertWithOnConflict("Recording", null, recording.getContentValuesInsert(), 0);
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }
    }

    public static void deleteRecordList(List<Recording> listDir, int day) {
        Calendar cal = Calendar.getInstance();
        for (Recording recording : listDir) {
            if (recording != null && recording.getDate() != null) {
                long diffInMillisec = cal.getTime().getTime() - recording.getDate().getTime();
                if ((diffInMillisec / (24 * 60 * 60 * 1000)) <= day) {
                    removeRecording(formatter.format(recording.getDate()) + "");
                }
            }
        }
    }

    public static void refreshRecorder(List<Recording> listDir) {
        for (Recording recording : listDir) {
            Recording exist = Recording.findById(recording.getIdCall());
            if (exist != null) {
                exist.setDelete(false);
                exist.save();
            }
        }
    }

    private static void removeRecording(String idCall) {
        new Delete().from(Recording.class).where("idCall = ?", idCall).executeSingle();
    }

    public static Recording findById(String idCall) {
        return new Select().from(Recording.class).where("idCall = ?", idCall).executeSingle();
    }

    public static List<Recording> getAllItems() {
        return new Select().from(Recording.class).orderBy("Id DESC").execute();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getDate() {
        return date;
    }

    public int compareTo(@NonNull Recording other) {
        return date.compareTo(other.date);
    }

    public boolean isOutGoing() {
        return isOutGoing;
    }

    public void setOutGoing(boolean outGoing) {
        isOutGoing = outGoing;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdCall() {
        return idCall;
    }

    public void setIdCall(String idCall) {
        this.idCall = idCall;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fileName);
        parcel.writeString(userName);
        parcel.writeString(phoneNumber);
        parcel.writeString(uri);
        parcel.writeByte((byte) (isOutGoing ? 1 : 0));
        parcel.writeByte((byte) (isImportant ? 1 : 0));
        parcel.writeString(idCall);
        parcel.writeByte((byte) (isDelete ? 1 : 0));
    }
}
