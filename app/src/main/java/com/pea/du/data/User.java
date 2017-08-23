package com.pea.du.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;
import com.pea.du.actyvities.defects.address.AddressActivity;
import com.pea.du.db.data.Contract;
import com.pea.du.db.methods.ReadMethods;
import com.pea.du.web.client.Controller;

import java.util.ArrayList;

import static com.pea.du.web.client.Contract.CHECK_USER;

public class User implements Parcelable {

    private Integer id;
    private Integer serverId;
    private String nickname;
    private String password;


    public User() {
    }

    public User(Integer id, String nickname, String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
    }

    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public User(User user){
        this.id = user.getId();
        this.serverId = user.getServerId();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
    }

    public User(Parcel source) {
        final ClassLoader cl = getClass().getClassLoader();

        Object[] data = source.readArray(cl);

        id = (Integer) data[0];
        serverId = (Integer) data[1];
        nickname = (String) data[2];
        password = (String) data[3];
    }


    public void isUserInDB(Context context) {
        ArrayList<User> userList = ReadMethods.getUsers(context,
                Contract.GuestEntry.NICKNAME + " = ? AND " + Contract.GuestEntry.PASSWORD + " = ?",
                new String[]{nickname, password});

        //Если пользователь есть в локальной базе
        if (userList.size() > 0) {
            id = userList.get(0).getId();
            serverId = userList.get(0).getServerId();

            Toast.makeText(context, "Авторизированный пользователь " +nickname, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, AddressActivity.class);
            intent.putExtra("User", this);

            context.startActivity(intent);
        } else {
            //Запрашиваем в базе сервера
            Controller controller = new Controller(context, CHECK_USER, nickname);
            controller.start();
        }
    }

    public static User getUserById(Context context, Integer userId){
        ArrayList<User> userList = ReadMethods.getUsers(context,
                Contract.GuestEntry._ID + " = ?",
                new String[]{userId.toString()});
        if (userList.size() > 0)
            return userList.get(0);
        else
            return null;
    }

    public static User getUserByServerId(Context context, Integer serverId){
        ArrayList<User> userList = ReadMethods.getUsers(context,
                Contract.GuestEntry.SERVER_ID + " = ?",
                new String[]{serverId.toString()});
        if (userList.size() > 0)
            return userList.get(0);
        else {
            User user = new User();
            user.setServerId(serverId);
            return user;
        }
    }

    ///////////////////////////////////////Parcelable/////////////////////////////////////////////////////////
    @Override
    public int describeContents() {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Object[] array = new Object[4];

        array[0] = id;
        array[1] = serverId;
        array[2] = nickname;
        array[3] = password;

        dest.writeArray(array);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

/////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (nickname != null ? !nickname.equals(user.nickname) : user.nickname != null) return false;
        return password != null ? password.equals(user.password) : user.password == null;
    }

    @Override
    public int hashCode() {
        int result = nickname != null ? nickname.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
