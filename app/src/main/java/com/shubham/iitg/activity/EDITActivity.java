package com.shubham.iitg.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shubham.iitg.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EDITActivity extends AppCompatActivity {
    EditText name, age, roll, hostel;
    Button btn;
    CircleImageView pic;
    ImageView edit;
    TextView email;
    private FirebaseAuth mAuth;


    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;

    private ProgressDialog mProgressDialog;
    FirebaseUser mCurrentUser;

    private DatabaseReference mUserDatabase, mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        roll = findViewById(R.id.roll);
        hostel = findViewById(R.id.hostel);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        btn = findViewById(R.id.btn);
        edit = findViewById(R.id.edit);
        pic = findViewById(R.id.pic);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadimg();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText().toString().trim())) {
                    name.setError("Enter Name!");
                }
                if (TextUtils.isEmpty(age.getText().toString().trim())) {
                    age.setError("Enter Age!");
                }
                if (TextUtils.isEmpty(age.getText().toString().trim())) {
                    age.setError("Enter Hostel!");
                }
                if (TextUtils.isEmpty(age.getText().toString().trim())) {
                    age.setError("Enter Roll!");
                }
                if (!TextUtils.isEmpty(name.getText().toString().trim())
                        && !TextUtils.isEmpty(age.getText().toString().trim())) {
                    sendnewvalues("name");
                    sendnewvalues("age");
                    sendnewvalues("hostel");
                    sendnewvalues("roll");
                }
            }
        });
        putall();
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EDITActivity.this, "SORRY YOU CANNOT EDIT EMAIL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void putall() {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        String emailis = current_user.getEmail();
        email.setText(emailis);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mUserDatabase.keepSynced(true);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String nameis = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String ageis = dataSnapshot.child("age").getValue().toString();
                final String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                final String hostelis = dataSnapshot.child("hostel").getValue().toString();
                final String rollis = dataSnapshot.child("roll").getValue().toString();
                hostel.setText(hostelis);
                roll.setText(rollis);
                name.setText(nameis);
                age.setText(ageis);

                if (!image.equals("default")) {

                    //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                    Picasso.get().load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_avatar).into(pic, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(thumb_image).placeholder(R.drawable.default_avatar).into(pic);
                        }

                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void uploadimg() {


        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_LONG).show();

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                mProgressDialog = new ProgressDialog(EDITActivity.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setMessage("Please wait while we upload and process the image.");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();


                final String uid = current_user.getUid();


                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                StorageReference filepath = mImageStorage.child("profile_images").child(uid + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(uid + ".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            final String download_url = Objects.requireNonNull(task.getResult().getDownloadUrl()).toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumb_downloadUrl = Objects.requireNonNull(thumb_task.getResult().getDownloadUrl()).toString();
                                    Toast.makeText(EDITActivity.this, thumb_downloadUrl, Toast.LENGTH_SHORT).show();
                                    if (thumb_task.isSuccessful()) {

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("image", download_url);
                                        update_hashMap.put("thumb_image", thumb_downloadUrl);
                                        Toast.makeText(EDITActivity.this, "" + thumb_downloadUrl, Toast.LENGTH_SHORT).show();

                                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                        mUserDatabase.keepSynced(true);

                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(EDITActivity.this, "Success Uploading.", Toast.LENGTH_LONG).show();
                                                    putall();
                                                }

                                            }
                                        });


                                    } else {

                                        Toast.makeText(EDITActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();

                                    }


                                }
                            });


                        } else {

                            Toast.makeText(EDITActivity.this, "Error in uploading.", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();

                        }

                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void sendnewvalues(String str) {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child(str);
        if (str.equals("name"))
            mDatabase.setValue(name.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(EDITActivity.this, "SAVED", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), com.shubham.iitg.blogapp.MainActivity.class));
                }
            });
        if (str.equals("age"))
            mDatabase.setValue(age.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(EDITActivity.this, "SAVED", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), com.shubham.iitg.blogapp.MainActivity.class));
                }
            });
        if (str.equals("hostel"))
            mDatabase.setValue(hostel.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(EDITActivity.this, "SAVED", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), com.shubham.iitg.blogapp.MainActivity.class));
                }
            });
        if (str.equals("roll"))
            mDatabase.setValue(roll.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(EDITActivity.this, "SAVED", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), com.shubham.iitg.blogapp.MainActivity.class));
                }
            });
    }


}
