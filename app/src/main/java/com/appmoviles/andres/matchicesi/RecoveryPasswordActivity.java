package com.appmoviles.andres.matchicesi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appmoviles.andres.matchicesi.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecoveryPasswordActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText etEmail;
    TextView tvText;
    FrameLayout btnRecovery;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);

        auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("es");

        etEmail = findViewById(R.id.recovery_email);
        btnRecovery = findViewById(R.id.btn_recovery);
        tvText = findViewById(R.id.tv_recovery);
        progressBar = findViewById(R.id.pb_progressbar);

        btnRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recovery();
            }
        });

    }

    private void recovery() {
        if (validate() == true) {
            animateButtonWidth();
            fadeOutTextAndSetProgressDialog();
            sendEmailRecoveryPassword();
        }
    }

    private void sendEmailRecoveryPassword() {
        String email = etEmail.getText().toString();
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    public void onSuccess(Void result) {
                        Log.d(">>>", "Email sent.");
                        nextAction();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(">>>", "Email cannot sent. " + e.getMessage());
            }
        });
    }

    private boolean validate() {
        boolean valid = true;

        String email = etEmail.getText().toString();

        // Patrón para validar el email
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@correo.icesi.edu.co");
        Matcher mather = pattern.matcher(email);

        if (email.isEmpty()) {
            etEmail.setError("Este campo es obligatorio.");
            valid = false;
        } else if (mather.find() == false) {
            etEmail.setError("Este correo no es proporcionado por la universidad.");
            valid = false;
        }

        return valid;
    }

    private void restoreButton() {
        restoreButtonWidth();
        fadeInTextAndSetProgressDialog();
    }

    private void restoreButtonWidth() {
        ValueAnimator anim = ValueAnimator.ofInt(getFinalWidth(), getInitialWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = btnRecovery.getLayoutParams();
                layoutParams.width = value;
                btnRecovery.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private void animateButtonWidth() {
        ValueAnimator anim = ValueAnimator.ofInt(btnRecovery.getMeasuredWidth(), getFinalWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = btnRecovery.getLayoutParams();
                layoutParams.width = value;
                btnRecovery.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private void fadeOutTextAndSetProgressDialog() {
        tvText.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showProgressDialog();
            }
        }).start();
    }

    private void fadeInTextAndSetProgressDialog() {
        btnRecovery.setVisibility(View.INVISIBLE);
        tvText.animate().alpha(100f).setDuration(250).setListener(new AnimatorListenerAdapter() {
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
                fadeOutProgressDialog();
                delayedStartNextAcivity();
            }
        }, 2000);
    }

    private void fadeOutProgressDialog() {
        btnRecovery.animate().alpha(0f).setDuration(200).start();
    }

    private void delayedStartNextAcivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(RecoveryPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(RecoveryPasswordActivity
                        .this, "Se ha enviado un correo para recuperar tu contraseña", Toast.LENGTH_LONG).show();

            }
        }, 100);
    }

    private void showProgressDialog() {
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);
    }

    private int getFinalWidth() {
        return (int) getResources().getDimension(R.dimen.get_width);
    }

    private int getInitialWidth() {
        return (int) getResources().getDimension(R.dimen.initial_width);
    }

}
