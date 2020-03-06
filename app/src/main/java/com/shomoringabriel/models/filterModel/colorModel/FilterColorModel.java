package com.shomoringabriel.models.filterModel.colorModel;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.shomoringabriel.database.DecagonDatabase;

@Table(database = DecagonDatabase.class)
public class FilterColorModel extends BaseModel {

    @PrimaryKey
    private String userId;

    @PrimaryKey
    private String colorName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
