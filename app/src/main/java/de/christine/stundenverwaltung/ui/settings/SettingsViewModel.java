package de.christine.stundenverwaltung.ui.settings;

import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    String defaultStartTime;
    String defaultEndTime;
    String defaultBreakTime;
    String UserName;
    String companyName;

    public SettingsViewModel(String defaultStartTime, String defaultEndTime, String defaultBreakTime, String UserName, String companyName) {
        this.defaultStartTime = defaultStartTime;
        this.defaultEndTime = defaultEndTime;
        this.defaultBreakTime = defaultBreakTime;
        this.UserName = UserName;
        this.companyName = companyName;
    }

    public String getDefaultStartTime() {
        return defaultStartTime;
    }

    public void setDefaultStartTime(String defaultStartTime) {
        this.defaultStartTime = defaultStartTime;
    }

    public String getDefaultEndTime() {
        return defaultEndTime;
    }

    public void setDefaultEndTime(String defaultEndTime) {
        this.defaultEndTime = defaultEndTime;
    }

    public String getDefaultBreakTime() {
        return defaultBreakTime;
    }

    public void setDefaultBreakTime(String defaultBreakTime) {
        this.defaultBreakTime = defaultBreakTime;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}