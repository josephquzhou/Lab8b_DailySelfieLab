package com.example.joe_2.lab8b_dailyselfielab;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentImageWithPath;
    private ListView mListView;
    private ImageAdapter mAdapter;
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    // for test only
    private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView)findViewById(R.id.listview);
        ArrayList<String> listFiles = getListFiles(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        mAdapter = new ImageAdapter(this, listFiles);
        mListView.setAdapter(mAdapter);

        // set alarm time to 12pm
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if(calendar.getTime().compareTo(new Date()) < 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0,intent1, 0);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(
                MainActivity.this.ALARM_SERVICE);
        // test code
//        am.setRepeating(AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis() + INITIAL_ALARM_DELAY,
//                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        // final code
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);


    }

    private ArrayList<String> getListFiles(File dir) {
        File[] files = dir.listFiles();
        Arrays.sort(files, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                return -Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            } });
        ArrayList<String> fileNames = new ArrayList<>();
        for (File f : files) {
            fileNames.add(f.getAbsolutePath());
        }
        return fileNames;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.start_camera) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = null;
            try {
                f = setUpPhotoFile();
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.joe_2.lab8b_dailyselfielab.fileprovider",
                        f);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            } catch (IOException e) {
                e.printStackTrace();
                f = null;
                if (mCurrentImageWithPath != null) {
                    new File(mCurrentImageWithPath).delete();
                    mCurrentImageWithPath = null;
                }
            }
            if (f != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (mCurrentImageWithPath != null) {
                    mAdapter.insert(mCurrentImageWithPath, 0);
                    mAdapter.notifyDataSetChanged();
                    mCurrentImageWithPath = null;
                }
            }
            else {
                if (mCurrentImageWithPath != null) {
                    File f = new File(mCurrentImageWithPath);
                    f.delete();
                    mCurrentImageWithPath = null;
                }
            }
        }
    }

    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        return f;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_").format(new Date());
        String imageFileName = timeStamp;

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                JPEG_FILE_SUFFIX,         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentImageWithPath = image.getAbsolutePath();
        return image;
    }


}
