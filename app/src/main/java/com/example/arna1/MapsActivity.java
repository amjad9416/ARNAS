package com.example.arna1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Interpolator;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.arna1.databinding.ActivityMapsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {



    private GoogleMap mMap;
    private Location previousLocation;

    private Marker marker;
    private ActivityMapsBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private DatabaseReference databaseReference,fil;
    private FirebaseAuth firebaseAuth;
    private Location location;
    private String selectedOption;
    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        String selectedItem = getIntent().getStringExtra("selectedItem");

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserDetails");
        fil=firebaseDatabase.getReference("UserDetails").child("blood");

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        retrieveUserLocationsByBloodGroup(selectedItem);



    }
    private void retrieveUserLocationsByBloodGroup(String bloodGroup) {
              Dialog dialog=new Dialog(MapsActivity.this);



        databaseReference.orderByChild("blood").equalTo(bloodGroup).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                String userId = dataSnapshot.getKey();
                String n = dataSnapshot.child("name").getValue(String.class);
                String g = dataSnapshot.child("gender").getValue(String.class);
                String a = dataSnapshot.child("age").getValue(String.class);
                String ad = dataSnapshot.child("address").getValue(String.class);
                String p = dataSnapshot.child("phone").getValue(String.class);
                Location location = dataSnapshot.child("location").getValue
                        (Location.class);



                Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                Double longitude = dataSnapshot.child("longitude").getValue(Double.class);

               marker=mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(n));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),15));

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String titletext = marker.getTitle();
                        Query query = databaseReference.orderByChild("name").equalTo(titletext);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    String naa = snapshot.child("name").getValue(String.class);
                                    String ggg = snapshot.child("gender").getValue(String.class);
                                    String aaa = snapshot.child("age").getValue(String.class);
                                    String addd = snapshot.child("address").getValue(String.class);
                                    String ppp = snapshot.child("phone").getValue(String.class);


                                    dialog.setContentView(R.layout.pops);
                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    dialog.setCancelable(false);

                                    dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                                    Button call = (Button) dialog.findViewById(R.id.callbut);
                                    Button dismiss = (Button) dialog.findViewById(R.id.dismissbut);
                                    TextView name = (TextView) dialog.findViewById(R.id.namename);
                                    TextView gender = (TextView) dialog.findViewById(R.id.genderrrrrrr);
                                    TextView age = (TextView) dialog.findViewById(R.id.ageage);
                                    TextView address = (TextView) dialog.findViewById(R.id.adderesddadrees);
                                    TextView phone = (TextView) dialog.findViewById(R.id.phonenumber);

                                    name.setText("Name:  " + naa);
                                    gender.setText("Gender:  " + ggg);
                                    age.setText("Age:  " + aaa);
                                    address.setText("Address:  " + addd);
                                    phone.setText("Phone Number:  " + ppp);
                                    call.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Toast.makeText(MapsActivity.this,"suc",Toast.LENGTH_SHORT).show();
                                            if (!ppp.isEmpty()) {
                                                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                                                dialIntent.setData(Uri.parse("tel:" + p));
                                                startActivity(dialIntent);
                                            }

                                        }
                                    });
                                    dismiss.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();


                                    // }//
                                    // });//

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle any errors
                            }
                        });
                    }
                });



















            }



            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MapsActivity.this, "error", Toast.LENGTH_SHORT).show();

                 }




        });





    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;






    }

}