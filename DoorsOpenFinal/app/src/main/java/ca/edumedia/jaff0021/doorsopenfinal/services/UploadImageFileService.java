package ca.edumedia.jaff0021.doorsopenfinal.services;

/**
 * Created by zaheedjaffer on 2018-01-10.
 */

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import ca.edumedia.jaff0021.doorsopenfinal.utils.HttpHelper;
import ca.edumedia.jaff0021.doorsopenfinal.utils.RequestPackage;

import static ca.edumedia.jaff0021.doorsopenfinal.MainActivity.NEW_BUILDING_IMAGE;

/**
 * UploadImageFileService.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 */
public class UploadImageFileService extends IntentService {

    public static final String UPLOAD_IMAGE_FILE_SEVICE_MESSAGE = "UploadImageFileServiceMessage";
    public static final String UPLOAD_IMAGE_FILE_SERVICE_RESPONSE = "UploadImageFileServiceResponse";
    public static final String UPLOAD_IMAGE_FILE_SERVICE_EXCEPTION = "UploadImageFileServiceException";
    public static final String REQUEST_PACKAGE = "requestPackage";

    public UploadImageFileService() {
        super("UploadImageFileService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestPackage requestPackage = intent.getParcelableExtra(REQUEST_PACKAGE);
        Bitmap bitmap = intent.getParcelableExtra(NEW_BUILDING_IMAGE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);

        String response = null;
        try {
            response = HttpHelper.uploadFile(requestPackage, "jaff0021", "password", "photo.jpg", baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            Intent messageIntent = new Intent(UPLOAD_IMAGE_FILE_SEVICE_MESSAGE);
            messageIntent.putExtra(UPLOAD_IMAGE_FILE_SERVICE_EXCEPTION, e.getMessage());
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            return;
        }

        Log.e("HERE", "upload image: " + response);
    }
}
