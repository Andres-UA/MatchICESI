package com.appmoviles.andres.matchicesi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appmoviles.andres.matchicesi.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;

    private TextView tvRegister;
    private EditText etEmail;
    private EditText etPassword;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        tvRegister = findViewById(R.id.link_signup);
        etEmail = findViewById(R.id.input_email);
        etPassword = findViewById(R.id.input_password);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void load(View view) {
        if (validate() == true ){
            animateButtonWidth();
            fadeOutTextAndSetProgressDialog();
            login();
        }
    }

    private void login(){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if(auth.getCurrentUser().isEmailVerified() == true){
                    nextAction();
                } else {
                    Toast.makeText(LoginActivity.this, "Tu cuenta no ha sido verificada.", Toast.LENGTH_SHORT).show();
                    restoreButton();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "No se pudo ingresar", Toast.LENGTH_SHORT).show();
                restoreButton();
            }
        });
    }

    private boolean validate() {
        boolean valid = true;

        String email = loginBinding.inputEmail.getText().toString();
        String password = loginBinding.inputPassword.getText().toString();

        // Patrón para validar el email
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@correo.icesi.edu.co");
        Matcher mather = pattern.matcher(email);

        if (email.isEmpty()) {
            loginBinding.inputEmail.setError("Este campo es obligatorio.");
            valid = false;
        } else if (mather.find() == false) {
            loginBinding.inputEmail.setError("Este correo no es proporcionado por la universidad.");
            valid = false;
        }

        if (password.isEmpty()) {
            loginBinding.inputEmail.setError("Este campo es obligatorio.");
            valid = false;
        } else if (password.length() < 4) {
            loginBinding.inputEmail.setError("Debe tener más de 10 caracteres");
            valid = false;
        }


        return valid;
    }

    private void restoreButton(){
        restoreButtonWidth();
        fadeInTextAndSetProgressDialog();
    }

    private void restoreButtonWidth(){
        ValueAnimator anim = ValueAnimator.ofInt(getFinalWidth(), getInitialWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = loginBinding.signInBtn.getLayoutParams();
                layoutParams.width = value;
                loginBinding.signInBtn.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private void animateButtonWidth() {
        ValueAnimator anim = ValueAnimator.ofInt(loginBinding.signInBtn.getMeasuredWidth(), getFinalWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = loginBinding.signInBtn.getLayoutParams();
                layoutParams.width = value;
                loginBinding.signInBtn.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private void fadeOutTextAndSetProgressDialog() {
        loginBinding.signInTtx.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showProgressDialog();
            }
        }).start();
    }

    private void fadeInTextAndSetProgressDialog() {
        loginBinding.signInPB.setVisibility(View.INVISIBLE);
        loginBinding.signInTtx.animate().alpha(100f).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        }).start();
    }

    private void nextAction() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealButton();
                fadeOutProgressDialog();
                delayedStartNextAcivity();
            }
        }, 2000);
    }

    private void revealButton() {
        loginBinding.signInBtn.setElevation(0f);
        loginBinding.revealView.setVisibility(View.VISIBLE);

        int x = loginBinding.revealView.getWidth();
        int y = loginBinding.revealView.getHeight();

        int startX = (int) (getFinalWidth() / 2 + loginBinding.signInBtn.getX());
        int startY = (int) (getFinalWidth() / 2 + loginBinding.signInBtn.getY());

        float radius = Math.max(x, y) * 1.2f;

        Animator reveal = ViewAnimationUtils.createCircularReveal(loginBinding.revealView, startX, startY, getFinalWidth(), radius);
        reveal.setDuration(350);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                finish();
            }
        });
        reveal.start();
    }

    private void fadeOutProgressDialog() {
        loginBinding.signInPB.animate().alpha(0f).setDuration(200).start();
    }

    private void delayedStartNextAcivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 100);
    }

    private void showProgressDialog() {
        loginBinding.signInPB.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        loginBinding.signInPB.setVisibility(View.VISIBLE);
    }

    private int getFinalWidth() {
        return (int) getResources().getDimension(R.dimen.get_width);
    }

    private int getInitialWidth() {
        return (int) getResources().getDimension(R.dimen.initial_width);
    }
}
