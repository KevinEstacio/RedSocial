package com.views.redsocial.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.views.redsocial.R;
import com.views.redsocial.models.User;
import com.views.redsocial.providers.AuthProvider;
import com.views.redsocial.providers.UsersProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView mCircleImageView;
    TextInputEditText kTextInputUsername, kTextInputEmail, kTextInputPassword, kTextInputConfirmarPassword, kTextInputPhone;
    Button kButtonRegister;
    //FirebaseAuth kAuth;
    //FirebaseFirestore kFirestore;
    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        kTextInputUsername = findViewById(R.id.textInputUserName);
        kTextInputEmail = findViewById(R.id.textInputCorreo);
        kTextInputPassword = findViewById(R.id.textInputContra);
        kTextInputConfirmarPassword = findViewById(R.id.textInputConfirmarContra);
        kTextInputPhone = findViewById(R.id.textInputPhone);
        kButtonRegister = findViewById(R.id.btnRegister);
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        //kAuth = FirebaseAuth.getInstance();
        mAuthProvider = new AuthProvider();
        //kFirestore = FirebaseFirestore.getInstance();
        mUsersProvider = new UsersProvider();

        kButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        mCircleImageView = findViewById(R.id.circleImageBack);
        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void register() {
        String username = kTextInputUsername.getText().toString();
        String email = kTextInputEmail.getText().toString();
        String password = kTextInputPassword.getText().toString();
        String confirmarpassword = kTextInputConfirmarPassword.getText().toString();
        String phone = kTextInputPhone.getText().toString();
        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmarpassword.isEmpty() && !phone.isEmpty()) {
            if (isEmailValid(email)) {
                if (password.equals(confirmarpassword)) {
                    if (password.length() >= 6) {
                        createUser(username, email, password,phone);
                    } else {
                        Toast.makeText(this, "Las contraseñas deben tener al menos 6 caracteres ", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(this, "Las contraseñas no coinsiden", Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(this, "Todo correcto pero el email no es valido", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(this, "Faltan datos", Toast.LENGTH_LONG).show();
        }
    }

    private void createUser(final String username, final String email, final String password, final String phone) {
        mDialog.show();
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuthProvider.getUid();
                    /*
                    Map<String, Object> map = new HashMap<>();
                    map.put("email",email);
                    map.put("password",password);
                    map.put("username",username);
                     */
                    User user = new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setPhone(phone);
                    user.setTimestamp(new Date().getTime());
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "No se pudo almacenar el usuario correctamente  ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    mDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}