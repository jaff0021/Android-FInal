package ca.edumedia.jaff0021.doorsopenfinal;

/**
 * Created by zaheedjaffer on 2018-01-11.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import ca.edumedia.jaff0021.doorsopenfinal.model.BuildingPOJO;
import ca.edumedia.jaff0021.doorsopenfinal.services.MyService;
import ca.edumedia.jaff0021.doorsopenfinal.utils.HttpMethod;
import ca.edumedia.jaff0021.doorsopenfinal.utils.RequestPackage;

import static ca.edumedia.jaff0021.doorsopenfinal.MainActivity.JSON_URL;

/**
 * Edit Course Activity.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 */
public class EditBuildingActivity extends Activity {

    private TextView tvBuildingName;
    private EditText etBuildingDescription;
    private EditText etBuildingAddress;
    private EditText etSaturdayOpen;
    private EditText etSaturdayClose;
    private EditText etSundayOpen;
    private EditText etSundayClose;
    private CheckBox cbIsBuildingNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_edit);

        final BuildingPOJO selectedBuilding = getIntent().getExtras().getParcelable(BuildingAdapter.BUILDING_KEY);
        if (selectedBuilding == null) {
            throw new AssertionError("Null data item received!");
        }

        tvBuildingName = (EditText) findViewById(R.id.editBuildingName);
        etBuildingDescription = (EditText) findViewById(R.id.editBuildingDescription);
        etBuildingAddress = (EditText) findViewById(R.id.editBuildingAddress);
        etSaturdayOpen = (EditText) findViewById(R.id.editSaturdayOpen);
        etSaturdayClose = (EditText) findViewById(R.id.editSaturdayClose);
        etSundayOpen = (EditText) findViewById(R.id.editSundayOpen);
        etSundayClose = (EditText) findViewById(R.id.editSundayClose);
        cbIsBuildingNew = (CheckBox) findViewById(R.id.isNewBuilding);

        // Prevent User from editing Course Code
        tvBuildingName.setEnabled(false);

        tvBuildingName.setText(selectedBuilding.getNameEN());
        etBuildingDescription.setText(selectedBuilding.getDescriptionEN());
        etBuildingAddress.setText(selectedBuilding.getAddressEN() + "");
        etSaturdayOpen.setText(selectedBuilding.getSaturdayStart());
        etSaturdayClose.setText(selectedBuilding.getSaturdayClose());
        etSundayOpen.setText(selectedBuilding.getSundayStart());
        etSundayClose.setText(selectedBuilding.getSundayClose());
        cbIsBuildingNew.setChecked(selectedBuilding.isIsNewBuilding());


        Button saveButton = findViewById(R.id.saveEditBuilding);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etBuildingDescription.getText().toString();
                String address = etBuildingAddress.getText().toString();
                String name = tvBuildingName.getText().toString();
                String satOpen = etSaturdayOpen.getText().toString();
                String satClose = etSaturdayClose.getText().toString();
                String sunOpen = etSundayOpen.getText().toString();
                String sunClose = etSundayClose.getText().toString();
                boolean isNewBuilding = cbIsBuildingNew.isChecked();

                // Validation Rule: all fields are required
                if (name.isEmpty()) {
                    tvBuildingName.setError( "Please Enter the Building Name");
                    tvBuildingName.requestFocus();
                    return;
                }

                if (address.isEmpty()) {
                    etBuildingAddress.setError( "Please Enter the Building Address");
                    etBuildingAddress.requestFocus();
                    return;
                }

                if (description.isEmpty()) {
                    etBuildingDescription.setError( "Please Enter the Building Description.");
                    etBuildingDescription.requestFocus();
                    return;
                }

                // Validation Rule: course level is in range: 1 - 4 (inclusive)
//                int level = 0;
//                try {
//                    level = Integer.parseInt(levelString);
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//                if ( (level < 1) || (level > 4) ) {
//                    etBuildingAddress.setError( "Please Enter the Course Level: 1 to 4");
//                    etBuildingAddress.requestFocus();
//                }

                selectedBuilding.setNameEN( name );
                selectedBuilding.setDescriptionEN( description );
                selectedBuilding.setAddressEN( address );
                selectedBuilding.setIsNewBuilding( isNewBuilding );

                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setMethod( HttpMethod.PUT );
                requestPackage.setEndPoint( JSON_URL + selectedBuilding.getBuildingId() );
                requestPackage.setParam("nameEN", name);
                requestPackage.setParam("descriptionEN", description);
                requestPackage.setParam("addressEN", address);

                if(isNewBuilding == true){
                    requestPackage.setParam("isNewBuilding", "true");
                } else {
                    requestPackage.setParam("isNewBuilding", "false");
                }


                Intent intent = new Intent(getApplicationContext(), MyService.class);
                intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
                startService(intent);
                setResult(RESULT_OK, intent);
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button cancelButton = findViewById(R.id.cancelEditBuilding);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}