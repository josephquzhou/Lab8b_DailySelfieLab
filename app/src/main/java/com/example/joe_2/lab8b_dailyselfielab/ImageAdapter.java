package com.example.joe_2.lab8b_dailyselfielab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import static android.graphics.BitmapFactory.decodeFile;

public class ImageAdapter extends ArrayAdapter<String> {
	private static final int WIDTH = 5000;
	private static final int HEIGHT = 5000;
	private Context mContext;
	private ArrayList<String> mFiles;

	// Store the list of image names
	public ImageAdapter(Context c, ArrayList<String> fileNames) {
		super(c, 0, fileNames);
		mContext = c;
		this.mFiles = fileNames;
	}

	// Return the number of items in the Adapter
	@Override
	public int getCount() {
		return mFiles.size();
	}

	// Return the data item at position
	@Override
	public String getItem(int position) {
		return mFiles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// Return an ImageView for each item referenced by the Adapter
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item, null, true);
        }
		ImageView imageView = (ImageView)view.findViewById(R.id.image);
		TextView textView = (TextView)view.findViewById(R.id.text);
		ImageButton btn = (ImageButton)view.findViewById(R.id.delete) ;

		final String fullName = getItem(position);
		final String shortName = fullName.substring(fullName.lastIndexOf('/') + 1, fullName.lastIndexOf('_'));


		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ImageViewActivity.class);
				intent.putExtra("fileName", fullName);
				mContext.startActivity(intent);
			}
		});

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						mContext);
				alert.setTitle("Alert!!");
				alert.setMessage("Are you sure to delete photo: " + shortName);
				alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						File f = new File(fullName);
						if (f.delete()) {
							mFiles.remove(position);
							notifyDataSetChanged();
						}
					}
				});
				alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

				alert.show();
			}
		});

        int i = imageView.getWidth();
        int j = imageView.getHeight();
        int ii = imageView.getMeasuredWidth();
        int jj = imageView.getMeasuredHeight();

//        Bitmap b = Bitmap.createScaledBitmap(decodeFile(fullName), WIDTH,
//                HEIGHT, true);
//        imageView.setImageBitmap(b);
//        b.recycle();

		imageView.setImageBitmap(decodeFile(fullName));
		textView.setText(shortName);
		return view;
	}
}
