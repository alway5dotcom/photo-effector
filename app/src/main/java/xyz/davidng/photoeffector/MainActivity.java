package xyz.davidng.photoeffector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends Activity implements View.OnClickListener {
    public static final int CAMERA_REQUEST = 123;
    public static final int GALLERY_REQUEST = 234;
    private Button btnOpenCamera;
    private Button btnOpenGallery;
    private Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOpenCamera = (Button) findViewById(R.id.btnPickFromCamera);
        btnOpenGallery = (Button) findViewById(R.id.btnPickFromGallery);
        btnTest = (Button)findViewById(R.id.btnTest);
        btnOpenCamera.setOnClickListener(this);
        btnOpenGallery.setOnClickListener(this);
        btnTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPickFromCamera:
                //openCamera();
                camera();
                break;
            case R.id.btnPickFromGallery:
                //openGallery();
                gallery();
                break;
            case R.id.btnTest:
                Intent i = new Intent(MainActivity.this, IMGprocessing.class);
                startActivity(i);
                finish();
                break;
        }
    }

    private void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 0);
        i.putExtra("aspectY", 0);
        i.putExtra("outputX", 200);
        i.putExtra("outputY", 150);
        startActivityForResult(i, CAMERA_REQUEST);
    }

    private void camera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void openGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 0);
        i.putExtra("aspectY", 0);
        i.putExtra("outputX", 200);
        i.putExtra("outputY", 150);
        i.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(i, "Complete action using"), GALLERY_REQUEST);
    }

    private void gallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == CAMERA_REQUEST) {
//            Bundle bundle = data.getExtras();
//            if (bundle != null) {
//                Bitmap bmp = bundle.getParcelable("data");
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//
//                Intent i = new Intent(MainActivity.this, IMGprocessing.class);
//                i.putExtra("image", byteArray);
//                startActivity(i);
//                finish();
//            }
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Intent i = new Intent(MainActivity.this, IMGprocessing.class);
            i.putExtra("image", byteArray);
            startActivity(i);
            finish();
        }
        if (requestCode == GALLERY_REQUEST) {
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Bitmap bmp = BitmapFactory.decodeStream(imageStream);
            //=========================================================================
            Bitmap bmp = null;
            try {
                bmp = decodeUri(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //=========================================================================
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Intent i = new Intent(MainActivity.this, IMGprocessing.class);
            i.putExtra("image", byteArray);
            startActivity(i);
            finish();
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 140;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }
}
