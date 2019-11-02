package com.shubham.iitg;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shubham.iitg.blogapp.MainActivity;
import com.shubham.wowoviewpager.Animation.WoWoAlphaAnimation;
import com.shubham.wowoviewpager.Animation.WoWoElevationAnimation;
import com.shubham.wowoviewpager.Animation.WoWoPathAnimation;
import com.shubham.wowoviewpager.Animation.WoWoRotationAnimation;
import com.shubham.wowoviewpager.Animation.WoWoScaleAnimation;
import com.shubham.wowoviewpager.Animation.WoWoShapeColorAnimation;
import com.shubham.wowoviewpager.Animation.WoWoTextViewColorAnimation;
import com.shubham.wowoviewpager.Animation.WoWoTextViewTextAnimation;
import com.shubham.wowoviewpager.Animation.WoWoTranslationAnimation;
import com.shubham.wowoviewpager.Enum.Ease;
import com.shubham.wowoviewpager.WoWoPathView;

import java.util.HashMap;

public class GuidePageActivity2 extends WoWoActivity {
    private static final int MYcode = 78;
    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    private DatabaseReference mUserDatabase,mDatabase;
    private int r;
    private boolean animationAdded = false;
    private ImageView targetPlanet;
    private View loginLayout,loginLayout1,loginLayout2,loginLayout3,loginLayout4;
    Button login,register;
    MaterialEditText email, password;
    TextView forgot,help;
    @Override
    protected int contentViewRes() {
        return R.layout.activity_guide_page2;
    }

    @Override
    protected int fragmentNumber() {
        return 4;
    }

    @Override
    protected Integer[] fragmentColorsRes() {
        return new Integer[]{
                R.color.white,
                R.color.white,
                R.color.white,
                R.color.white,
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        r = (int)Math.sqrt(screenW * screenW + screenH * screenH) + 10;

        mLoginProgress = new ProgressDialog(this,R.style.dialog);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        forgot=findViewById(R.id.forgot);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        help=findViewById(R.id.help);
        ///=====

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(password.getText().length()<8){password.setError("min 8 digits");}
                }else{password.setError(null);}
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailis = email.getText().toString();
                String passwordis = password.getText().toString();
                if(TextUtils.isEmpty(emailis)){email.setError("Enter email!");}
                if(TextUtils.isEmpty(passwordis)){password.setError("Enter password!");}
                if(!TextUtils.isEmpty(emailis) && !TextUtils.isEmpty(passwordis)) {
                    password.setError(null);
                    email.setError(null);
                    mLoginProgress.setTitle("Registering User");
                    mLoginProgress.setMessage("Please wait while we create your account !");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    register_user( emailis, passwordis);
                }else{
                    Toast.makeText(GuidePageActivity2.this, "Please Enter Details first", Toast.LENGTH_SHORT).show();
                }
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailis = email.getText().toString();
                String passwordis = password.getText().toString();
                if(TextUtils.isEmpty(emailis)){email.setError("Enter email!");}
                if(TextUtils.isEmpty(passwordis)){password.setError("Enter password!");}
                if(!TextUtils.isEmpty(emailis) && !TextUtils.isEmpty(passwordis)) {
                    password.setError(null);
                    email.setError(null);
                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    loginUser(emailis, passwordis);
                }else{Toast.makeText(GuidePageActivity2.this, "Please Enter Details first", Toast.LENGTH_SHORT).show();}

            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailis = email.getText().toString();
                if(!TextUtils.isEmpty(emailis)){
                    password.setError(null);
                    email.setError(null);
                    mAuth.sendPasswordResetEmail(emailis).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Check Your mail and reset your password",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{ email.setError("Enter email !");
                    password.setError(null);

                    Toast.makeText(getApplicationContext(),"Please enter a email..",Toast.LENGTH_SHORT).show();}
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makedialog();
            }
        });
        ImageView earth = (ImageView) findViewById(R.id.earth);
        targetPlanet = (ImageView) findViewById(R.id.planet_target);
        loginLayout = findViewById(R.id.login_layout);
        loginLayout1 = findViewById(R.id.login_layout);
        loginLayout2 = findViewById(R.id.login_layout);
        loginLayout3 = findViewById(R.id.login_layout);
        loginLayout4 = findViewById(R.id.login_layout);
        earth.setY(screenH / 2);
        targetPlanet.setY(-screenH / 2 - screenW / 2);
        targetPlanet.setScaleX(0.25f);
        targetPlanet.setScaleY(0.25f);

        wowo.addTemporarilyInvisibleViews(0, earth, findViewById(R.id.cloud_blue), findViewById(R.id.cloud_red));
        wowo.addTemporarilyInvisibleViews(0, targetPlanet);
        wowo.addTemporarilyInvisibleViews(2, loginLayout, findViewById(R.id.login));
        wowo.addTemporarilyInvisibleViews(2, loginLayout1, findViewById(R.id.register));
        wowo.addTemporarilyInvisibleViews(2, loginLayout2, findViewById(R.id.forgot));
        wowo.addTemporarilyInvisibleViews(2, loginLayout2, findViewById(R.id.help));


    }

    private void makedialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(R.layout.dialog_option)
                .setNegativeButton("OK", null)
                .show();

    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        addAnimations();
    }

    private void register_user( String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", "Default");
                    userMap.put("status", "Hi there I'm using Educateme App.");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("age", "18");
                    userMap.put("hostel", "default");
                    userMap.put("roll", "default");
                    userMap.put("department", "default");
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                mLoginProgress.dismiss();
                                sendEmailVerification();
                                Toast.makeText(getApplicationContext(),"Registered!! Please verify Email Confirmation and Login",Toast.LENGTH_LONG).show();

                            }

                        }
                    });


                } else {

                    mLoginProgress.hide();
                    Toast.makeText(getApplicationContext(), "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }


    private void sendEmailVerification() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Check your Email", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                    }

                    else{
                        Toast.makeText(getApplicationContext(),"ERROR OCCURED TRY AGAIN!!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
    private void loginUser(String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    mLoginProgress.dismiss();

                    String current_user_id = mAuth.getCurrentUser().getUid();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                            }else{Toast.makeText(getApplicationContext(),"Please Verify Email or enter correct details",Toast.LENGTH_SHORT).show();}
                            finish();


                        }
                    });




                } else {

                    mLoginProgress.hide();

                    String task_result = task.getException().getMessage().toString();

                    Toast.makeText(getApplicationContext(), "Error : " + task_result, Toast.LENGTH_LONG).show();

                }

            }
        });


    }



    private void addAnimations() {
        if (animationAdded) return;
        animationAdded = true;

        addEarthAnimation();
        addCloudAnimation();
        addTextAnimation();
        addRocketAnimation();
        addCircleAnimation();
        addMeteorAnimation();
        addPlanetAnimation();
        addPlanetTargetAnimation();
        addLoginLayoutAnimation();
        addButtonAnimation();
        addEditTextAnimation();

        wowo.ready();

        // Do this the prevent the edit-text and button views on login layout
        // to intercept the drag event.
        wowo.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                loginLayout.setEnabled(position == 3);
                loginLayout.setVisibility(position + positionOffset <= 2 ? View.INVISIBLE : View.VISIBLE);
                loginLayout1.setEnabled(position == 3);
                loginLayout1.setVisibility(position + positionOffset <= 2 ? View.INVISIBLE : View.VISIBLE);
                loginLayout2.setEnabled(position == 3);
                loginLayout2.setVisibility(position + positionOffset <= 2 ? View.INVISIBLE : View.VISIBLE);
                loginLayout3.setEnabled(position == 3);
                loginLayout3.setVisibility(position + positionOffset <= 2 ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }

    private void addEarthAnimation() {
        View earth = findViewById(R.id.earth);
        wowo.addAnimation(earth)
                .add(WoWoRotationAnimation.builder().page(0).keepX(0).keepY(0).fromZ(0).toZ(180).ease(Ease.OutBack).build())
                .add(WoWoRotationAnimation.builder().page(1).keepX(0).keepY(0).fromZ(180).toZ(720).ease(Ease.OutBack).build())
                .add(WoWoRotationAnimation.builder().page(2).keepX(0).keepY(0).fromZ(720).toZ(1260).ease(Ease.OutBack).build())
                .add(WoWoScaleAnimation.builder().page(1).fromXY(1).toXY(0.5).ease(Ease.OutBack).build())
                .add(WoWoScaleAnimation.builder().page(2).fromXY(0.5).toXY(0.25).ease(Ease.OutBack).build());
    }

    private void addCloudAnimation() {
        wowo.addAnimation(findViewById(R.id.cloud_blue))
                .add(WoWoTranslationAnimation.builder().page(0).fromX(screenW).toX(0).keepY(0).ease(Ease.OutBack).sameEaseBack(false).build())
                .add(WoWoTranslationAnimation.builder().page(1).fromX(0).toX(screenW).keepY(0).ease(Ease.InBack).sameEaseBack(false).build());

        wowo.addAnimation(findViewById(R.id.cloud_red))
                .add(WoWoTranslationAnimation.builder().page(0).fromX(-screenW).toX(0).keepY(0).ease(Ease.OutBack).sameEaseBack(false).build())
                .add(WoWoTranslationAnimation.builder().page(1).fromX(0).toX(-screenW).keepY(0).ease(Ease.InBack).sameEaseBack(false).build());

        wowo.addAnimation(findViewById(R.id.cloud_yellow))
                .add(WoWoTranslationAnimation.builder().page(0).keepX(0).fromY(0).toY(dp2px(50)).ease(Ease.OutBack).sameEaseBack(false).build())
                .add(WoWoTranslationAnimation.builder().page(1).fromX(0).toX(-screenW).keepY(dp2px(50)).ease(Ease.InBack).sameEaseBack(false).build());
    }

    private void addTextAnimation() {
        View text = findViewById(R.id.text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) text.setZ(50);
        String[] texts = new String[]{
                "BOOKS?",
                "NOTES?",
                "Lets Transform More!",
                "Let's Discover More!",
        };
        wowo.addAnimation(text)
                .add(WoWoTextViewTextAnimation.builder().page(0).from(texts[0]).to(texts[1]).build())
                .add(WoWoTextViewTextAnimation.builder().page(1).from(texts[1]).to(texts[2]).build())
                .add(WoWoTextViewTextAnimation.builder().page(2).from(texts[2]).to(texts[3]).build())
                .add(WoWoTextViewColorAnimation.builder().page(1).from("#05502f").to(Color.WHITE).build());
    }

    private void addRocketAnimation() {
        WoWoPathView pathView = (WoWoPathView) findViewById(R.id.path_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) pathView.setZ(50);

        // For different screen size, try to adjust the scale values to see the airplane.
        float xScale = 1;
        float yScale = 1;

        pathView.newPath()
                .pathMoveTo(xScale * (-100), screenH - 100)
                .pathCubicTo(screenW / 2, screenH - 100,
                        screenW / 2, screenH - 100,
                        screenW / 2, yScale * (-100));
        wowo.addAnimation(pathView)
                .add(WoWoPathAnimation.builder().page(0).from(0).to(0.50).path(pathView).build())
                .add(WoWoPathAnimation.builder().page(1).from(0.50).to(0.75).path(pathView).build())
                .add(WoWoPathAnimation.builder().page(2).from(0.75).to(1).path(pathView).build())
                .add(WoWoAlphaAnimation.builder().page(2).from(1).to(0).build());
    }

    private void addCircleAnimation() {
        View circle = findViewById(R.id.circle);
        wowo.addAnimation(circle)
                .add(WoWoScaleAnimation.builder().page(1).fromXY(1).toXY(r * 2 / circle.getWidth()).build())
                .add(WoWoShapeColorAnimation.builder().page(1).from("#f9dc0a").to("#05502f").build());
    }

    private void addMeteorAnimation() {
        View meteor = findViewById(R.id.meteor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) meteor.setZ(50);
        float fullOffset = screenW + meteor.getWidth();
        float offset = fullOffset / 2;
        wowo.addAnimation(meteor)
                .add(WoWoTranslationAnimation.builder().page(1)
                        .fromX(0).fromY(0)
                        .toX(offset).toY(offset).ease(Ease.OutBack).sameEaseBack(false).build())
                .add(WoWoTranslationAnimation.builder().page(2)
                        .fromX(offset).fromY(offset)
                        .toX(fullOffset).toY(fullOffset).ease(Ease.InBack).sameEaseBack(false).build());
    }

    private void addPlanetAnimation() {
        View planet0 = findViewById(R.id.planet_0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) planet0.setZ(50);
        wowo.addAnimation(planet0)
                .add(WoWoTranslationAnimation.builder().page(1)
                        .keepX(0)
                        .fromY(0).toY(planet0.getHeight() + 200)
                        .ease(Ease.OutBack).sameEaseBack(false).build())
                .add(WoWoTranslationAnimation.builder().page(2)
                        .fromX(0).toX(screenW)
                        .keepY(planet0.getHeight() + 200)
                        .ease(Ease.InBack).sameEaseBack(false).build());

        View planet1 = findViewById(R.id.planet_1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) planet1.setZ(50);
        wowo.addAnimation(planet1)
                .add(WoWoTranslationAnimation.builder().page(1)
                        .fromX(0).toX(-planet1.getWidth())
                        .keepY(0)
                        .ease(Ease.OutBack).sameEaseBack(false).build())
                .add(WoWoTranslationAnimation.builder().page(2)
                        .fromX(-planet1.getWidth()).toX(-screenW - planet1.getWidth())
                        .keepY(0)
                        .ease(Ease.InBack).sameEaseBack(false).build());
    }

    private void addPlanetTargetAnimation() {
        wowo.addAnimation(targetPlanet)
                .add(WoWoRotationAnimation.builder().page(1).keepX(0).keepY(0).fromZ(0).toZ(180).ease(Ease.OutBack).build())
                .add(WoWoRotationAnimation.builder().page(2).keepX(0).keepY(0).fromZ(180).toZ(360).ease(Ease.OutBack).build())
                .add(WoWoTranslationAnimation.builder().page(0).keepX(0)
                        .fromY(-screenH / 2 - screenW / 2)
                        .toY(-screenH / 2).ease(Ease.OutBack).sameEaseBack(false).build())
                .add(WoWoScaleAnimation.builder().page(1).fromXY(0.25).toXY(0.5).ease(Ease.OutBack).build())
                .add(WoWoScaleAnimation.builder().page(2).fromXY(0.5).toXY(1).ease(Ease.OutBack).build());
    }

    private void addLoginLayoutAnimation() {
        View layout = findViewById(R.id.login_layout);
        wowo.addAnimation(layout)
                .add(WoWoAlphaAnimation.builder().page(1).start(1).end(1).from(0).to(1).build())
                .add(WoWoShapeColorAnimation.builder().page(2).from("#05502f").to("#0aa05f").build())
                .add(WoWoElevationAnimation.builder().page(2).from(0).to(40).build());
    }

    private void addButtonAnimation() {
        View login = findViewById(R.id.login);
        View register = findViewById(R.id.register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) login.setZ(50);
        wowo.addAnimation(login)
                .add(WoWoAlphaAnimation.builder().page(2).from(0).to(1).build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) register.setZ(50);
        wowo.addAnimation(register)
                .add(WoWoAlphaAnimation.builder().page(2).from(0).to(1).build());
    }

    private void addEditTextAnimation() {
        wowo.addAnimation(findViewById(R.id.email))
                .add(WoWoAlphaAnimation.builder().page(2).from(0).to(1).build());
        wowo.addAnimation(findViewById(R.id.password))
                .add(WoWoAlphaAnimation.builder().page(2).from(0).to(1).build());
        wowo.addAnimation(findViewById(R.id.forgot))
                .add(WoWoAlphaAnimation.builder().page(2).from(0).to(1).build());
        wowo.addAnimation(findViewById(R.id.help))
                .add(WoWoAlphaAnimation.builder().page(2).from(0).to(1).build());
    }
}
