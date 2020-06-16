package org.pytorch.demo.vision.view;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.pytorch.demo.R;

public class DemoCameraActivity extends Activity {

    private Button uploadBtn;
    private Button chooseBtn;
    private ImageView mImageView;
    private static final int CAMERA_REQUEST_CODE = 1;
    private String pictureFilePath;
    private StorageReference mStorageReference;
    private String deviceIdentifier;
    public Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_page);

        uploadBtn = (Button) findViewById(R.id.buttonToEnableCamera);
        chooseBtn = (Button) findViewById(R.id.buttonToChoose);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mStorageReference = FirebaseStorage.getInstance().getReference("Images");


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fileuploader();
            }
        });

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/'");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
               /* Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);*/

            }

        });
    }

    private void Fileuploader() {

        StorageReference Ref = mStorageReference.child(System.currentTimeMillis() + "." + getExtension(imgUri));

        Ref.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //  Uri downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                        Toast.makeText(DemoCameraActivity.this, "Upload finish!!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
        /*     Uri uri = data.getData();
            StorageReference filepath = mStorageReference.child("Photos").child(Objects.requireNonNull(uri.getLastPathSegment()));
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                    Picasso.get().load(downloadUri.toString()).fit().centerCrop().into(mImageView);
                    Toast.makeText(DemoCameraActivity.this, "Upload finish!!", Toast.LENGTH_LONG).show();
               }
            });}*/

            imgUri = data.getData();
            mImageView.setImageURI(imgUri);

        }
    }
}
