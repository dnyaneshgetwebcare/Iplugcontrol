package com.iplugcontrol.automation.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iplugcontrol.automation.R;
import com.iplugcontrol.automation.adapter.CriticalDeviceAdapter;
import com.iplugcontrol.automation.adapter.OtherDeviceAdapter;
import com.iplugcontrol.automation.models.CriticalDevices;
import com.iplugcontrol.automation.models.OtherDevices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends Fragment {
    String TAG = "DashboardFragment";
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.critical_device)
    RecyclerView criticalDevice;
    @BindView(R.id.other_text)
    TextView otherText;
    @BindView(R.id.other_device_list)
    RecyclerView otherDeviceList;
    private HomeViewModel homeViewModel;
    List<CriticalDevices> criticalDevicesList;
    List<OtherDevices> otherDevicesList;
    FirebaseAuth mAuth;
    CriticalDeviceAdapter criticalDevicesAdapter;
OtherDeviceAdapter otherDeviceAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this,root);
        criticalDevice.setHasFixedSize(true);
        otherDevicesList=new ArrayList<OtherDevices>();
        criticalDevice.setLayoutManager(new LinearLayoutManager(getContext()));
        otherDeviceList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
       // database = FirebaseDatabase.getInstance()
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
      //  Log.d(TAG, "User Signed In " + user.getEmail());
        //Toast.makeText(getContext(), "User Signed In " + user.getEmail(), Toast.LENGTH_SHORT).show();
        db.collection("users").document(user.getUid()).collection("devices")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            criticalDevicesList = new ArrayList<CriticalDevices>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //DocumentReference snapshot=document.get();
                                Map<String, Object> group = document.getData();
                                Log.d(TAG, document.getId() + " => " + document.getData().values().toString());
                                //Log.d(TAG, reference.getId() + " => " + reference.getPath());
                                CriticalDevices criticalDevices = null;
                                OtherDevices otherDevices = null;
                                for (Map.Entry<String, Object> entry : group.entrySet()) {
                                    if (criticalDevices == null) {
                                        criticalDevices = new CriticalDevices();
                                    }
                                    //myRef.getKey()
                                    if (entry.getKey().equalsIgnoreCase("roomHint")) {
                                        // devicesModel.setName(entry.getKey());
                                        if (criticalDevices != null) {
                                            criticalDevices.setType(entry.getValue().toString());

                                        }
                                    } else if(entry.getKey().equalsIgnoreCase("type")) {
                                        criticalDevices.setName(getName( entry.getValue().toString()));
                                } else if(entry.getKey().equalsIgnoreCase("otherDeviceIds")) {
                                        // criticalDevices = new CriticalDevices();

                                        ArrayList<HashMap<String, Object>> device_id_map = (ArrayList<HashMap<String, Object>>) entry.getValue();
                                        // HashMap<String,Object> device_id_map= temp.get(0);
                                        //device_id_map.get(0);
                                        for (Map.Entry<String, Object> entry_dev : device_id_map.get(0).entrySet()) {
                                            criticalDevices.setId(entry_dev.getValue().toString());
                                        }
                                    }else if(entry.getKey().equalsIgnoreCase("nicknames")) {

                                            ArrayList<String> device_id_map= (ArrayList<String>) entry.getValue();

                                            //criticalDevices.setName(device_id_map.get(0).toString());

                                        otherDevices = new OtherDevices();
                                        otherDevices.setRoomtype(device_id_map.get(0).toString());
                                        otherDevices.setOffDevices(1);
                                        otherDevices.setOnDevices(1);
                                       /* if (!otherDevicesList.contains(otherDevices)) {

                                            otherDevicesList.add(otherDevices);
                                        }else{*/
                                        boolean new_device=true;
                                        for (OtherDevices p : otherDevicesList) {
                                            if (p.getRoomtype().equals(device_id_map.get(0).toString())) {
                                                p.setOnDevices(p.getOnDevices()+1);
                                                new_device=false;
                                                break;
                                            }
                                        }
                                        if(new_device){
                                            otherDevicesList.add(otherDevices);
                                        }
                                    }

                                        //criticalDevices.setId(entry.getValue().toString());


                                        //}



                                }
                                if (criticalDevices != null) {
                                    criticalDevicesList.add(criticalDevices);

                                }

                            }
                            //Collections.sort(devicesModels,new SortByRoom());
                            // Collections.sort(roomsModels,new RoomModelSort());
                            //getRealtimeListner(devicesModels.get(0).getId());
                            // progressRv.setVisibility(View.GONE);
                            criticalDevicesAdapter = new CriticalDeviceAdapter(criticalDevicesList,getContext());
                          /*  List<DevicesModel> devicelist=new ArrayList<DevicesModel>();
                            for(int i=0;i<roomsModels.size();i++){
                                devicelist=new ArrayList<DevicesModel>();
                                for (DevicesModel dev_model:devicesModels
                                ) {
                                    if(dev_model.getType().equalsIgnoreCase(roomsModels.get(i).getRoomName())) {
                                        devicelist.add(dev_model);
                                    }
                                }

                                sectionedAdapter.addSection(roomsModels.get(i).getRoomName(),new ExpandableRoomSection(roomsModels.get(i).getRoomName(),devicelist,clickListener,getContext(),roomsModels));
                            }*/
                          otherDeviceAdapter=new OtherDeviceAdapter(otherDevicesList,getContext());
                          otherDeviceList.setAdapter(otherDeviceAdapter);
                              criticalDevice.setAdapter(criticalDevicesAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return root;
    }
    public String getName(String string){
        String[] parts = string.split(Pattern.quote("."));
        return parts[3];
    }
   /* @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean containsName(final List<OtherDevices> list, final String name){
        return list.stream().map(OtherDevices::getRoomtype).filter(name::equals).findFirst().isPresent();
    }*/
}