package com.pea.du.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.pea.du.db.local.data.Contract;
import com.pea.du.db.local.methods.ReadMethods;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.pea.du.db.local.data.Contract.GuestEntry.ADDRESS;
import static com.pea.du.db.local.data.Contract.GuestEntry.ADDRESS_TABLE_NAME;
import static java.lang.Integer.parseInt;

public class Act implements Parcelable{
    private Integer id;
    private Integer serverId;
    private Date createDate;
    private User user;
    private StaticValue address;


    public Act() {
    }

    public Act(Integer serverId) {
        this.serverId = serverId;
    }

    public Act(Act act) {
        this.id = act.getId();
        this.serverId = act.getServerId();
        this.createDate = act.getCreateDate();
        this.user = act.getUser();
        this.address = act.getAddress();
    }

    public Act(User user, StaticValue address) {
        this.user = new User(user);
        this.address = new StaticValue(address);
    }

    public Act(Integer id, Integer serverId, Date createDate, User user, StaticValue address) {
        this.id = id;
        this.serverId = serverId;
        this.createDate = createDate;
        this.user = user;
        this.address = address;
    }

    public Act(Integer serverId, Date createDate, User user, StaticValue address) {
        this.serverId = serverId;
        this.createDate = createDate;
        this.user = user;
        this.address = address;
    }

    public Act(Integer serverId, Integer userId, Integer addressId) {
        this.serverId = serverId;
        this.user = new User(userId);
        this.address = new StaticValue(addressId, ADDRESS_TABLE_NAME, ADDRESS);
    }

    public Act(Parcel source) {
        final ClassLoader cl = getClass().getClassLoader();

        java.lang.Object[] data = source.readArray(cl);

        id = (Integer) data[0];
        serverId = (Integer) data[1];
        createDate = (Date) data[2];
        user = (User) data[3];
        address = (StaticValue) data[4];
    }

    public boolean isActInDB(Context context) {
        ArrayList<Act> actList = ReadMethods.getActs(context,
                Contract.GuestEntry.USER_ID + " = ? AND " + Contract.GuestEntry.ADDRESS_ID + " = ?",
                new String[]{user.getServerId().toString(), address.getServerId().toString()});

        if (actList.size()>0) {
            id = actList.get(0).getId();
            serverId = actList.get(0).getServerId();
            return true;
        }
        else
            return false;
    }

    public static Act fromStringList(List<String> list, Context context){
        Act act = new Act();

        act.setServerId(parseInt(list.get(0)));

        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        try {
            act.setCreateDate(format.parse(list.get(1)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        User user = User.getUserByServerId(context, parseInt(list.get(2)));
        act.setUser(user);

        StaticValue address = new StaticValue(ADDRESS_TABLE_NAME, ADDRESS);
        address.setServerId(parseInt(list.get(3)));
        address.getStaticById(context);
        act.setAddress(address);

        return act;
    }

    ///////////////////////////////////////Parcelable/////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        java.lang.Object[] array = new java.lang.Object[5];;
        array[0] = id;
        array[1] = serverId;
        array[2] = createDate;
        array[3] = user;
        array[4] = address;

        dest.writeArray(array);
    }

    public static final Parcelable.Creator<Act> CREATOR = new Parcelable.Creator<Act>() {

        @Override
        public Act createFromParcel(Parcel source) {
            return new Act(source);
        }

        @Override
        public Act[] newArray(int size) {
            return new Act[size];
        }
    };

/////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public String toString() {
        return "Act{" +
                "createDate=" + createDate +
                ", user=" + user +
                ", address=" + address +
                '}';
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Act act = (Act) o;

        if (user != null ? !user.equals(act.user) : act.user != null) return false;
        return address != null ? address.equals(act.address) : act.address == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StaticValue getAddress() {
        return address;
    }

    public void setAddress(StaticValue address) {
        this.address = new StaticValue(address);
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
