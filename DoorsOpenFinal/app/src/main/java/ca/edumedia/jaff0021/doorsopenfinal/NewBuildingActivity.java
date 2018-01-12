package ca.edumedia.jaff0021.doorsopenfinal;

/**
 * Created by zaheedjaffer on 2018-01-10.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ca.edumedia.jaff0021.doorsopenfinal.model.BuildingPOJO;
import ca.edumedia.jaff0021.doorsopenfinal.services.MyService;
import ca.edumedia.jaff0021.doorsopenfinal.utils.HttpMethod;
import ca.edumedia.jaff0021.doorsopenfinal.utils.RequestPackage;

import static ca.edumedia.jaff0021.doorsopenfinal.MainActivity.JSON_URL;
import static ca.edumedia.jaff0021.doorsopenfinal.MainActivity.NEW_BUILDING_DATA;
import static ca.edumedia.jaff0021.doorsopenfinal.MainActivity.NEW_BUILDING_IMAGE;

/**
 * NewBuildingActivity
 */

public class NewBuildingActivity extends AppCompatActivity {
    private final static int CAMERA_REQUEST_CODE = 100;

    private EditText nameEditText;
    private EditText addressEditText;
    private ImageView photoView;
    private Bitmap    bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_new);

        nameEditText = (EditText) findViewById(R.id.editName);
        addressEditText = (EditText) findViewById(R.id.editAddress);
        photoView = (ImageView) findViewById(R.id.photoView);
        bitmap = null;

        FloatingActionButton photoButton = (FloatingActionButton) findViewById(R.id.cameraButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveNewBuildingButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String address = addressEditText.getText().toString();

                // Validation Rule: all fields are required
                if (name.isEmpty()) {
                    nameEditText.setError( "Please Enter the Name");
                    nameEditText.requestFocus();
                    return;
                }

                if (address.isEmpty()) {
                    addressEditText.setError( "Please Enter the Address");
                    addressEditText.requestFocus();
                    return;
                }

                BuildingPOJO newBuilding = new BuildingPOJO();
                newBuilding.setNameEN( name );
                newBuilding.setAddressEN( address );

                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setMethod( HttpMethod.POST );
                requestPackage.setEndPoint( JSON_URL );
                requestPackage.setParam("nameEN", newBuilding.getNameEN() );
                requestPackage.setParam("addressEN", newBuilding.getAddressEN() );
                requestPackage.setParam("addressFR", newBuilding.getAddressEN() );
                requestPackage.setParam("categoryId", 3 + "");
                requestPackage.setParam("categoryEN", "abc");
                requestPackage.setParam("categoryFR", "xyz");

                Intent intent = new Intent(getApplicationContext(), MyService.class);
                intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
                startService(intent);

                intent = new Intent();
                intent.putExtra(NEW_BUILDING_DATA, newBuilding);
                if (bitmap != null) {
                    intent.putExtra(NEW_BUILDING_IMAGE, bitmap);
                }
                setResult(RESULT_OK, intent);
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                if(bitmap != null){
                    //there is a picture
                    photoView.setImageBitmap(bitmap);
                }
            }

            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled: Take a Photo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}