package com.david0926.sunrinhack2020;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.david0926.sunrinhack2020.databinding.ActivityRegisterBinding;
import com.david0926.sunrinhack2020.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedkeyboardobserver.TedKeyboardObserver;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        //finish activity, when back button pressed
        binding.toolbarRegi.setNavigationOnClickListener(view -> finish());

        //scroll to bottom when keyboard up
        new TedKeyboardObserver(this).listen(isShow -> {
            binding.scrollRegi.smoothScrollTo(0, binding.scrollRegi.getBottom());
        });

        //profile edit button clicked
        binding.regiProfile.setOnClickListener(view -> {

            //start image picker
            TedImagePicker
                    .with(this)
                    .showTitle(false)
                    .startAnimation(R.anim.slide_up, R.anim.slide_up_before)
                    .finishAnimation(R.anim.slide_down_before, R.anim.slide_down)
                    .start(this::setProfileImage);
        });

        //sign up button clicked
        binding.btnRegiSignup.setOnClickListener(view -> {

            binding.setOnProgress(true);
            hideKeyboard(this);

            if (TextUtils.isEmpty(binding.getName()) || TextUtils.isEmpty(binding.getEmail())
                    || TextUtils.isEmpty(binding.getPw())) //empty field
                showErrorMsg("Please fill all required fields.");

            else if (!isValidEmail(binding.getEmail())) //invalid email
                showErrorMsg("Please enter a valid email address.");

            else if (!isValidPw(binding.getPw())) //invalid password
                showErrorMsg("Please enter a valid password. (6~24 letters, 0-9 + A-z)");

            else if (binding.regiProfile.getDrawable() == null) //profile image not uploaded
                showErrorMsg("Please upload your profile image.");

            else //confirm success
                createAccount(imageToByte(binding.regiProfile.getDrawable()), binding.getName(), binding.getEmail(), binding.getPw());

        });

    }

    private void createAccount(byte[] profile, String name, String email, String pw) {


        //1. firebase storage (upload profile image)
        storageReference
                .child("profile/" + email + ".png")
                .putBytes(profile)
                .addOnSuccessListener(runnable1 -> {

                    //2. get profile url
                    storageReference.child("profile/" + email + ".png")
                            .getDownloadUrl()
                            .addOnSuccessListener(uri -> {

                                ArrayList<HashMap<String, String>> list = new ArrayList<>();

                                //3. firestore (upload user information)
                                firebaseFirestore
                                        .collection("users")
                                        .document(email)
                                        .set(new UserModel(name, email, timeNow(), uri.toString(), list))
                                        .addOnSuccessListener(runnable -> {

                                            //4. firebase auth (create user)
                                            firebaseAuth
                                                    .createUserWithEmailAndPassword(email, pw)
                                                    .addOnSuccessListener(runnable2 -> finishSignUp())
                                                    .addOnFailureListener(this, e -> showErrorMsg(e.getLocalizedMessage()));
                                        })
                                        .addOnFailureListener(e -> showErrorMsg(e.getLocalizedMessage()));

                            })
                            .addOnFailureListener(e -> showErrorMsg(e.getLocalizedMessage()));
                })
                .addOnFailureListener(e -> showErrorMsg(e.getLocalizedMessage()));
    }

    private void finishSignUp() {
        sendBroadcast(new Intent("finish_signup"));

        binding.animatorRegi.showNext();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }, 1500);
    }

    private void showErrorMsg(String msg) {
        binding.setOnProgress(false);
        binding.txtRegiError.setVisibility(View.VISIBLE);
        binding.txtRegiError.setText(msg);
        binding.txtRegiError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    private boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidPw(String target) {
        //6~24 letters, 0~9 + A-z
        Pattern p = Pattern.compile("(^.*(?=.{6,24})(?=.*[0-9])(?=.*[A-z]).*$)");
        Matcher m = p.matcher(target);
        //except korean letters
        return m.find() && !target.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }

    private void hideKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private String timeNow() {
        return new SimpleDateFormat("yyyy/MM/dd hh:mm aa", Locale.ENGLISH).format(new Date());
    }

    private void setProfileImage(Uri uri) {
        if (getMimeType(uri).equals("image/jpeg") || getMimeType(uri).equals("image/png")) {
            Glide.with(this).load(uri).into(binding.regiProfile);
        } else showErrorMsg("Please upload valid profile image. (jpeg, png)");
    }

    public String getMimeType(Uri uri) {

        String mimeType;
        if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private byte[] imageToByte(Drawable drawable) {

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream);

        return outputStream.toByteArray();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_before, R.anim.slide_down);
    }

}
