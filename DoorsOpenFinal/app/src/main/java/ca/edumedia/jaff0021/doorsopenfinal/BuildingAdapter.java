package ca.edumedia.jaff0021.doorsopenfinal;

/**
 * Created by zaheedjaffer on 2018-01-10.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.edumedia.jaff0021.doorsopenfinal.model.BuildingPOJO;
import ca.edumedia.jaff0021.doorsopenfinal.services.MyService;
import ca.edumedia.jaff0021.doorsopenfinal.utils.HttpMethod;
import ca.edumedia.jaff0021.doorsopenfinal.utils.RequestPackage;

import static ca.edumedia.jaff0021.doorsopenfinal.MainActivity.JSON_URL;

/**
 * BuildingAdapter.
 *
 */
public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {

    public static final String BUILDING_KEY = "building_key";

    private Context            mContext;
    private List<BuildingPOJO> mBuildings;

    public BuildingAdapter(Context context, List<BuildingPOJO> buildings) {
        this.mContext = context;
        this.mBuildings = buildings;
    }

    @Override
    public BuildingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View planetView = inflater.inflate(R.layout.list_building, parent, false);
        ViewHolder viewHolder = new ViewHolder(planetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BuildingAdapter.ViewHolder holder, final int position) {
        final BuildingPOJO aBuilding = mBuildings.get(position);

        holder.tvName.setText(aBuilding.getNameEN());
        holder.tvAddress.setText(aBuilding.getAddressEN());

        //FIXME :: LOCALHOST
        String url = "https://doors-open-ottawa.mybluemix.net/buildings/" + aBuilding.getBuildingId() + "/image";
        //url = "http://10.0.2.2:3000/buildings/" + aBuilding.getBuildingId() + "/image";
        Picasso.with(mContext)
                .load(Uri.parse(url))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.noimagefound)
                .resize(96, 96)
                .into(holder.imageView);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailBuildingActivity.class);
                intent.putExtra(BUILDING_KEY, aBuilding);
                mContext.startActivity(intent);

            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, EditBuildingActivity.class);
                intent.putExtra(BUILDING_KEY, aBuilding);
                mContext.startActivity(intent);
                return true;
            }
        });

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Confirm")
                        .setMessage("Delete " + aBuilding.getNameEN() + " - " + aBuilding.getBuildingId() + "?")

                        // Displays: OK
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete this course
                                Toast.makeText(mContext, "Deleted Course: " + aBuilding.getBuildingId(), Toast.LENGTH_SHORT).show();

                                 RequestPackage requestPackage = new RequestPackage();
                                 requestPackage.setMethod( HttpMethod.DELETE );
                                 requestPackage.setEndPoint( JSON_URL + aBuilding.getBuildingId() );

                                 Intent intent = new Intent(mContext, MyService.class);
                                 intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
                                 mContext.startService(intent);

                                dialog.dismiss();
                                intent = new Intent(mContext, MainActivity.class);
                                mContext.startActivity(intent);
                            }
                        })

                        // Displays: Cancel
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();

                                Toast.makeText(mContext, "CANCELLED: Deleted Course: " + aBuilding.getBuildingId(), Toast.LENGTH_SHORT).show();
                                //Log.i(TAG, "CANCELLED: Deleted Course: " + aCourse.getCode());
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBuildings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvAddress;
        public ImageView imageView;
        public View mView;
        public Button mDelete;

        public ViewHolder(View buildingView) {
            super(buildingView);

            tvName = buildingView.findViewById(R.id.buildingNameText);
            tvAddress = buildingView.findViewById(R.id.buildingAddressText);
            imageView = buildingView.findViewById(R.id.imageView);
            mView = buildingView;
            mDelete = buildingView.findViewById(R.id.deleteButton);
        }
    }

    public void sortByNameAscending() {
        Collections.sort( mBuildings, new Comparator<BuildingPOJO>() {
            @Override
            public int compare( BuildingPOJO lhs, BuildingPOJO rhs ) {
                return lhs.getNameEN().compareTo( rhs.getNameEN() );
            }
        });

        notifyDataSetChanged();
    }

    public void sortByNameDescending() {
        Collections.sort( mBuildings, Collections.reverseOrder(new Comparator<BuildingPOJO>() {
            @Override
            public int compare( BuildingPOJO lhs, BuildingPOJO rhs ) {
                return lhs.getNameEN().compareTo( rhs.getNameEN() );
            }
        }));

        notifyDataSetChanged();
    }
}
