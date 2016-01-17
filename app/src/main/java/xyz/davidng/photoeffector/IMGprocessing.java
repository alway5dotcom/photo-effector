package xyz.davidng.photoeffector;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class IMGprocessing extends Activity implements View.OnClickListener, View.OnTouchListener {
    byte[] byteArrayImage;
    public Bitmap bmp;
    Bitmap bmpBackup;
    private ImageView img;

    private Button btnShadow;
    private Button btnNone;
    private Button btnInvert;
    private Button btnSaveSD;
    private Button btnGray;
    private Button btnRed, btnGreen, btnBlue;
    private Button btnContrast;
    private Button btnRound;
    private Button btnBrightness;
    private Button btnFlip;
    private boolean contrastIsOn = false;
    private int contrast = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgprocessing);
        img = (ImageView) findViewById(R.id.imageviewMain);
        byteArrayImage = getIntent().getByteArrayExtra("image");
        //bmp = BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.length);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.saigon);
        bmpBackup = bmp;
        img.setImageBitmap(bmp);
        viewInitialize();

    }

    private void viewInitialize() {
        btnShadow = (Button) findViewById(R.id.btnShadow);
        btnNone = (Button) findViewById(R.id.btnNone);
        btnInvert = (Button) findViewById(R.id.btnInvert);
        btnSaveSD = (Button) findViewById(R.id.btnSaveSD);
        btnGray = (Button) findViewById(R.id.btnGray);
        btnRed = (Button) findViewById(R.id.btnRed);
        btnGreen = (Button) findViewById(R.id.btnGreen);
        btnBlue = (Button) findViewById(R.id.btnBlue);
        btnContrast = (Button) findViewById(R.id.btnContrast);
        btnRound = (Button)findViewById(R.id.btnRound);
        btnBrightness = (Button)findViewById(R.id.btnBrightness);
        btnFlip = (Button)findViewById(R.id.btnFlip);

        btnShadow.setOnClickListener(this);
        btnNone.setOnClickListener(this);
        btnInvert.setOnClickListener(this);
        btnSaveSD.setOnClickListener(this);
        btnGray.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnContrast.setOnClickListener(this);
        btnRound.setOnClickListener(this);
        btnBrightness.setOnClickListener(this);
        btnFlip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShadow:
                Bitmap temp = Utils.doHightlightShadow(bmp);
                img.setImageBitmap(temp);
                bmp = temp;
                break;
            case R.id.btnNone:
                bmp = bmpBackup;
                img.setImageBitmap(bmp);
                break;
            case R.id.btnInvert:
                Bitmap temp3 = Utils.doInvert(bmp);
                img.setImageBitmap(temp3);
                bmp = temp3;
                break;
            case R.id.btnSaveSD:
                try {
                    Utils.saveImage(bmp);
                    Toast.makeText(getApplicationContext(), "saved to folder /abcxyz", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Failed !", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnGray:
                Bitmap temp4 = Utils.doGray(bmp);
                img.setImageBitmap(temp4);
                bmp = temp4;
                break;
            case R.id.btnRed:
                Bitmap temp5 = Utils.doColorFilter(bmp, 1.0, 0.0, 0.0);
                img.setImageBitmap(temp5);
                bmp = temp5;
                break;
            case R.id.btnGreen:
                Bitmap temp6 = Utils.doColorFilter(bmp, 0.0, 1.0, 0.0);
                img.setImageBitmap(temp6);
                bmp = temp6;
                break;
            case R.id.btnBlue:
                Bitmap temp7 = Utils.doColorFilter(bmp, 0.0, 0.0, 1.0);
                img.setImageBitmap(temp7);
                bmp = temp7;
                break;
//            case R.id.btnContrast:
//                if (contrastIsOn == false) {
//                    contrastIsOn = true;
//                    Toast.makeText(getApplicationContext(), "Contrast ON", Toast.LENGTH_SHORT).show();
//                } else {
//                    contrastIsOn = false;
//                    Toast.makeText(getApplicationContext(), "Contrast OFF", Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.btnRound:
                showRoundValueInput();
                break;
            case R.id.btnBrightness:
                showBrightness();
                break;
            case R.id.btnFlip:
                showFlip();
                break;

        }
    }

    public void showBrightness(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setTitle("Enter value (0-255)");
        alert.setView(edittext);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int value = Integer.parseInt(edittext.getText().toString());
                if(value < 0){
                    value = 0;
                }
                Bitmap temp9 = Utils.doBrightness(bmp, value);
                img.setImageBitmap(temp9);
                bmp = temp9;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });

        alert.show();
    }

    public void showFlip(){
        final CharSequence[] items = { "Vertical","Horizontal" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("choose:");
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Bitmap temp10 = null;
                        if(item == 0){
                            temp10 = Utils.flip(bmp, 0);
                        } else if(item == 1){
                            temp10 = Utils.flip(bmp, 1);
                        }
                        img.setImageBitmap(temp10);
                        bmp = temp10;
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showRoundValueInput(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setTitle("Enter value:");
        alert.setView(edittext);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                float value = Float.parseFloat(edittext.getText().toString());
                if(value < 0){
                    value = 0;
                }
                Bitmap temp8 = Utils.roundCorner(bmp, value);
                img.setImageBitmap(temp8);
                bmp = temp8;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });
        alert.show();

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        int preX = 0, afterX = 0;
//        while (contrastIsOn == true) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    preX = (int) event.getX();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    break;
//                case MotionEvent.ACTION_UP:
//                    afterX = (int) event.getX();
//
//                    if ((preX - afterX) > 0) {
//                        contrast -= 80;
//                        Bitmap temp6 = Utils.createContrast(bmp, contrast);
//                        img.setImageBitmap(temp6);
//                        bmp = temp6;
//                    } else if ((preX - afterX) < 0) {
//                        contrast += 100;
//                        Bitmap temp6 = Utils.createContrast(bmp, contrast);
//                        img.setImageBitmap(temp6);
//                        bmp = temp6;
//                    }
//                    break;
//            }
//        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Back to main?");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(IMGprocessing.this, MainActivity.class));
                finish();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        dialog.create().show();
    }
}
