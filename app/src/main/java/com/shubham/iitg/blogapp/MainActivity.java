package com.shubham.iitg.blogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shubham.iitg.GuidePageActivity2;
import com.shubham.iitg.R;
import com.shubham.iitg.Splash;
import com.shubham.iitg.activity.About;
import com.shubham.iitg.activity.Contact;
import com.shubham.iitg.activity.EDITActivity;
import com.shubham.iitg.activity.Help;
//import com.shubham.iitg.notepad.activities.home.HomeActivity;
import com.shubham.iitg.notepad.note_list.NotepadListActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import at.blogc.android.views.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.bloco.faker.components.Name;
import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolBar;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    ExpandableTextView expand;
    FloatingActionButton add_post_btn,download;
    private String user_id;
    private Uri main_uri = null;
    private Uri default_uri = null;
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private NotificationFragment notificationFragment;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    private ProgressDialog mLoginProgress;

    private StorageReference mStorageRef;
    Button Submit;
    private FirebaseFirestore fireStore;
    private Bitmap compressedImageBitmap;

    CircleImageView profile;
    private DatabaseReference mUserDatabase,mDatabase;
    TextView emailheader,ageheader,nameheader;
    ImageView edit;
    private boolean isChanged = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainblog);



//        toolBar =  findViewById(R.id.toolbar);
//        setSupportActionBar(toolBar);
//
//        getSupportActionBar().setTitle("Photo Blog");

        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Learning Portal");

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mLoginProgress = new ProgressDialog(this,R.style.dialog);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);



        navigationView = findViewById(R.id.navbar);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
try{
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();}catch (Exception e){e.printStackTrace();
        Intent i=new Intent(getApplicationContext(),GuidePageActivity2.class);
        startActivity(i);
    CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");

}
        mStorageRef = FirebaseStorage.getInstance().getReference();
        fireStore = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireStore.setFirestoreSettings(settings);

        View headerView = navigationView.getHeaderView(0);
        nameheader = headerView.findViewById(R.id.name);
        emailheader = headerView.findViewById(R.id.email);
        profile = headerView.findViewById(R.id.profile_pic);
        ageheader = headerView.findViewById(R.id.age);
        edit=headerView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AccounrSetup.class));
            }
        });

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {

            ///////////////


            fireStore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            isChanged = false;
                            final String name = task.getResult().getString("name");
                           final String age = task.getResult().getString("age");
                            final String image = task.getResult().getString("image");
//                        Toast.makeText(AccounrSetup.this,"DATA EXISTS",Toast.LENGTH_LONG).show();
                            main_uri = Uri.parse(image);
                            //putValue(name,age,hostel,roll,image);
                            nameheader.setText(name);

                            String email=currentUser.getEmail();
                            emailheader.setText(email);
                            ageheader.setText(age);
                            Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
                                    .placeholder(R.drawable.default_avatar).into(profile, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(profile);
                                }

                            });
                        }
                        else{
                            main_uri = default_uri;
                            Toast.makeText(MainActivity.this,"NO DATA EXISTS", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Firestore Retrieve Error OUTSIDE", Toast.LENGTH_LONG).show();
                    }
                }
            });


            ///////////////
        }else{sendLogin();}
        if(mAuth.getCurrentUser() != null) {


         bottomNavigationView = findViewById(R.id.mainBottomNav);

         //fragments
         homeFragment = new HomeFragment();
         notificationFragment = new NotificationFragment();
         accountFragment = new AccountFragment();

         replaceFragment(homeFragment);

         //Onclick on Bottom Nav Bar
         bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 switch (item.getItemId()) {

                     case R.id.bottomAccount:
                         replaceFragment(accountFragment);
                         return true;


                     case R.id.bottomHome:
                         replaceFragment(homeFragment);
                         return true;


                     case R.id.bottomNotification:
                         replaceFragment(notificationFragment);
                         return true;
                 }
                 return false;
             }
         });




         add_post_btn = findViewById(R.id.addPostButton);
         download= findViewById(R.id.download);
         download.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(), downloader.video.xvlover.videodownloader.MainActivity.class));
             }
         });
         add_post_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                 if (currentUser != null) {
                     user_id = mAuth.getCurrentUser().getUid();
                     firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                             if (task.getResult().exists()) {
                                 Intent addPost = new Intent(MainActivity.this, NewPost.class);
                                 startActivity(addPost);
                             } else {
                                 Toast.makeText(MainActivity.this, "Please choose profile photo and name", Toast.LENGTH_LONG).show();
                                 Intent main = new Intent(MainActivity.this, AccounrSetup.class);
                                 startActivity(main);
                             }
                         }
                     });
                 } else {
                     Toast.makeText(MainActivity.this, "Please choose profile photo and name", Toast.LENGTH_LONG).show();
                     Intent main = new Intent(MainActivity.this, AccounrSetup.class);
                     startActivity(main);
                 }


             }
         });

     }
    }


    private void putValues(final String name) {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        String email=current_user.getEmail();
        emailheader.setText(email);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child(name);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String value = dataSnapshot.getValue(String.class);
                if(name.equals("name")){
                    nameheader.setText(value);
                }
                if(name.equals("age")){
                    ageheader.setText("Age : "+value+" Y");
                }if(name.equals("thumb_image")){
                    Picasso.get().load(value).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_avatar).into(profile, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(value).placeholder(R.drawable.default_avatar).into(profile);
                        }

                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        //Check if user logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            sendLogin();
            finish();
        }

    }

    private void sendLogin() {
       Intent ash = new Intent(MainActivity.this, GuidePageActivity2.class);
        startActivity(ash);
    }

    //add menu drawable resource to action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                logOut();
                return true;

            case R.id.action_settings:
                Intent acS = new Intent(MainActivity.this,AccounrSetup.class);
                startActivity(acS);

                return true;

                default:
                    return false;
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_dashboard: startActivity(new Intent(this, MainActivity.class));
                CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");
                return true;
            case R.id.action_notes:startActivity(new Intent(this, NotepadListActivity.class));
                CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");
                return true;

            case R.id.action_share:shareapp();return true;

            case R.id.action_about:startActivity(new Intent(this, About.class));CustomIntent.customType(MainActivity.this,"fadein-to-fadeout"); return true;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareapp() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "EducateMe");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_text));
        startActivity(Intent.createChooser(sharingIntent, "Share app via"));
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();


    }

    private void sendToStart() {

        Intent startIntent = new Intent(getApplicationContext(), GuidePageActivity2.class);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startIntent);


    }

    private void logOut() {
        mAuth.signOut();
        sendLogin();
    }

    //Fragment transition to change fragment when pressed
    private void replaceFragment(android.support.v4.app.Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,fragment);
        fragmentTransaction.commit();
    }

}
