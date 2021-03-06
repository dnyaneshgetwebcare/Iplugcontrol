package com.iplugcontrol.automation.ui.devices;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iplugcontrol.automation.R;
import com.iplugcontrol.automation.ScheduleActivity;
import com.iplugcontrol.automation.holders.HeaderViewHolder;
import com.iplugcontrol.automation.holders.ItemViewHolder;
import com.iplugcontrol.automation.models.DevicesModel;
import com.iplugcontrol.automation.models.RoomsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

import static android.content.ContentValues.TAG;

final class ExpandableRoomSection extends Section {

    final String title;
    final List<RoomsModel> list;
    final ClickListener clickListener;
    List<DevicesModel> deviceList;
    //SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
    FirebaseDatabase database;
    Context context;
    private boolean expanded = true;
    ExpandableRoomSection(@NonNull final String title, @NonNull final List<DevicesModel> deviceList,
                           @NonNull final ClickListener clickListener,Context context,List<RoomsModel> list) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_list)
                .headerResourceId(R.layout.list_header)
                .build());
        //this.sectionedRecyclerViewAdapter=sectionedRecyclerViewAdapter;
        this.title = title;

       this.list = list;
        this.clickListener = clickListener;
        this.deviceList = deviceList;
        database = FirebaseDatabase.getInstance();
        this.context=context;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded ? deviceList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(final View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        //position=itemHolder.getAdapterPosition();
        final DevicesModel myListData = deviceList.get(position);
        itemHolder.rl_container.setVisibility(View.GONE);
        itemHolder.progressBar.setVisibility(View.VISIBLE);
        itemHolder.device_name.setText(deviceList.get(position).getName());
        itemHolder.room_type.setText(deviceList.get(position).getType());
        itemHolder.device_img.setImageResource(getDeviceType(deviceList.get(position).getDevice_type(),deviceList.get(position).getStatus()));
        itemHolder.device_status.setImageResource(getDrawable(deviceList.get(position).getStatus()));
        //  holder.change_status.setText(deviceList.get(position).getStatus());
        itemHolder.ll_brightness.setVisibility(View.GONE);
        if (deviceList.get(position).getBrightness() == null || deviceList.get(position).getBrightness() == "") {
            deviceList.get(position).setBrightness("0");
        }
        if (deviceList.get(position).getFan_speed() == null || deviceList.get(position).getFan_speed() == "") {
            deviceList.get(position).setFan_speed("0");
        }

        if (deviceList.get(position).isFan_flag()) {
            itemHolder.ll_brightness.setVisibility(View.VISIBLE);
            itemHolder.sb_brightness.setMax(5);
            itemHolder.brightness_percent.setText(deviceList.get(position).getFan_speed());
            itemHolder.sb_brightness.setProgress(Integer.parseInt(deviceList.get(position).getFan_speed()));
            itemHolder.sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    try {
                        changeBrightness(deviceList.get(position).getId(), progress,"currentFanSpeedSetting");
                    } catch (Exception ex) {
                        Log.w("Status", ex.getMessage());
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    });
}

        if (deviceList.get(position).isBrightness_flag()) {
            itemHolder.ll_brightness.setVisibility(View.VISIBLE);
            itemHolder.sb_brightness.setMax(100);
            itemHolder.brightness_percent.setText(deviceList.get(position).getBrightness());
            itemHolder.sb_brightness.setProgress(Integer.parseInt(deviceList.get(position).getBrightness()));
            itemHolder.sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    try {
                        changeBrightness(deviceList.get(position).getId(), progress,"Brightness");
                    } catch (Exception ex) {
                        Log.w("Status", ex.getMessage());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        if(deviceList.get(position).getStatus()==null || deviceList.get(position).getStatus().equalsIgnoreCase("")){
            getRealtimeListner(deviceList.get(position).getId(),position,itemHolder.getAdapterPosition());
            //clickListener.onStateChanged(deviceList.get(position).getId(),position,itemHolder.getAdapterPosition());
        }else{
            itemHolder.progressBar.setVisibility(View.GONE);
            itemHolder.rl_container.setVisibility(View.VISIBLE);
        }
        itemHolder.btn_schedual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSchedual(deviceList.get(position).getSchedual_string(),deviceList.get(position).getDocument_id(),getname(deviceList.get(position).getName()),deviceList.get(position).getId(),deviceList.get(position).getType(),deviceList.get(position).getName());
            }
        });
        itemHolder.device_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changeStatus(deviceList.get(position).getId(), deviceList.get(position).getStatus());
                }catch (Exception ex){
                    Log.w("Status",ex.getMessage());
                }
            }
        });
        //Log.d(TAG, myRef.getKey());
        itemHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    changeStatus(deviceList.get(position).getId(), deviceList.get(position).getStatus());
                }catch (Exception ex){
                    Log.w("Status",ex.getMessage());
                }
            }
        });
    }
    public void getRealtimeListner(String device_id, final int position,int adapterpos){
        DatabaseReference myRef = database.getReference().getRoot().child(device_id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        //User expense = ds.getValue(User.class);
                        // users.add(expense.getUsername());
                        Log.d(TAG, ds.getKey() + " => " + ds.getValue());
                        if(ds.getKey().equalsIgnoreCase("OnOff")){
                            Map<String,Object> device_status= (Map<String, Object>) ds.getValue();
                            for (Map.Entry<String, Object> entry : device_status.entrySet()) {
                                Log.d(TAG, entry.getKey() + " => " + entry.getValue());
                                deviceList.get(position).setStatus(entry.getValue().toString());
                            }
                        }
                        if(ds.getKey().equalsIgnoreCase("Brightness")){

                            deviceList.get(position).setBrightness(ds.getValue().toString());
                        }
                        if(ds.getKey().equalsIgnoreCase("currentFanSpeedSetting")){

                            deviceList.get(position).setFan_speed(ds.getValue().toString());
                        }
                    }
                    clickListener.onStateChanged(adapterpos);
                    //sectionedRecyclerViewAdapter.notifyItemChanged(adapterpos);
                    //sectionedRecyclerViewAdapter.notifyItemChangedInSection(title,position);
                    //notifyItemChanged(position);

                }catch(Exception e){
                    Log.e("MODEL_ERROR",e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FIREBASE",databaseError.getMessage());
            }


        });
    }
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(final View view) {
        return new HeaderViewHolder(view);
    }
    public  void getSchedual(String schedual_string,String document_id,String device_name,String device_id,String room_type,String device_type){
        Intent intent=new Intent(context, ScheduleActivity.class);
        intent.putExtra("schedual_string",schedual_string);
        intent.putExtra("device_name",device_name);
        intent.putExtra("document_id",document_id);
        intent.putExtra("device_id",device_id);
        intent.putExtra("room_type",room_type);
        intent.putExtra("device_type",device_type);
        context.startActivity(intent);

    }
    public String getname(String type){
        switch (type) {
            case "ac":
                return "Air Conditioner";
            case "plug":
                return "Plug";
            case "door":
                return "Door";
            case "light":
                return "Light";
            case "Dlight":
                return "Light";
            case "geyser":
                return "Geyser";
            case "fan":
                return "Fan";
            case "curtain":
                return "Curtain";
            default:
                return "";
        }

    }
    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

       headerHolder.tvTitle.setText(title);
      /*  headerHolder.imgArrow.setImageResource(
                expanded ? R.drawable.ic_keyboard_arrow_up_black_18dp : R.drawable.ic_keyboard_arrow_down_black_18dp
        );
*/
//Log.w("Adapter Postion",)
      /*  headerHolder.rootView.setOnClickListener(v ->
                clickListener.onHeaderRootViewClicked(title, this)
        );*/
    }
    public void changeStatus(String device_id,String status_val){
        try {
            DatabaseReference myRef = database.getReference().getRoot().child(device_id).child("OnOff").child("on");
            boolean bool = Boolean.parseBoolean(status_val);
            myRef.setValue((!bool));
        }catch (Exception ex){
            Log.w("Status",ex.getMessage());
        }
    }
    public void changeBrightness(String device_id,int brightness,String set_value){
        //set_value="Brightness";

        try {
            DatabaseReference myRef = database.getReference().getRoot().child(device_id).child(set_value);
            if(set_value.equalsIgnoreCase("currentFanSpeedSetting")){
                myRef.setValue(brightness+"");
            }else{
                myRef.setValue(brightness);
            }

        }catch (Exception ex){
            Log.w("Status",ex.getMessage());
        }
    }
    public int getDeviceType(String type,String status){
        /*if(type.toLowerCase().contains("light") ||type.toLowerCase().contains("lamp")  ){
            type="light";
        }*/
        type=type.toLowerCase();
        if(status!=null) {
            switch (type) {
                case "light":
                    if (status.equalsIgnoreCase("true")) {
                        return R.mipmap.bulb_on;
                    } else {
                        return R.mipmap.bulb_off;
                    }
                case "Dlight":
                    if (status.equalsIgnoreCase("true")) {
                        return R.mipmap.bulb_on;
                    } else {
                        return R.mipmap.bulb_off;
                    }
                case "plug":

                    if (status.equalsIgnoreCase("true")) {
                        return R.mipmap.plug_on;
                    } else {
                        return R.mipmap.plug_off;
                    }
                case "door":

                    if (status.equalsIgnoreCase("true")) {
                        return R.mipmap.door_on;
                    } else {
                        return R.mipmap.door_off;
                    }
                case "ac":

                    if (status.equalsIgnoreCase("true")) {
                        return R.mipmap.ac_on;
                    } else {
                        return R.mipmap.ac_off;
                    }
                case "geyser":

                    if (status.equalsIgnoreCase("true")) {
                        return R.mipmap.geyser_on;
                    } else {
                        return R.mipmap.geyser_off;
                    }
                case "fan":

                    if (status.equalsIgnoreCase("true")) {
                        return R.mipmap.fan_on;
                    } else {
                        return R.mipmap.fan_off;
                    }
                case "curtain":

                    if (status.equalsIgnoreCase("true")) {
                        return R.mipmap.curtain_on;
                    } else {
                        return R.mipmap.curtain_off;
                    }
            }
        }
        return R.drawable.ic_unknown;
    }
    public int getDrawable(String status){
        if(status==null){
            return R.mipmap.ic_loader;
        }
        switch (status){
            case "false":

                return R.mipmap.power_off;
            default:
                return R.mipmap.power_on;

        }

    }
    boolean isExpanded() {
        return expanded;
    }

    void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }

    interface ClickListener {

        void onHeaderRootViewClicked(@NonNull final String sectionTitle, @NonNull final ExpandableRoomSection section);

        void onItemRootViewClicked(@NonNull final String sectionTitle, final int itemAdapterPosition);

        void onStateChanged(int adapterpos);
    }
}
