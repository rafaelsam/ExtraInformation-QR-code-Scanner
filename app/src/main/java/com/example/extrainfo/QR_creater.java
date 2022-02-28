package com.example.extrainfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import org.w3c.dom.Text;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class QR_creater extends AppCompatActivity {

    // Variables for ....

    private Button qrCodeBtn,save,share,print;
    private EditText dataEdt;
    private ImageView qrCodeIV;

    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    String savePath= Environment.getExternalStorageDirectory().getPath()+"/QRCode/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_creater);

        // Initializing all variables.

        qrCodeBtn = (Button) findViewById(R.id.idBtnGenerateQR);
        save = (Button) findViewById(R.id.save);
        share = (Button) findViewById(R.id.share);
        print = (Button) findViewById(R.id.print);
        dataEdt = (EditText) findViewById(R.id.idEdit);
        qrCodeIV = (ImageView) findViewById(R.id.idIVQcode);

        // initializing button onclick listener

        qrCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(dataEdt.getText().toString())) {

                    // If EditText is empty then show Toast message
                    Toast.makeText(QR_creater.this, "Enter some text to generate QR code", Toast.LENGTH_LONG).show();
                }
                else{

                    // This line allow App to get window Manager services
                    WindowManager manager=(WindowManager) getSystemService(WINDOW_SERVICE);

                    // This function initializing variable for default variables
                    Display display = manager.getDefaultDisplay();

                    // Creating point which will display qr code
                    Point point= new Point();
                    display.getSize(point);

                    // Getting width and height of the point
                    int width=point.x;
                    int height=point.y;

                    // set dimension from width and height
                    int dimen = width < height ? width : height;
                    dimen = dimen*3/4;

                    // set this dimension inside the qr code encode to generate qr code
                    qrgEncoder = new QRGEncoder(dataEdt.getText().toString(),null, QRGContents.Type.TEXT, dimen);

                    try {
                        // getting qr code in the form of bitmap
                        bitmap = qrgEncoder.encodeAsBitmap();

                        // set bitmap inside image view
                        qrCodeIV.setImageBitmap(bitmap);
                    }
                    catch (WriterException e){

                        // This method is called when exceptions occur
                        Log.e("Tag",e.toString());
                    }
                }
            }

        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean save;
                String result;

                try {
                    save= QRGSaver.save(savePath,dataEdt.getText().toString().trim(),bitmap,QRGContents.ImageType.IMAGE_JPEG);
                    result = save? "Image Saved":"Image not saved";
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body ="Your body here";
                String hub="Your subject";

                myIntent.putExtra(Intent.EXTRA_SUBJECT,hub);
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(myIntent,"Share Using"));
            }
        });


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintHelper photoPrinter = new PrintHelper(QR_creater.this);
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

                Bitmap bitmap = ((BitmapDrawable) qrCodeIV.getDrawable()).getBitmap();
                photoPrinter.printBitmap("test print",bitmap);
            }
        });

    }
}