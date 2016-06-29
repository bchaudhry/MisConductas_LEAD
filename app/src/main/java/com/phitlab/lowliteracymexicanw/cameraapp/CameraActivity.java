package com.phitlab.lowliteracymexicanw.cameraapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.phitlab.lowliteracymexicanw.BaseActivity;
import com.phitlab.lowliteracymexicanw.MainActivity;
import com.phitlab.lowliteracymexicanw.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CameraActivity extends BaseActivity implements PictureCallback, OnClickListener{

	private Camera mCamera;
	private CameraPreview mCameraPreview;
	private ImageView captureImage;
	private Dialog dialog;
	private AlertDialog alertDialog;
	private int behaviorID;
	private String activity;
	private String timeStamp;
	private int camera = 0;
	protected int no;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		Log.d("Camera", "OnCreate Camera");
		
		behaviorID = this.getIntent().getIntExtra("BEHAVIOR", -1);
		activity = this.getIntent().getStringExtra("ACTIVITY");

		String title = this.getIntent().getStringExtra("TITLE");
		TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
		titleTextView.setText(title);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.camerascreen);
		layout.setBackgroundColor(Color.GRAY);
		
		RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.bottomLayout);
		rlayout.setBackgroundColor(Color.GRAY);
		
		captureImage = (ImageView) findViewById(R.id.capture_image);
		captureImage.setOnClickListener(this);
		
		if (checkCameraHardware(this.getBaseContext())) {
			mCamera = getCameraInstance();
			mCameraPreview = new CameraPreview(this, mCamera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mCameraPreview);
			Log.d("Camera", "OnCreate Camera inside if");
		}
		
		soundfile = this.getIntent().getStringExtra("SOUND");
		//playSound(soundfile);	
		ImageView titleSound = (ImageView) findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound(soundfile);	
				registry.getLog().logClick(title_sound++, "title_sound");
			}
			
		});	
		registry.getLog().logNewClick(5);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mCamera.release();
//    	if(!getNewActivity())
//    		logoutWarning();
	}
	
	
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			return true;
		}
		else {
			return false;
		}
	}
	

	private Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		}
		catch(Exception e){
			  Log.e("CameraActivity", "failed to open Camera");
		      e.printStackTrace();
		}
		return c;
	}

	private File getOutputMediaFile() {
		if (Environment.getExternalStorageState() != null) {
			//File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidCameraTestsFolder");
			File imageStorageDir = new File(Environment.getExternalStorageDirectory().getPath(), "LEADPhotos");
	
			if (! imageStorageDir.exists()){
				if(! imageStorageDir.mkdirs()){
					Log.d("Camera Activity", "failed to create directory");
					return null;
				}
			}
			
			timeStamp = new SimpleDateFormat("MMdd_HHmm").format(new Date());
			File mediaFile = new File(imageStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
			
			return mediaFile;
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.capture_image) {
			try {
				mCamera.takePicture(null, null, this);
				registry.getLog().logClick(camera ++, "camera_icon");
			}
			catch (Exception ex) {
				Log.d("camera activity", ""+ex);
			}
        }
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		File pictureFile = getOutputMediaFile();
		Log.d("Camera Activity", "I have taken the picture");
        if (pictureFile == null){
            Log.d("Camera Activity", "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            Log.d("Camera Activity", "I should close the file now");
            fos.write(data);
            fos.close();
			
            finalizeDialog(data);
            dialog.show();
            
            mCameraPreview.surfaceChanged(null,0, 0, 0);
        } catch (FileNotFoundException e) {
            Log.d("Camera Activity", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("Camera Activity", "Error accessing file: " + e.getMessage());
        }
    }

	private void finalizeDialog(byte[] data) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_showimage);
        dialog.setCancelable(true);
        
        String strTitle = getResources().getString(R.string.camera_message);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(strTitle);
        StyleSpan span = new StyleSpan(Typeface.ITALIC);
        ScaleXSpan span1 = new ScaleXSpan((float) 0.45);
        ssBuilder.setSpan(span, 0, strTitle.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssBuilder.setSpan(span1, 0, strTitle.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
       // dialog.setTitle(ssBuilser);
       // playSound("camera_message.ogg");
        

		ImageView titleSound = (ImageView) dialog.findViewById(R.id.sound_Icon);
		titleSound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playSound("camera_message.ogg");	
				registry.getLog().logClick(title_sound++, "title_sound");
			}
			
		});
        
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0 , data.length, options); 

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 350, 250);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0 , data.length, options);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 450, 400, false);
         
        // create a matrix object
        Matrix matrix = new Matrix();
        matrix.postRotate(90); // anti-clockwise by 90 degrees
         
        // create a new bitmap from the original using the matrix to transform the result
        final Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
        
        // final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0 , data.length); 
        ImageView img = (ImageView) dialog.findViewById(R.id.image_taken);
        img.setImageBitmap(rotatedBitmap); 
        
        ImageView imgOK = (ImageView) dialog.findViewById(R.id.ok);
        imgOK.setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View v) {
        		Log.d("hello", "hello camera world");
        		//displayPopup();
        		dialog.dismiss();
        		bitmap.recycle();
        		saveImageName();
        		mCamera.release();
        		showToast();
        		registry.getLog().logFinalClick("yes_icon", 1);
				registry.getLog().logChoice("photo_name", "IMG_"+ timeStamp + ".jpg");
    			Intent intent = new Intent(CameraActivity.this, MainActivity.class);
    			intent.putExtra("TITLE", getResources().getString(R.string.mainmenu_title));
    			intent.putExtra("SOUND", "thanks.ogg");
    			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			resetReminders();
    			startActivity(intent);
            }

        });
        
        ImageView imgCancel = (ImageView) dialog.findViewById(R.id.remove);
        imgCancel.setOnClickListener(new OnClickListener() {
        	
        	@Override
			public void onClick(View v) {
        		bitmap.recycle();
        		registry.getLog().logClick(no++, "no_icon");
                dialog.dismiss();
            }
        });   
	}
	
	protected void saveImageName() {
		File imageInfoStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Data");
		File imageFile = new File(imageInfoStorageDir.getPath() + File.separator + "imageLog.log");
		File imageStorageDir = new File(Environment.getExternalStorageDirectory().getPath(), "LEADPhotos");
		try {
			BufferedWriter bufWriter = new BufferedWriter(new FileWriter(imageFile));
			bufWriter.write(imageStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
			bufWriter.flush();
			bufWriter.close();
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
	}


	protected void displayPopup() {
    	AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Pregunta");
        helpBuilder.setMessage("¿Te gustaría hacer otra fotografía?");
        helpBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
        	@Override
			public void onClick(DialogInterface dialog, int which) {      		  
        		alertDialog.dismiss();
        	}
    	 });
        
        helpBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
        	@Override
			public void onClick(DialogInterface dialog, int which) {
        		alertDialog.dismiss();
        		mCamera.release();
      	  	}
      	 });
		
        alertDialog = helpBuilder.create();
        alertDialog.show();
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (dialog != null) {
				dialog.show();
			}
		}
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			if (dialog != null) {
				dialog.show();
			}
		}
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee
        // a final image with both dimensions larger than or equal to the
        // requested height and width.
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
	}

}
