package com.example.extrainfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {
    Button LoginButton, RegistrationButton;
    EditText Email, Password;
    String EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String TempPassword="NOT_FOUND";
    public static final String UserEmail="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton=(Button)findViewById(R.id.buttonLogin);
        RegistrationButton=(Button)findViewById(R.id.buttonRegister);

        Email = (EditText)findViewById(R.id.editEmail);
        Password =(EditText)findViewById(R.id.editPassword);

        sqLiteHelper = new SQLiteHelper(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEditTextStatus();

                LoginFunction();
            }
        });


        RegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(loginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

    public void LoginFunction(){
        if(EditTextEmptyHolder){

            sqLiteDatabaseObj=sqLiteHelper.getWritableDatabase();

            cursor=sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null,""+SQLiteHelper.Table_Column_2_Email+"=?",new String[]{EmailHolder},null,null,null);

            while (cursor.moveToNext()){
                if(cursor.isFirst()){
                    cursor.moveToFirst();

                    TempPassword=cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3_password));

                    cursor.close();
                }
            }

            CheckFinalResult();

        }
        else {
            Toast.makeText(loginActivity.this,"Please enter Username or Password",Toast.LENGTH_LONG).show();

        }

    }


    public void CheckEditTextStatus(){

        EmailHolder=Email.getText().toString();
        PasswordHolder= Password.getText().toString();

        if (TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){

            EditTextEmptyHolder=false;
        }
        else {
            EditTextEmptyHolder=true;
        }

    }

    public void CheckFinalResult(){
        if (TempPassword.equalsIgnoreCase(PasswordHolder)){
            Toast.makeText(loginActivity.this,"Login successfully",Toast.LENGTH_LONG).show();

            Intent intent=new Intent(loginActivity.this, QR_creater.class);

            intent.putExtra(UserEmail,EmailHolder);
            startActivity(intent);
        }
        else {
            Toast.makeText(loginActivity.this,"Username or Password is wrong",Toast.LENGTH_LONG).show();
        }
        TempPassword="NOT_FOUND";
    }


}