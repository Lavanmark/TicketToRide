package com.floorcorn.tickettoride.ui.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.presenters.LoginPresenter;
import com.floorcorn.tickettoride.ui.views.ILoginView;

//import android.apwidget.ArrayAdapter;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements ILoginView {

    private LoginPresenter presenter;

	// UI references.
	private EditText mUserView;
	private EditText mPasswordView;
	private EditText mFirstNameView;
	private EditText mLastNameView;
	private EditText mNewUsernameView;
	private EditText mNewPasswordView;
	private EditText mConfirmPasswordView;
    private EditText mIPAddressView;
    private EditText mPortView;

    private Button mSignInButton;
    private Button mRegisterButton;

	private View mProgressView;
	private View mLoginFormView;

    private boolean passwordEntered;
    private boolean usernameEntered;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Set up the login form.

		//new Corn("AppLog.log");
		
        mUserView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mFirstNameView = (EditText) findViewById(R.id.register_name_first);
        mLastNameView = (EditText) findViewById(R.id.register_name_last);
		mNewUsernameView = (EditText) findViewById(R.id.register_username);
        mNewPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_password_confirm);
        mIPAddressView = (EditText) findViewById(R.id.ip_address);
        mPortView = (EditText) findViewById(R.id.port_number);

        passwordEntered = false;
        usernameEntered = false;

        //Set listeners for buttons and form fields
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginClicked();
            }
        });

        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.registerClicked();
            }
        });

        mUserView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameEntered = true;
                if(mUserView.getText().toString().length() < 1){
                    usernameEntered = false;
                }
                mSignInButton.setEnabled(usernameEntered && passwordEntered);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEntered = true;
                if(mPasswordView.getText().toString().length() < 1){
                    passwordEntered = false;
                }
                mSignInButton.setEnabled(usernameEntered && passwordEntered);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mFirstNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegisterButton.setEnabled(registerFormComplete());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLastNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegisterButton.setEnabled(registerFormComplete());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNewUsernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegisterButton.setEnabled(registerFormComplete());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNewPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegisterButton.setEnabled(registerFormComplete());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mConfirmPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRegisterButton.setEnabled(registerFormComplete());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Disable buttons at first
        mSignInButton.setEnabled(false);
        mRegisterButton.setEnabled(false);

        LoginPresenter mPresenter = new LoginPresenter();
        setPresenter(mPresenter);

        presenter.setView(this);
		presenter.clearObservers();
		presenter.register();
	}

    @Override
    protected void onResume(){
        super.onResume();
        this.clearView();
	    presenter.clearObservers();
	    presenter.register();
    }

	public void onStop() {
		super.onStop();
	}

    /**
     * This method checks all of the fields to determine if there is enough information to
     * register
     *
     * @return a boolean indicating whether or not there is enough information to submit
     *
     * @pre text has been altered in one of the UI fields.
     * @post the returned value is false if there is not enough data. Else true.
     */
    private boolean registerFormComplete()
    {
        if(mFirstNameView.getText().toString().length() < 1)
            return false;
        if(mLastNameView.getText().toString().length()< 1)
            return false;
        if(mNewUsernameView.getText().toString().length() < 1)
            return false;
        if(mNewPasswordView.getText().toString().length() < 1)
            return false;
        if(mConfirmPasswordView.getText().toString().length() < 1)
            return false;
        return true;
    }

	@Override
	public String getUsername() {
		return this.mUserView.getText().toString();
	}

	@Override
	public String getPassword() {
		return this.mPasswordView.getText().toString();
	}

	@Override
	public String getNewPassword() {
		return this.mNewPasswordView.getText().toString();
	}

	@Override
	public String getConfirmPassword() {
		return this.mConfirmPasswordView.getText().toString();
	}

    @Override
    public String getFirstName() {
        return this.mFirstNameView.getText().toString();
    }

    @Override
    public String getLastName() {
        return this.mLastNameView.getText().toString();
    }


	@Override
	public String getNewUsername() {
		return this.mNewUsernameView.getText().toString();
	}

	@Override
	public void displayMessage(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();

	}

	@Override
	public void clearView() {
		this.mUserView.setText(null);
		this.mPasswordView.setText(null);
		this.mFirstNameView.setText(null);
		this.mLastNameView.setText(null);
		this.mNewUsernameView.setText(null);
		this.mNewPasswordView.setText(null);
		this.mConfirmPasswordView.setText(null);

	}

    @Override
    public void launchNextActivity() {
        Intent i = new Intent(LoginActivity.this, GameListActivity.class);
	    //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    presenter.unregister();
        startActivity(i);
    }

    @Override
    public String getIP() {
        return this.mIPAddressView.getText().toString();
    }

    @Override
    public String getPort() {
        return this.mPortView.getText().toString();
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        if(presenter instanceof LoginPresenter)
            this.presenter = (LoginPresenter) presenter;
        else
            throw new IllegalArgumentException();
    }

	@Override
	public void backToLogin() {
		System.out.println("already at login!");
	}

	@Override
	public Activity getActivity() {
		return LoginActivity.this;
	}


}

