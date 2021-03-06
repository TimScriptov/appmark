package com.vipapp.appmark2.item.design.attribute;

import android.view.View;

import com.vipapp.appmark2.item.design.DesignAttribute;
import com.vipapp.appmark2.project.Res;
import com.vipapp.appmark2.util.AttributesUtils;
import com.vipapp.appmark2.util.ClassUtils;

import static com.vipapp.appmark2.util.Const.GRAVITY_ATTR;

public class GravityAttribute extends DesignAttribute {
    public GravityAttribute() {
        super(GRAVITY_ATTR);
    }

    @Override
    public void applyAction(View view, Res resources) {
        ClassUtils.callMethod(view,
                "setGravity",
                new Class[]{ int.class },
                new Object[]{ AttributesUtils.valueToGravity(getValue()) });
    }
}
