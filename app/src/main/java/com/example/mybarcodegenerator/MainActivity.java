package com.example.mybarcodegenerator;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText text;
     TextView textResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textResult = findViewById(R.id.textResult1);
        Button barCodeGenerator = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        text = findViewById(R.id.editText);

        barCodeGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //2 setBarCodeGenerator();
                //setQRCodeGenerator();
                filePath();
            }
        });
    }

    private void setQRCodeGenerator() {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(text.getText().toString(), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(bmp);
          //  ((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void filePath()
    {
        File folder = new File("/sdcard/DCIM/Camera/");
//        File[] listOfFiles = folder.listFiles();

        File dcimDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());
        File ais = Environment.getExternalStorageDirectory();
       // String mpath = dcimDir.getAbsolutePath() ;//+ "/Camera/";
       /// String mpath = dcimDir.getAbsolutePath() + "/Camera/IMG_20220922_183732_058.jpg";
        String mpath = dcimDir.getAbsolutePath() + "/Camera/Krishna11.jpg";



       // File pdir = new File(mpath);
        Log.d("MainActivity","File in tab:");
        runTextRecognition(mpath);
//        Log.d("MainActivity","File in tab:"+pdir.exists() );
//
//        File[] listOfFiles = pdir.listFiles();
//
//        Log.d("MainActivity","File in tab:"+listOfFiles.length );
//        for (int i = 0; i < listOfFiles.length; i++) {
//            if (listOfFiles[i].isFile()) {
//                Log.d("MainActivity","File found" + listOfFiles[i].getName());
//                System.out.println("File " + listOfFiles[i].getName());
//            } else if (listOfFiles[i].isDirectory()) {
//                Log.d("MainActivity","Directory "  + listOfFiles[i].getName());
//               // System.out.println("Directory " + listOfFiles[i].getName());
//            }
//        }
    }

    private void runTextRecognition(String filepath) {

        InputImage image = null;
        try {
           //File imgFile = new File("/sdcard/Images/IMG_20220922_183732_058.jpg");
            File imgFile = new File(filepath);

            Log.d("MainActivity","File found" + imgFile.exists());
            if(imgFile.exists())
            {
                Uri imgFileUri=Uri.fromFile(imgFile);

                image = InputImage.fromFilePath(MainActivity.this, imgFileUri);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
       // InputImage image = InputImage.fromBitmap(mSelectedImage, 0);
        TextRecognizer recognizer = TextRecognition.getClient();
       // mTextButton.setEnabled(false);
        recognizer.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text texts) {
                               // mTextButton.setEnabled(true);
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                //mTextButton.setEnabled(true);
                                e.printStackTrace();
                            }
                        });
    }
    private void processTextRecognitionResult(Text texts) {
        List<Text.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
           // showToast("No text found");
            return;
        }
       // mGraphicOverlay.clear();
        String append="";
        for (int i = 0; i < blocks.size(); i++) {

            List<Text.Line> lines = blocks.get(i).getLines();
            append=append+"\n"+blocks.get(i).getText().toString();
            if(i==3){
                textResult.setText(append);
                Log.v("MainActivity","Block = 3 value : "+append);;
            }

            for (int j = 0; j < lines.size(); j++) {
                List<Text.Element> elements = lines.get(j).getElements();
//                append=append+"\n"+lines.get(j).getText().toString();
                for (int k = 0; k < elements.size(); k++) {
//                    append=append+"\n"+elements.get(k).getText().toString();

//                    textResult.setText(append);

//                    Graphic textGraphic = new TextGraphic(mGraphicOverlay, elements.get(k));
//                    mGraphicOverlay.add(textGraphic);

                }
            }
        }
        Log.v("MainActivity","total string: "+append);
    }



    private void setBarCodeGenerator(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text.getText().toString(), BarcodeFormat.CODE_128, imageView.getWidth(), imageView.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.RGB_565);
            for (int i = 0; i<imageView.getWidth(); i++){
                for (int j = 0; j<imageView.getHeight(); j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}