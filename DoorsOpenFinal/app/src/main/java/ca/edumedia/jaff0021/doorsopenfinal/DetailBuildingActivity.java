package ca.edumedia.jaff0021.doorsopenfinal;

/**
 * Created by zaheedjaffer on 2018-01-10.
 */

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import ca.edumedia.jaff0021.doorsopenfinal.model.BuildingPOJO;


/**
 * Detailed Activity of a Building.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 */
public class DetailBuildingActivity extends AppCompatActivity implements OnMapReadyCallback {
    //TODO add map
    private TextView  tvName;
    private TextView  tvCategory;
    private TextView  tvDescription;
    private ImageView buildingImage;
    private TextView  tvSaturday;
    private TextView  tvSunday;
    private GoogleMap mMap;
    private double    longitude;
    private double    latitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        BuildingPOJO selectedBuilding = getIntent().getExtras().getParcelable(BuildingAdapter.BUILDING_KEY);
        if (selectedBuilding == null) {
            throw new AssertionError("Null data item received!");
        }


        tvName = (TextView) findViewById(R.id.tvName);
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        buildingImage = (ImageView) findViewById(R.id.buildingImage);
        tvSaturday = (TextView) findViewById(R.id.tvSaturday);
        tvSunday = (TextView) findViewById(R.id.tvSunday);

        tvName.setText(selectedBuilding.getNameEN());
        tvCategory.setText(selectedBuilding.getCategoryEN());
        tvDescription.setText(selectedBuilding.getDescriptionEN());

        longitude = selectedBuilding.getLongitude();
        latitude = selectedBuilding.getLatitude();

        try{
            tvSaturday.setText("Saturday " + selectedBuilding.getSaturdayStart().substring(11) + " - " + selectedBuilding.getSaturdayClose().substring(11));
        } catch (Exception e) {
            tvSaturday.setText("Saturday No Hours Available");
        }

        try{
            tvSunday.setText("Sunday " + selectedBuilding.getSundayStart().substring(11) + " - " + selectedBuilding.getSundayClose().substring(11));
        } catch (Exception e) {
            tvSunday.setText("Sunday No Hours Available");
        }

        findViewById(R.id.fragment).getLayoutParams().height = 600;

        //FIXME :: LOCALHOST
        String url = "https://doors-open-ottawa.mybluemix.net/buildings/" + selectedBuilding.getBuildingId() + "/image";
        //url = "http://10.0.2.2:3000/buildings/" + selectedBuilding.getBuildingId() + "/image";
        Picasso.with(this)
                .load(Uri.parse(url))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.noimagefound)
                .fit()
                .into(buildingImage);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng building = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(building).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(building));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }
}