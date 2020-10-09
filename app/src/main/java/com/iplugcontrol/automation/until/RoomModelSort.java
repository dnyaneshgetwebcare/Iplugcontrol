package com.iplugcontrol.automation.until;

import com.iplugcontrol.automation.models.RoomsModel;

import java.util.Comparator;

public class RoomModelSort implements Comparator<RoomsModel> {
    @Override
    public int compare(RoomsModel o1, RoomsModel o2) {
        return o1.getRoomName().compareTo(o2.getRoomName());
    }
}
