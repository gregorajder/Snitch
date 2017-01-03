package com.gregorajdergmail.snitch.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gregorajdergmail.snitch.App;
import com.gregorajdergmail.snitch.R;
import com.gregorajdergmail.snitch.model.FileHelper;
import com.nononsenseapps.filepicker.FilePickerActivity;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_REQUEST_CODE = 1;
    @Inject
    protected FileHelper fileHelper;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), FilePickerActivity.class);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                startActivityForResult(i, ACTIVITY_REQUEST_CODE);
            }
        });

        textView = (TextView) findViewById(R.id.my_text_view);
        Button stopButton = (Button) findViewById(R.id.stop_watching_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileHelper.stopService(getBaseContext());
                textView.setText(null);
            }
        });


        String path = fileHelper.getFilePath();
        if (path != null) {
            textView.setText(path);
            fileHelper.startService(this, path);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            fileHelper.startService(this, data.getData().getPath());
            textView.setText(data.getData().getPath());
        }
    }

}

