package com.example.arna1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class hospital_details extends AppCompatActivity {

    EditText name, age, address, phone, gender, blood;
    Context context;
    Button submit;
    RadioGroup radioGroup;
    String selectedOption;
    String storedItem;

    DatabaseReference usersReference;
    private boolean isLocationUpdatesStarted = false;
    FirebaseDatabase f=FirebaseDatabase.getInstance();
    DatabaseReference root=f.getReference().child("users");
    private LocationListener locationListener;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    //private static final long UPDATE_INTERVAL=600;
    private LatLng latLng;
    private long UPDATE_INTERVAL = 2000;
    private long FASTEST_INTERVAL = 5000;
    private static final int PERMISSIONS_REQUEST = 1;
    private DatabaseReference databaseReference;
    // private FusedLocationProviderClient fusedLocationClient;

    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_details);
        try {
            this.getSupportActionBar().hide();
        }
        // catch block to handle NullPointerException
        catch (NullPointerException e) {
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // context=this;
        Spinner sa=(Spinner)findViewById(R.id.rblood);

        sa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                // Store the selected item in a string variable
                storedItem = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected (if needed)
            }
        });
// Start the location service
       // startService(new Intent(this, LocationService.class));
        radioGroup = findViewById(R.id.RadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                selectedOption = radioButton.getText().toString();
                //Toast.makeText(hospital_details.this, "Selected Option: " + selectedOption, Toast.LENGTH_SHORT).show();
            }
        });


        // Initialize the location request with desired properties
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        usersReference = FirebaseDatabase.getInstance().getReference("UserDetails");

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateLocation(location);
                }
            }
        };

        name = (EditText) findViewById(R.id.rname);
        age = (EditText) findViewById(R.id.rage);
        address = (EditText) findViewById(R.id.remailid);

        phone = (EditText) findViewById(R.id.rphone);




        submit = (Button) findViewById(R.id.submit);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        databaseReference = FirebaseDatabase.getInstance().getReference();


         databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //  else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            //startLocationUpdates();
                            saveUserDetails();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_LOCATION);
                        }
                    }

                }
            //}
        });
    }

private void startLocationUpdates() {
    if (!isLocationUpdatesStarted) {
        isLocationUpdatesStarted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
               // saveUserDetails();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_LOCATION);
            }
        }
    }
}
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
    private void updateLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        String userId = getCurrentUserId();

        // Implement a method to get the current user's ID
        if (userId != null) {
            DatabaseReference userRef = usersReference.child(userId);
            userRef.child("latitude").setValue(latitude);
            userRef.child("longitude").setValue(longitude);
        }
    }
    private String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }



    private void saveUserDetails() {
        String namee = name.getText().toString();
        String agee = age.getText().toString();
        String addresse = address.getText().toString();
        String bloode = storedItem;
        String phonee = phone.getText().toString();
        String gendere = selectedOption;


        //int ag=Integer.parseInt(age.getText().toString());

        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Enter name");
        } else if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(hospital_details.this, "Select Gender", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(age.getText().toString())) {
            age.setError("Enter age");

        }






        // Convert the age text to an integer

   else if (storedItem.equals("Select Blood Group")) {
            Toast.makeText(hospital_details.this, "Select Your Blood Group", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(phone.getText().toString())) {
            phone.setError("Enter Phone number");
        } else if (phone.getText().toString().length() != 10) {
            phone.setError("Invalid phone number");

        } else if (TextUtils.isEmpty(address.getText().toString())) {
            address.setError("Enter address");
        }


        else {
            try {


                int aged = Integer.parseInt(age.getText().toString().trim());
                if (aged > 65) {
                    age.setError("Age Limit Exceeded");
                } else if (aged <= 18) {
                    age.setError("Required Min Age 18");
                } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                } else {


                    fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location = task.getResult();
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                String userId = getCurrentUserId();
                                alldet userDetails = new alldet(namee, agee, addresse, bloode, phonee, gendere, longitude, latitude);

                                usersReference.child(userId).setValue(userDetails);
                                Toast.makeText(hospital_details.this, "Details submitted successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(hospital_details.this,MainActivity.class));
                            } else {
                                Toast.makeText(hospital_details.this, "Failed to get location. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
                    catch (NumberFormatException e) {
                // Toast.makeText(hospital_details.this,"Invalid age",Toast.LENGTH_SHORT).show();
            }
        }



        startService(new Intent(this, LocationService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveUserDetails();
            } else {
                Toast.makeText(hospital_details.this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

}

