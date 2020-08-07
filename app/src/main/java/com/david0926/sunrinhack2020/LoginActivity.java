package com.david0926.sunrinhack2020;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.david0926.sunrinhack2020.databinding.ActivityLoginBinding;
import com.david0926.sunrinhack2020.model.UserModel;
import com.david0926.sunrinhack2020.util.UserCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import gun0912.tedkeyboardobserver.TedKeyboardObserver;

public class LoginActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        //scroll to bottom when keyboard up
        new TedKeyboardObserver(this).listen(isShow -> {
            binding.scrollLogin.smoothScrollTo(0, binding.scrollLogin.getBottom());
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null && UserCache.getUser(this) != null)
            finishSignIn();

        //sign in button clicked
        binding.btnLoginSignin.setOnClickListener(view -> {

            binding.setOnProgress(true);
            hideKeyboard(this);

            String id = binding.getEmail(), pw = binding.getPw();

            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pw)) //empty field
                showErrorMsg("Please fill all required fields.");
            else signIn(id, pw);

        });

        //sign up button clicked
        binding.btnLoginRegi.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_up, R.anim.slide_up_before);
        });

        //finish when sign up success
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("finish_signup")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_signup"));
    }

    private void signIn(String id, String pw) {

        //why did I code like this???

        OnCompleteListener<DocumentSnapshot> firestoreCompleteListener = task -> {

            DocumentSnapshot document = task.getResult();
            if (document != null && document.exists()) {

                //3. firebase auth (sign in)
                firebaseAuth
                        .signInWithEmailAndPassword(id, pw)
                        .addOnSuccessListener(authResult -> {
                            UserCache.setUser(this, document.toObject(UserModel.class));
                            finishSignIn();
                        })
                        .addOnFailureListener(e -> {
                            String errorMsg = e.getLocalizedMessage();
                            if (errorMsg == null) return;

                            if (errorMsg.contains("password is invalid"))
                                showErrorMsg("Please enter a valid password.");
                            else showErrorMsg(errorMsg);
                        });

            } else showErrorMsg("User data does not exist.");
        };

        OnCompleteListener<SignInMethodQueryResult> emailCheckCompleteListener = task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<String> signInMethods = task.getResult().getSignInMethods();
                if (signInMethods != null && !signInMethods.isEmpty()) {

                    //2. firestore (user data check)
                    firebaseFirestore
                            .collection("users")
                            .document(id)
                            .get()
                            .addOnCompleteListener(firestoreCompleteListener);

                } else showErrorMsg("User account does not exist.");
            } else showErrorMsg("Please enter a valid email address.");
        };

        //1. firebase auth (account exist check)
        firebaseAuth
                .fetchSignInMethodsForEmail(id)
                .addOnCompleteListener(emailCheckCompleteListener);
    }

    private void finishSignIn() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_down_before, R.anim.slide_down);
        finish();
    }

    private void showErrorMsg(String msg) {
        binding.setOnProgress(false);
        binding.txtLoginError.setVisibility(View.VISIBLE);
        binding.txtLoginError.setText(msg);
        binding.txtLoginError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    private void hideKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_before, R.anim.slide_down);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
