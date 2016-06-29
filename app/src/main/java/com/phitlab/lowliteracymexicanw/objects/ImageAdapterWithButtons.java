package com.phitlab.lowliteracymexicanw.objects;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import com.phitlab.lowliteracymexicanw.R;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageAdapterWithButtons extends BaseAdapter {

	private static final int NORMAL = 0;
	private Context mContext;
	private ArrayList<GridObject> gridObjAL;
	private int[] iconClicks;
	private int screen_type;
	private MediaPlayer mp;
	protected Registry registry;
	
	public ImageAdapterWithButtons(Context c, ArrayList<GridObject> gridObjAL){
		mContext = c;
		this.gridObjAL = new ArrayList<GridObject>(gridObjAL);
		iconClicks = new int[getCount()];
		screen_type = NORMAL;
		for (int i=0;i<getCount();i++)
			iconClicks[i] = 0;
	}
	
	public ImageAdapterWithButtons(Context c, ArrayList<GridObject> gridObjAL, int type){
		mContext = c;
		this.gridObjAL = new ArrayList<GridObject>(gridObjAL);
		iconClicks = new int[getCount()];
		screen_type = type;
		for (int i=0;i<getCount();i++)
			iconClicks[i] = 0;
		
	}

	
	@Override
	public int getCount() {
		return gridObjAL.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView image = null;
		RelativeLayout rl = null;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			View view = View.inflate(mContext, R.layout.gridview_withbuttons, null);
			rl = (RelativeLayout) view.findViewById(R.id.relaGrid);
		}
		else { 
			rl = (RelativeLayout)convertView;
		}
	 
		rl.setBackgroundColor(Color.WHITE);
		
		image = (ImageView) rl.findViewById(R.id.chooseImage);
		
		if (screen_type == NORMAL) 
			image.setImageResource(gridObjAL.get(position).getRid());
		else {
			ImageLoader imgLoader = new ImageLoader(mContext);
			imgLoader.DisplayImage(gridObjAL.get(position).getrUrl(),image);
		}
		image.setScaleType(ImageView.ScaleType.FIT_XY);
		image.setBackgroundColor(Color.TRANSPARENT);
		image.setPadding(10, 10, 10, 10);
		
		TextView text = (TextView) rl.findViewById(R.id.chooseText);   
		text.setBackgroundColor(Color.WHITE);
		text.setText(gridObjAL.get(position).getrTitle());
		text.setTextColor(Color.GRAY);
		
		rl.setId(gridObjAL.get(position).getRid());
		
		final int pos = position;
		ImageView voiceImgView = (ImageView)rl.findViewById(R.id.playVoice);
		voiceImgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playSound(gridObjAL.get(pos).getMedia());
				Log.d("ImageAdapter", "audio file is "+gridObjAL.get(pos).getMedia());
				String icon = "icon" + (pos+1) + "_sound";
				//registry.getLog().logFinalClick(icon, iconClicks[pos]++);
			}
		});

		View blankView = rl.findViewById(R.id.blankView);
		blankView.setBackgroundColor(Color.WHITE);
		
		return rl;
	}
	
	
	private void playSound(String str) {

		if(mp != null){
		    mp.release();
		}
		mp = new MediaPlayer();
		 
		//in this case my sound file is in the application directory...       
		String audioFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Data/AppAudio/"+str;
		       
		try {
		    File file = new File(audioFilePath);
		    FileInputStream fis = new FileInputStream(file);
		    mp.setDataSource(fis.getFD());
		    mp.prepare();
		    mp.start();    
		} catch(FileNotFoundException e){
			Log.d("ImageAdapterWithButton", "the exception 1" + e.toString());
		} catch (IllegalArgumentException e) {
			Log.d("ImageAdapterWithButton", "the exception 2" + e.toString());
		} catch (IllegalStateException e) {
			Log.d("ImageAdapterWithButton", "the exception 3" + e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("ImageAdapterWithButton", "the exception 4" + e.toString());
		}
    }

}
	
		
