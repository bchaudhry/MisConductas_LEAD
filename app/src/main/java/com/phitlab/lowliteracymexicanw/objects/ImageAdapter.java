package com.phitlab.lowliteracymexicanw.objects;

import java.util.ArrayList;
import com.phitlab.lowliteracymexicanw.R;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	public ImageLoader imageLoader;
	//private MediaPlayer splashSound;
	protected Registry registry = Registry.getInstance();
	private int[] iconClicks;
	private ArrayList<GridObject> gridObjAL;
	private int[] minusClicks;
	private int[] plusClicks;
	private MediaPlayer mp;
	
	public ImageAdapter(Context c, ArrayList<GridObject> gridObjAL){
		mContext = c;
		this.gridObjAL = new ArrayList<GridObject>(gridObjAL);
		//splashSound = MediaPlayer.create(c,R.raw.startup);
		iconClicks = new int[getCount()];
		plusClicks = new int[getCount()];
		minusClicks = new int[getCount()];

		for (int i=0;i<getCount();i++) {
			iconClicks[i] = 0;
			minusClicks[i] =0;
			plusClicks[i] = 0;
		}	
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
		return position;
	}

	// create a new ImageView for each item referenced by the Adapter
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		RelativeLayout rl = null;
		final int pos = position;
		View view = convertView;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			view = View.inflate(mContext,  R.layout.gridview_withplusminus, null);
			rl = (RelativeLayout) view.findViewById(R.id.relaGrid);
		}
		else {
			rl = (RelativeLayout)convertView;
		}

		rl.setBackgroundColor(Color.WHITE);
		
		TextView text = (TextView) rl.findViewById(R.id.chooseText);      
		text.setBackgroundColor(Color.WHITE);
		text.setText("" + this.gridObjAL.get(position).getrTitle());
		text.setTextColor(Color.GRAY);

		ImageView image = (ImageView) rl.findViewById(R.id.chooseImage);
		image.setImageResource(gridObjAL.get(position).getRid());
		image.setScaleType(ImageView.ScaleType.FIT_XY);
		image.setBackgroundColor(Color.TRANSPARENT);
		image.setPadding(10, 10, 10, 10);
		
		ImageView voiceImgView = (ImageView)rl.findViewById(R.id.playVoice);
		voiceImgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playSound(gridObjAL.get(pos).getMedia());
				Log.d("ImageAdapter", "audio file is "+gridObjAL.get(pos).getMedia());
				String icon = "icon" + (pos+1) + "_sound";
				registry.getLog().logFinalClick(icon, iconClicks[pos]++);
			}
		});
		
		TextView countText = (TextView) rl.findViewById(R.id.numText);      
		countText.setBackgroundColor(Color.RED);
		countText.setTextColor(Color.WHITE);
		countText.setText(Integer.toString(plusClicks[position]-minusClicks[position]));

		ImageView plusImgView = (ImageView)rl.findViewById(R.id.img_plus);
		plusImgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				plusClicks[pos]++;
				registry.getLog().logClick(plusClicks[pos], "plus_icon");
				notifyDataSetChanged();
			}
		});
		
		ImageView minusImgView = (ImageView)rl.findViewById(R.id.img_minus);
		minusImgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				if (plusClicks[pos] > 0 && minusClicks[pos] < plusClicks[pos]) {
					minusClicks[pos]++;
					registry.getLog().logClick(minusClicks[pos], "minus_icon");
					notifyDataSetChanged();
				}
			}
		});
		
		View blankView = rl.findViewById(R.id.blankView);
		blankView.setBackgroundColor(Color.WHITE);
		Log.d("ImageAdapter", "inside the cover");
		return rl;
	}

	public int getTotal() {
		int cnt = 0;
		for (int i=0;i<getCount();i++) {
			cnt += (plusClicks[i]-minusClicks[i]);
		}
		return cnt;
	}
	
	private void playSound(String str) {
		//stopPlaying();
	  	mp = new MediaPlayer();

        try {
            mp.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/Data/AppAudio/"+str);
            mp.prepare();
            mp.start();
//            new Thread(new Runnable() {
//    			public void run() {
//    				 try {
//						Thread.sleep(1000);
//						stopPlaying();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//    			}        
//            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void stopPlaying() {
		  if (mp != null) {
	            mp.stop();
	            mp.release();
	            mp = null;
	       }    
	}
	
	public int getClicks(int pos) {
		return (plusClicks[pos]-minusClicks[pos]);
	}
	
	public String getTotalString() {
		String str = "Partial=" +  (plusClicks[0]-minusClicks[0]) + "; Full=" +  (plusClicks[1]-minusClicks[1]);
		//for (int i=0;i<getCount();i++) {
		//	str += "Type " + i + "=" + (plusClicks[i]-minusClicks[i]) + "; ";
		//}
		return str;
	}
	
	public int getTotal(int i) {
		return (plusClicks[i]-minusClicks[i]);
	}
}