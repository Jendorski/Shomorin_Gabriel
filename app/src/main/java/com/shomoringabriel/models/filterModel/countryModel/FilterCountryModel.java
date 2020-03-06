package com.shomoringabriel.models.filterModel.countryModel;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.shomoringabriel.database.DecagonDatabase;

@Table(database = DecagonDatabase.class)
public class FilterCountryModel extends BaseModel {

    public FilterCountryModel() {
    }

    public FilterCountryModel(String userId, String countryName) {
        this.userId = userId;
        this.countryName = countryName;
    }

    @PrimaryKey
    private String userId;

    @PrimaryKey
    private String countryName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
