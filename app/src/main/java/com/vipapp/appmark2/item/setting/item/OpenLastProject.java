package com.vipapp.appmark2.item.setting.item;

import android.content.Context;

import com.vipapp.appmark2.R;
import com.vipapp.appmark2.item.SettingsItem;
import com.vipapp.appmark2.item.setting.type.BooleanSetting;
import com.vipapp.appmark2.util.wrapper.Str;

import static com.vipapp.appmark2.util.Const.DEFAULT_OPEN_LAST_PROJECT;
import static com.vipapp.appmark2.util.Const.OPEN_LAST_PROJECT_PREF;

public class OpenLastProject extends SettingsItem<Boolean> {

    public OpenLastProject() {
        super(OPEN_LAST_PROJECT_PREF, new BooleanSetting());
        this.default_value = DEFAULT_OPEN_LAST_PROJECT;
    }

    public String getSettingTitle(Context ctx) {
        return Str.get(R.string.open_last_project_title);
    }
    public String getSettingSubtitle(Context ctx) {
        return Str.get(R.string.opened_project_subtitle);
    }

}
