package com.example.shlomit.myhwapp;

/**
 * Created by shlomit on 11/04/2018.
 */

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * An activity to create a folder inside a folder.
 */
public class CreateFolderInFolderActivity extends BaseDemoActivity {
    private static final String TAG = "CreateFolderInFolder";

    @Override
    protected void onDriveClientReady() {
        pickFolder()
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveId>() {
                            @Override
                            public void onSuccess(DriveId driveId) {
                                createFolderInFolder(driveId.asDriveFolder());
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "No folder selected", e);
                        showMessage("folder_not_selected");
                        finish();
                    }
                });
    }

    private void createFolderInFolder(final DriveFolder parent) {
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle("New folder")
                .setMimeType(DriveFolder.MIME_TYPE)
                .setStarred(true)
                .build();

        getDriveResourceClient()
                .createFolder(parent, changeSet)
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveFolder>() {
                            @Override
                            public void onSuccess(DriveFolder driveFolder) {
                                showMessage(getString(R.string.file_created,
                                        driveFolder.getDriveId().encodeToString()));
                                finish();
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to create file", e);
                        showMessage(getString(R.string.file_create_error));
                        finish();
                    }
                });
    }
}
