package com.iplugcontrol.automation.until;

import com.iplugcontrol.automation.models.DevicesModel;

import java.util.Comparator;

public class SortByRoom implements Comparator<DevicesModel> {

    @Override
    public int compare(DevicesModel o1, DevicesModel o2) {
        return o1.getType().compareTo(o2.getType());
    }
}
