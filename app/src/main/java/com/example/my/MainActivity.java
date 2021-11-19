package com.example.my;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.MyAdapter.ListItem;
import com.example.my.MyAdapter.MainAdapter;
import com.example.my.R;
import com.example.my.db.AppEcoter;
import com.example.my.db.MyDbManager;
import com.example.my.db.OnDataResived;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDataResived {
    private MyDbManager myDbManager;
    private EditText edTitle, edDisc;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


//       app:cardElevation="10dp" сглажевает углы
//       app:cardCornerRadius="10dp" сглажевает углы
    }

    // вызывается меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.id_search);
        SearchView sv = (SearchView) item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // нажатие на кнопку поиска
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // по ведению буквы
            @Override
            public boolean onQueryTextChange(final String newText) {
                // поиск по буквам
                readFromDb(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // переменые
    public void init() {
        myDbManager = new MyDbManager(this);
        edTitle = findViewById(R.id.edTitle);
        edDisc = findViewById(R.id.edDisc);
        recyclerView = findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);
        getItemTouchHelper().attachToRecyclerView(recyclerView);

    }

    // запускает базу данных
    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
        readFromDb("");
    }

    // переход в другой класс
    public void onClickAdd(View view) {
        // записывает в базу данных
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);


    }

    // при выходи из приложения
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }


    // свайп
    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainAdapter.removeItem(viewHolder.getAdapterPosition(), myDbManager);
            }
        });
    }

    private void readFromDb(final String text) {
        // поиск по буквам
        AppEcoter.getInstance().getSunIo().execute(new Runnable() {
            @Override
            public void run() {
                myDbManager.getFromDb(text, MainActivity.this);
            }
        });
    }


    @Override
    public void onReceived(List<ListItem> list) {
        AppEcoter.getInstance().getMainIo().execute(new Runnable() {
            @Override
            public void run() {
                mainAdapter.updateAdapter(list);
            }
        });
    }
}
