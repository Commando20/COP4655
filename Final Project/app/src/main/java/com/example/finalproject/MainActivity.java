package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private SignInButton signInButton;
    private Button signOutButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1;

    TextView signInTip;
    TextView welcome;
    View profileImage;
    TextView name;
    TextView email;
    TextView favoritesTip;
    Button favoritesButton;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInTip = findViewById(R.id.signInTip);
        signInButton = findViewById(R.id.signInButton);
        mAuth = FirebaseAuth.getInstance();
        signOutButton = findViewById(R.id.signOutButton);
        favoritesButton = findViewById(R.id.favoritesButton);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                signInTip.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.INVISIBLE);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Toast.makeText(MainActivity.this,"You are now Logged Out",Toast.LENGTH_SHORT).show();
                welcome.setVisibility(View.INVISIBLE);
                profileImage.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                email.setVisibility(View.INVISIBLE);
                signOutButton.setVisibility(View.INVISIBLE);
                favoritesTip.setVisibility(View.INVISIBLE);
                favoritesButton.setVisibility(View.INVISIBLE);

                signInTip.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
            }
        });

        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent favorites = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(favorites);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        MenuItem favorites = bottomNavigationView.getMenu().getItem(R.id.navigation_favorites).setCheckable(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_search:
                        Intent search = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(search);
                        break;
                    case R.id.navigation_results:
                        Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                        startActivity(results);
                        break;
                    case R.id.navigation_favorites:
                        if (signIn()) {
                            favorites.setCheckable(true);
                            Intent favorites = new Intent(MainActivity.this, FavoritesActivity.class);
                            startActivity(favorites);
                        }else {
                            // Create the object of AlertDialog Builder class
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            // Set Alert Title
                            builder.setTitle("Alert!");
                            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                            builder.setCancelable(false);
                            //Set the message show for the Alert time
                            builder.setMessage("You must first sign in to be able to use this feature.");
                            // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.
                            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // When the user click yes button then app will close
                                    dialog.cancel();
                                }
                            });
                            //Create the Alert dialog
                            AlertDialog alertDialog = builder.create();
                            //Show the Alert Dialog box
                            alertDialog.show();
                        }
                        break;
                }
                return true;
            }
        });
    }

    public boolean signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        return true; //Returns true (You are signed in)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(MainActivity.this,"Signed In Successfully",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(MainActivity.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    public void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        //check if the account is null
        if (acct != null) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        //FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    updateUI();
                }
            });
        }
        else{
            Toast.makeText(MainActivity.this, "acc failed", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint({"SetTextI18n"})
    public void updateUI(){
        welcome = findViewById(R.id.welcome);
        profileImage = findViewById(R.id.profileImage);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        favoritesTip = findViewById(R.id.favoritesTip);
        favoritesButton = findViewById(R.id.favoritesButton);

        favoritesButton.setVisibility(View.VISIBLE);
        favoritesTip.setVisibility(View.VISIBLE);
        welcome.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.VISIBLE);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null){
            //String personGivenName = account.getGivenName();
            //String personFamilyName = account.getFamilyName();
            //String personId = account.getId();
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            String personPhoto = Objects.requireNonNull(account.getPhotoUrl()).toString();

            Glide.with(this).load(personPhoto).into((ImageView) profileImage);
            name.setText(personName);
            email.setText(" (" + personEmail + ")");
        }
    }
}

