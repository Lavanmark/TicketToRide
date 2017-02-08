package com.floorcorn.tickettoride.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
//import android.apwidget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.presenters.LoginPresenter;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements ILoginView, IView {

	/**
	 * Id to identity READ_CONTACTS permission request.
	 */
	private static final int REQUEST_READ_CONTACTS = 0;

	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[]{
			"foo@example.com:hello", "bar@example.com:world"
	};
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */

    private LoginPresenter presenter;

	// UI references.
	private EditText mUserView;
	private EditText mPasswordView;
	private EditText mFirstNameView;
	private EditText mLastNameView;
	private EditText mNewUsernameView;
	private EditText mNewPasswordView;
	private EditText mConfirmPasswordView;

    private Button mSignInButton;
    private Button mRegisterButton;

	private View mProgressView;
	private View mLoginFormView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Set up the login form.
//		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mUserView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mFirstNameView = (EditText) findViewById(R.id.register_name_first);
        mLastNameView = (EditText) findViewById(R.id.register_name_last);
		mNewUsernameView = (EditText) findViewById(R.id.register_username);
        mNewPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_password_confirm);


        LoginPresenter mPresenter = new LoginPresenter();
        setPresenter(mPresenter);

        presenter.setView(this);


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

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
	}

	@Override
	public String getUsername() {
		return this.mNewUsernameView.getText().toString();
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
        //TODO: display a toast with the message

	}

	@Override
	public void clearView() {
        //TODO: clear text from views.

	}

    @Override
    public void setPresenter(IPresenter presenter) {
        if(presenter instanceof LoginPresenter)
            this.presenter = (LoginPresenter) presenter;
        else
            throw new IllegalArgumentException();
    }


    private interface ProfileQuery {
		String[] PROJECTION = {
				ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
		};

		int ADDRESS = 0;
		int IS_PRIMARY = 1;
	}

}

