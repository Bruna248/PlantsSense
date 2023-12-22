package pt.uc.dei.cm.plantsmc.view.auth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.view.MainActivity;
import pt.uc.dei.cm.plantsmc.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private TextView textViewRegisterLink;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            redirectToMainActivity();
        }
    }

    private void redirectToMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        editTextEmail = findViewById(R.id.editTextLoginEmail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBarLogin);
        textViewRegisterLink = findViewById(R.id.textViewRegisterLink);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                buttonLogin.setVisibility(View.GONE);

                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    userViewModel.setUser(mAuth.getCurrentUser());
                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    buttonLogin.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });

        textViewRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivities(new Intent[]{new Intent(LoginActivity.this, RegisterActivity.class)});
            }
        });

        userViewModel.getCurrentUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    redirectToMainActivity();
                }
            }
        });
    }
}