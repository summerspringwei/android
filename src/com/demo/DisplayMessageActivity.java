package com.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMessageActivity extends Activity {

	private int FILE_SELECT_CODE=0;
	private TextView rev_text=null;
	private ImageView selectedImg=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		Intent intent=getIntent();
		String message=intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		rev_text=(TextView)findViewById(R.id.rev_text);
		rev_text.setText(message);
		Button fileChoose_btn=(Button)findViewById(R.id.fileChoose_btn);
		fileChoose_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFileChooser();
			}
		});
		selectedImg=(ImageView)findViewById(R.id.selectedImg_iv);
	}

	/** 调用文件选择软件来选择文件 **/
	private void showFileChooser() {
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
	    intent.setType("*/*"); 
	    intent.addCategory(Intent.CATEGORY_OPENABLE);
	    try {
	        startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
	        onActivityResult(FILE_SELECT_CODE, FILE_SELECT_CODE, intent);
	        
	    } catch (android.content.ActivityNotFoundException ex) {
	        Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
	    }
	}
	
	/*接受文件*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
	    switch (requestCode) {
	        case 0:      
	        if (resultCode == RESULT_OK) {
	            // Get the Uri of the selected file 
	            Uri uri = data.getData();
	            selectedImg.setImageURI(uri);
	            String path = FileUtils.getPath(this, uri);
	            rev_text.setText(path);
	        }
	        break;
	    }
	super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public static class FileUtils {
	    public static String getPath(Context context, Uri uri) {

	        if ("content".equalsIgnoreCase(uri.getScheme())) {
	            String[] projection = { "_data" };
	            Cursor cursor = null;

	            try {
	                cursor = context.getContentResolver().query(uri, projection,null, null, null);
	                int column_index = cursor.getColumnIndexOrThrow("_data");
	                if (cursor.moveToFirst()) {
	                    return cursor.getString(column_index);
	                }
	            } catch (Exception e) {
	                // Eat it
	            }
	        }

	        else if ("file".equalsIgnoreCase(uri.getScheme())) {
	            return uri.getPath();
	        }

	        return null;
	    }
	}
}
