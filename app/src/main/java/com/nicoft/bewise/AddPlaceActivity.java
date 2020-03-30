package com.nicoft.bewise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.graphics.Matrix;

import com.gc.materialdesign.views.ButtonFloat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.widget.Spinner;


import java.io.File;
import java.io.IOException;

public class AddPlaceActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleMapOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);


        options = new GoogleMapOptions();
        setUpMapIfNeeded();


        myDb = new DatabaseHelper(this);

        final String[] TITLES = new String[]{"House","Office"};



        final ImageView picAddPlace = (ImageView)findViewById(R.id.picAddPlace);
        picAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO lanzar camara para tomar la foto de la casa.
                    takePicture();
                }
        });
        final ImageView fotoCasa=(ImageView)findViewById(R.id.imageViewHouseAddPlace);
        fotoCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO lanzar camara para tomar la foto de la casa.
                takePicture();
            }
        });
        final ImageView fotoTrabajo=(ImageView)findViewById(R.id.imageViewWorkAddPlace);
        fotoTrabajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO lanzar camara para tomar la foto de la casa.
                takePicture();
            }
        });

        EditText namePlace = (EditText)findViewById(R.id.input_name_addplace);

        Spinner tipoPlace = (Spinner)findViewById(R.id.spinner_tipoaddplace);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_spn, TITLES);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        tipoPlace.setAdapter(adapter);

       tipoPlace.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
           @Override
           public void onItemSelected(Spinner spinner, View view, int i, long l) {
               Toast.makeText(getApplicationContext(),spinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
               final LinearLayout llcamera=(LinearLayout)findViewById(R.id.llimgcameraAddPlace);

               if(llcamera.getVisibility()==View.GONE)
               {
                   if(spinner.getSelectedItem().toString().equals(TITLES[0]))
                   {
                       Toast.makeText(getApplicationContext(),"House",Toast.LENGTH_LONG).show();
                       fotoCasa.setVisibility(View.VISIBLE);
                        fotoTrabajo.setVisibility(View.GONE);
                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),"Office",Toast.LENGTH_LONG).show();
                       fotoCasa.setVisibility(View.GONE);
                       fotoTrabajo.setVisibility(View.VISIBLE);
                   }
               }
           }
       });


        final ButtonFloat addButton = (ButtonFloat)findViewById(R.id.buttonFloatAddPlace);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
    File imageFile;//TODO ORGANIZAR ESTE ATRIBUTO
    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        long time = System.currentTimeMillis();
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+".jpg");
        Uri tempUri=Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        intent.putExtra("TIME",time);

        startActivityForResult(intent, 0);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setUpMapIfNeeded();

        if(requestCode==0){
            switch (resultCode){
                case Activity.RESULT_OK:
                    if (imageFile.exists()){
                        Toast.makeText(getApplicationContext(),"The file was saved at "+imageFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
                        final ImageView picAddPlace = (ImageView)findViewById(R.id.picAddPlace);
                        final ImageView fotoCasa=(ImageView)findViewById(R.id.imageViewHouseAddPlace);
                        final ImageView fotoTrabajo=(ImageView)findViewById(R.id.imageViewWorkAddPlace);
                        final ImageView fotoRound=(ImageView)findViewById(R.id.imageViewWorkAddPlace);

                        final LinearLayout llcamera=(LinearLayout)findViewById(R.id.llimgcameraAddPlace);
                        final LinearLayout llimgs=(LinearLayout)findViewById(R.id.llimgsAddPlace);

                        llcamera.setVisibility(View.GONE);
                        llimgs.setVisibility(View.VISIBLE);

                        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());


                        try {
                            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                            //Log.d("EXIF", "Exif: " + orientation);
                            Matrix matrix = new Matrix();
                            if (orientation == 6) {
                                matrix.postRotate(90);
                            }
                            else if (orientation == 3) {
                                matrix.postRotate(180);
                            }
                            else if (orientation == 8) {
                                matrix.postRotate(270);
                            }
                            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
                        }
                        catch (Exception e) {

                        }


                        //picAddPlace.setVisibility(View.GONE);
                        fotoCasa.setImageBitmap(myBitmap);
                        fotoCasa.setVisibility(View.VISIBLE);
                        fotoTrabajo.setImageBitmap(myBitmap);
                        fotoRound.setImageBitmap(myBitmap);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"The file was not saved",Toast.LENGTH_LONG).show();
                    }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            //mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAddPlace))
                    //.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        mMap.setPadding(0,0,0,32);
        //options.mapType(GoogleMap.MAP_TYPE_NORMAL).compassEnabled(true).rotateGesturesEnabled(true).tiltGesturesEnabled(true);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), 18));
            }
        });
        //mMap.getMyLocation();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));


                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latLng.latitude+","+latLng.longitude+"&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                // startActivity(mapIntent);

            }
        });
    }
}
