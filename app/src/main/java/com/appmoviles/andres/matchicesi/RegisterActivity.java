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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNames;
    private EditText etSurnames;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRePassword;

    private FrameLayout flRegister;
    private TextView tvRegister;
    private ProgressBar pbRegister;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();

        etNames = findViewById(R.id.register_names);
        etSurnames = findViewById(R.id.register_surnames);
        etEmail = findViewById(R.id.register_email);
        etPassword = findViewById(R.id.register_password);
        etRePassword = findViewById(R.id.register_re_password);

        flRegister = findViewById(R.id.signInBtn);
        tvRegister = findViewById(R.id.signInTtx);
        pbRegister = findViewById(R.id.signInPB);
    }

    private boolean validate() {
        boolean valid = true;

        String names = etNames.getText().toString();
        String surnames = etSurnames.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String rePassword = etRePassword.getText().toString();

        // Patrón para validar el email
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@correo.icesi.edu.co");
        Matcher mather = pattern.matcher(email);

        if (names.isEmpty()) {
            etNames.setError("Este campo es obligatorio.");
            valid = false;
        }

        if (surnames.isEmpty()) {
            etSurnames.setError("Este campo es obligatorio.");
            valid = false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Este campo es obligatorio.");
            valid = false;
        } else if (mather.find() == false) {
            etEmail.setError("Este correo no es proporcionado por la universidad.");
            valid = false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Este campo es obligatorio.");
            valid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Debe tener más de 6 caracteres");
            valid = false;
        }

        if (rePassword.isEmpty()) {
            etRePassword.setError("Este campo es obligatorio.");
            valid = false;
        } else if (password.length() < 6) {
            etRePassword.setError("Debe tener más de 6 caracteres");
            valid = false;
        }

        if (!password.isEmpty() && !rePassword.isEmpty() && !password.equals(rePassword)) {
            etPassword.setError("Las contraseñas deben ser iguales");
            valid = false;
        }

        return valid;
    }

    public void register() {
        final String names = etNames.getText().toString();
        final String surnames = etSurnames.getText().toString();
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();


        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String uid = auth.getCurrentUser().getUid();
                User user = new User(uid, names, surnames, email, false);
                //database.getReference().child("users").child(uid).setValue(user);

                firestore.collection("users").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(names)
                                .build();

                        auth.getCurrentUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {

                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "Correo electrónico de verificación enviado a " + email + ".",
                                                                Toast.LENGTH_LONG).show();
                                                        auth.signOut();
                                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(),
                                                                "No se pudo enviar el correo electrónico de verificación a " + email + ".",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Hubo un error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void doRegister(View view) {
        if (validate()) {
            animateButtonWidth();
            fadeOutTextAndSetProgressDialog();
            nextAction();
        }
        //animateButtonWidth();
        //fadeOutTextAndSetProgressDialog();
        //nextAction();
    }

    private void animateButtonWidth() {
        ValueAnimator anim = ValueAnimator.ofInt(flRegister.getMeasuredWidth(), getFinalWidth());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = flRegister.getLayoutParams();
                layoutParams.width = value;
                flRegister.requestLayout();
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private void fadeOutTextAndSetProgressDialog() {
        tvRegister.animate().alpha(0f).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showProgressDialog();
            }
        }).start();
    }

    private void nextAction() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                delayedStartNextAcivity();
            }
        }, 2000);
    }


    private void fadeOutProgressDialog() {
        pbRegister.animate().alpha(0f).setDuration(200).start();
    }

    private void delayedStartNextAcivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                register();
                fadeOutProgressDialog();
            }
        }, 100);
    }

    private void showProgressDialog() {
        pbRegister.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
        pbRegister.setVisibility(View.VISIBLE);
    }

    private int getFinalWidth() {
        return (int) getResources().getDimension(R.dimen.get_width);
    }

}
