package kuncystem.hu.testapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuncy on 2017. 11. 18..
 */

/**
 * This is a model class to the server response
 * */
public class ApiLevel {
    @SerializedName("imageUrl")
    private String ImageUrl;

    @SerializedName("releaseDate")
    private Integer ReleaseDate;

    @SerializedName("versionNumber")
    private String VersionNumber;

    @SerializedName("rowType")
    private Integer RowType;

    @SerializedName("codeName")
    private String CodeName;

    @SerializedName("apiLevel")
    private Integer ApiLevel;

    public String getImageUrl() {
        return ImageUrl;
    }

    public Integer getReleaseDate() {
        return ReleaseDate;
    }

    public String getVersionNumber() {
        return VersionNumber;
    }

    public Integer getRowType() {
        return RowType;
    }

    public String getCodeName() {
        return CodeName;
    }

    public Integer getApiLevel() {
        return ApiLevel;
    }

    public void setRowType(Integer rowType) {
        RowType = rowType;
    }
}
