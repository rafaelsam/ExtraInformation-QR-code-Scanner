package com.example.extrainfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {


    Button Register;
    EditText Email, Password,Name;
    String EmailHolder, PasswordHolder, NameHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    SQLiteHelper sqLiteHelper;
    String SQLiteDatabaseQueryHolder;
    Cursor cursor;
    String F_Result="NOT_FOUND";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Register= (Button)findViewById(R.id.buttonRegister1);
        Email=(EditText)findViewById(R.id.editEmail1);
        Password=(EditText)findViewById(R.id.editPassword1);
        Name=(EditText)findViewById(R.id.editName1);

        sqLiteHelper = new SQLiteHelper(this);

        Register.setOnClickListener(v -> {

            SQLiteDatabaseBuild();

            SQLiteTableBuild();

            CheckEditTextStatus();

            CheckEmailAlreadyExistOrNot();

            EmptyEditTextAfterDataInsert();
        });

    }


    // Method to create Database
    public void SQLiteDatabaseBuild(){
        sqLiteDatabaseObj=openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE,null);
    }

    // Method to create Table
    public void SQLiteTableBuild(){
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" +SQLiteHelper.Table_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + sqLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_Email + " VARCHAR, "+ SQLiteHelper.Table_Column_3_password + " VARCHAR );"  );
    }

    // This Method insert data into database
    public void InsertDataIntoSQLiteDatabase(){

        // This method will be called if all field are filled
        if(EditTextEmptyHolder==true){

            // query to insert data into table
            SQLiteDatabaseQueryHolder="INSERT INTO "+SQLiteHelper.TABLE_NAME+" (name,email,password) VALUES ('"+NameHolder+"', '"+EmailHolder+"', '"+PasswordHolder+"');";

            // executing query
            sqLiteDatabaseObj.execSQL(SQLiteDatabaseQueryHolder);

            // closing SQLite database object
            sqLiteDatabaseObj.close();

            Intent intent = new Intent(RegisterActivity.this,loginActivity.class);
            startActivity(intent);

            Toast.makeText(RegisterActivity.this,"User registered successfully",Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(RegisterActivity.this,"Please fill all field",Toast.LENGTH_LONG).show();
        }

    }

    // This method empty data after data insertion
    public void EmptyEditTextAfterDataInsert(){
        Name.getText().clear();
        Email.getText().clear();
        Password.getText().clear();
    }

    // This method check if EditText is empty or not
    public void CheckEditTextStatus(){

        // Getting all values from EditText and storing in variable
        NameHolder=Name.getText().toString();
        EmailHolder=Email.getText().toString();
        PasswordHolder=Password.getText().toString();

        if (TextUtils.isEmpty(NameHolder)||TextUtils.isEmpty(EmailHolder)||TextUtils.isEmpty(PasswordHolder)){
            EditTextEmptyHolder=false;
        }
        else {
            EditTextEmptyHolder=true;
        }
    }


    // Checking if email already exist or not
    public void CheckEmailAlreadyExistOrNot(){

        // Opening SQLite database write permission
        sqLiteDatabaseObj= sqLiteHelper.getWritableDatabase();

        // Adding search email to cursor
        cursor=sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME,null,""+SQLiteHelper.Table_Column_2_Email+"=?",new String[]{EmailHolder},null,null,null);

        while (cursor.moveToNext()){
            if (cursor.isFirst()){
                cursor.moveToFirst();

                // if email is found then variable value is set Email not found
                F_Result="Email Found";

                cursor.close();
            }
        }
        CheckFinalResult();
    }


    public void CheckFinalResult(){

        // checking whether email exist or not
        if (F_Result.equalsIgnoreCase("Email Found")){

            // If email exist then it will show toast message
            Toast.makeText(RegisterActivity.this,"Email Already Exists",Toast.LENGTH_LONG).show();
        }
        else {

            // if email does not exist then it will insert data into table
            InsertDataIntoSQLiteDatabase();
        }
        F_Result="Not_Found";
    }

}