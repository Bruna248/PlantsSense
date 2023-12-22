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

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private TextView textViewLoginLink;
    private Button buttonRegister;
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
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        editTextEmail = findViewById(R.id.editTextRegisterEmail);
        editTextPassword = findViewById(R.id.editTextRegisterPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBarRegister);
        textViewLoginLink = findViewById(R.id.textViewLoginLink);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
                buttonRegister.setVisibility(View.GONE);

                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (password.equals(confirmPassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "createUserWithEmail:success");
                                        userViewModel.setUser(mAuth.getCurrentUser());
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        buttonRegister.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords don't match.",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    buttonRegister.setVisibility(View.VISIBLE);
                }

            }
        });

        textViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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