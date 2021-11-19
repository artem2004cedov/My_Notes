package com.example.my;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.my.MyAdapter.ListItem;
import com.example.my.db.MyConstants;
import com.example.my.db.MyDbManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditActivity extends AppCompatActivity {
    private ImageView imageNow;
    private ConstraintLayout imageGetContainer;
    ImageButton edEditButton, edEditDelete;
    private MyDbManager myDbManager;
    private FloatingActionButton floatingActionButton;
    private EditText edTitle, edDisc;
    private final int POCK_IMAGE_CODE = 123;
    private String tempUri = "empty";
    private boolean isEditState = true;
    private ListItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getMyIntent();
    }

    // запускает базу данных
    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == POCK_IMAGE_CODE && data != null) {
            tempUri = data.getData().toString();
            imageNow.setImageURI(data.getData());

            getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public void init() {
        edDisc = findViewById(R.id.edDisc);
        floatingActionButton = findViewById(R.id.fdAbImage);
        edTitle = findViewById(R.id.edTitle);
        imageNow = findViewById(R.id.imageViewNow);
        imageGetContainer = findViewById(R.id.real);
        edEditButton = findViewById(R.id.emEditButton);
        edEditDelete = findViewById(R.id.imEditDelete);
        myDbManager = new MyDbManager(this);

    }

    private void getMyIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            item = (ListItem) intent.getSerializableExtra(MyConstants.LIST_ITEM_INTENT);
            isEditState = intent.getBooleanExtra(MyConstants.EDIT_STATE, true);

            if (!isEditState) {
                edTitle.setText(item.getTitle());
                edDisc.setText(item.getDick());

                if (!item.getUri().equals("empty")) {
                    tempUri = item.getUri();
                    imageGetContainer.setVisibility(View.VISIBLE);
                    imageNow.setImageURI(Uri.parse(item.getUri()));

//                    edEditButton.setVisibility(View.GONE);
//                    edEditDelete.setVisibility(View.GONE);
                }
            }
        }
    }

    public void onClickSave(View view) {
        String title = edTitle.getText().toString();
        String disc = edDisc.getText().toString();
        if (title.equals("") || disc.equals("")) {
            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();
        } else {
            if (isEditState) {

                myDbManager.insertToDb(title, disc, tempUri);
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();

            } else {
                myDbManager.updateItem(title, disc, tempUri,item.getId());
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            }
            myDbManager.closeDb();
            finish();
        }
    }

    public void onClickDeleteImage(View view) {
        // картинка по умолчанию
        imageNow.setImageResource(R.drawable.baseline_image_24);
        tempUri = "empty";
        // уберается окно,поивляется кнопка
        imageGetContainer.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.VISIBLE);

    }

    public void onClickAddImage(View view) {
        // поивляется окно,уберается конопка
        imageGetContainer.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    public void onClickChooseImage(View view) {
        Intent choose = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        choose.setType("image/*");
        startActivityForResult(choose, POCK_IMAGE_CODE);
    }
}