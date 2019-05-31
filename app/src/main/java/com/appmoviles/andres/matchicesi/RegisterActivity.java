package com.appmoviles.andres.matchicesi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appmoviles.andres.matchicesi.model.SearchPreferences;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNames;
    private EditText etSurnames;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRePassword;
    private EditText etBirthDate;
    private Spinner sCareer;
    private Spinner sGenre;

    private ImageButton btnShowCalendar;
    private FrameLayout flRegister;
    private TextView tvRegister;
    private ProgressBar pbRegister;

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    private static final String CERO = "0";
    private static final String BARRA = "/";
    public final Calendar c = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        etNames = findViewById(R.id.register_names);
        etSurnames = findViewById(R.id.register_surnames);
        etEmail = findViewById(R.id.register_email);
        etPassword = findViewById(R.id.register_password);
        etRePassword = findViewById(R.id.register_re_password);
        etBirthDate = findViewById(R.id.et_birth_date);

        sCareer = findViewById(R.id.s_career);
        sGenre = findViewById(R.id.s_genre);

        flRegister = findViewById(R.id.signInBtn);
        tvRegister = findViewById(R.id.signInTtx);
        pbRegister = findViewById(R.id.signInPB);

        btnShowCalendar = findViewById(R.id.btn_show_birth_date);
        btnShowCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
    }


    private void getDate() {
        final int mes = c.get(Calendar.MONTH);
        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int anio = c.get(Calendar.YEAR);

        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);

                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);

                etBirthDate.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }

        }, anio, mes, dia);
        recogerFecha.show();
    }

    private boolean validate() {
        boolean valid = true;

        String names = etNames.getText().toString();
        String surnames = etSurnames.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String rePassword = etRePassword.getText().toString();
        String birthDate = etBirthDate.getText().toString();
        String career = sCareer.getSelectedItem().toString();
        String genre = sGenre.getSelectedItem().toString();

        // Patrón para validar el email
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@correo.icesi.edu.co*");
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

        if (birthDate.isEmpty()) {
            etBirthDate.setError("Este campo es obligatorio.");
            valid = false;
        }

        if (career.isEmpty() || career.equals("Carrera")) {
            Toast.makeText(getApplicationContext(), "Debe elegir una carrera.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (genre.isEmpty() || genre.equals("Te identificas como")) {
            Toast.makeText(getApplicationContext(), "Debe elegir un genero.", Toast.LENGTH_SHORT).show();
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
        final String birthDate = etBirthDate.getText().toString();
        final String career = sCareer.getSelectedItem().toString();
        final String genre = sGenre.getSelectedItem().toString();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(birthDate);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        final Date newBirthDate = date;


        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(final AuthResult authResult) {
                String uid = auth.getCurrentUser().getUid();
                User user = new User(uid, names, surnames, email, newBirthDate, career, genre, true, "https://i0.wp.com/www.winhelponline.com/blog/wp-content/uploads/2017/12/user.png", "");

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
                                                        fadeOutProgressDialog();
                                                        Toast.makeText(getApplicationContext(),
                                                                "Correo electrónico de verificación enviado a " + email + ".",
                                                                Toast.LENGTH_LONG).show();

                                                        SearchPreferences searchPreferences = new SearchPreferences(18, 30, true, true);

                                                        firestore.collection("search_preferences").document(auth.getCurrentUser().getUid()).set(searchPreferences);

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

                //test
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
