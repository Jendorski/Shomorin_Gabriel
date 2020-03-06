package com.shomoringabriel.models.filterModel.userModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.shomoringabriel.database.DecagonDatabase;
import com.shomoringabriel.models.filterModel.colorModel.FilterColorModel;
import com.shomoringabriel.models.filterModel.colorModel.FilterColorModel_Table;
import com.shomoringabriel.models.filterModel.countryModel.FilterCountryModel;
import com.shomoringabriel.models.filterModel.countryModel.FilterCountryModel_Table;

import java.io.Serializable;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Table(database = DecagonDatabase.class, name = "FilterUserModel")
public class FilterUserModel extends BaseModel implements Serializable, Parcelable {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;

    @SerializedName("avatar")
    @Expose
    @Column
    private String avatar;

    @SerializedName("fullName")
    @Expose
    @Column
    private String fullName;

    @SerializedName("createdAt")
    @Expose
    @Column
    private String createdAt;

    @SerializedName("gender")
    @Expose
    @Column
    private String gender;

    @SerializedName("colors")
    @Expose
    private List<String> colors;

    @SerializedName("countries")
    @Expose
    private List<String> countries;

    private List<FilterColorModel> colorsList;

    private List<FilterCountryModel> countriesList;

    public final static Parcelable.Creator<FilterUserModel> CREATOR = new Creator<FilterUserModel>() {

        @SuppressWarnings({"unchecked"})
        public FilterUserModel createFromParcel(Parcel in) {
            return new FilterUserModel(in);
        }

        public FilterUserModel[] newArray(int size) {
            return (new FilterUserModel[size]);
        }

    };

    private final static long serialVersionUID = 8913595692547224287L;

    protected FilterUserModel(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.avatar = ((String) in.readValue((String.class.getClassLoader())));
        this.fullName = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.colors, (java.lang.String.class.getClassLoader()));
        in.readList(this.countries, (java.lang.String.class.getClassLoader()));
    }

    public FilterUserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<FilterColorModel> getColorsList() {
        return colorsList;
    }

    public Single<List<FilterColorModel>> getColorsList(String ids) {
        return RXSQLite.rx(SQLite.select()
                .from(FilterColorModel.class)
                .where(FilterColorModel_Table.userId.is(ids)))
                .queryList()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single());
    }

    public void setColorsList(List<FilterColorModel> colorsList) {
        this.colorsList = colorsList;
    }

    public List<FilterCountryModel> getCountriesList() {
        return countriesList;
    }

    public Single<List<FilterCountryModel>> getCountriesList(String ids) {
        return RXSQLite.rx(SQLite.select()
                .from(FilterCountryModel.class)
                .where(FilterCountryModel_Table.userId.is(ids)))
                .queryList()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single());
    }

    public void setCountriesList(List<FilterCountryModel> countriesList) {
        this.countriesList = countriesList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(avatar);
        dest.writeValue(fullName);
        dest.writeValue(createdAt);
        dest.writeValue(gender);
        dest.writeList(colors);
        dest.writeList(countries);
    }

    public int describeContents() {
        return 0;
    }
}
