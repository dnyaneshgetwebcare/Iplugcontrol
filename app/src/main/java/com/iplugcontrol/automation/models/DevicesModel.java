package com.iplugcontrol.automation.models;

public class DevicesModel {
    String id;
    String status;
    String name;
    String type;
    String fan_speed;
    String device_type;
    String brightness;
    String schedual_string;
    String document_id;
    boolean brightness_flag;
    boolean on_off_flag;
    boolean open_close_flag;
    boolean fan_flag;

    public String getDevice_type() {
        return device_type;
    }

    public boolean isOn_off_flag() {
        return on_off_flag;
    }

    public String getFan_speed() {
        return fan_speed;
    }

    public void setFan_speed(String fan_speed) {
        this.fan_speed = fan_speed;
    }

    public void setOn_off_flag(boolean on_off_flag) {
        this.on_off_flag = on_off_flag;
    }

    public boolean isOpen_close_flag() {
        return open_close_flag;
    }

    public void setOpen_close_flag(boolean open_close_flag) {
        this.open_close_flag = open_close_flag;
    }

    public boolean isFan_flag() {
        return fan_flag;
    }

    public void setFan_flag(boolean fan_flag) {
        this.fan_flag = fan_flag;
    }

    public boolean isBrightness_flag() {
        return brightness_flag;
    }

    public void setBrightness_flag(boolean brightness_flag) {
        this.brightness_flag = brightness_flag;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public String getSchedual_string() {
        return schedual_string;
    }

    public void setSchedual_string(String schedual_string) {
        this.schedual_string = schedual_string;
    }
    public DevicesModel() {
    }

    public DevicesModel(String id, String status, String name,String schedual_string) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.schedual_string = schedual_string;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }
}
