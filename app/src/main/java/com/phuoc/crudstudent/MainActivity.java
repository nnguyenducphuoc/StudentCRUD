package com.phuoc.crudstudent;

import static com.phuoc.crudstudent.R.menu.menu_toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.phuoc.crudstudent.AndroidUtils.AndroidUtils;
import com.phuoc.crudstudent.ApiUtil.ApiUtil;
import com.phuoc.crudstudent.adapter.StudentModelAdapter;
import com.phuoc.crudstudent.custom.CustomPopupDialogEdit;
import com.phuoc.crudstudent.interfacee.IOnClickItem;
import com.phuoc.crudstudent.model.Student;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements IOnClickItem{
    private List<Student> studentList;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private SearchView searchView;
    StudentModelAdapter studentModelAdapter;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        getListData();
        callApi();

     //   SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
     //   searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                studentModelAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                studentModelAdapter.getFilter().filter(newText);
                return false;
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.add_student) {
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

    }



    public void callApi() {
        ApiUtil.apiutil.readStudents().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Student>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull List<Student> students) {
                        studentList = students;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(MainActivity.this, "Loi!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        studentModelAdapter = new StudentModelAdapter(studentList, getApplicationContext(), MainActivity.this::callApi);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(studentModelAdapter);
                    }
                });
    }

    private void getListData() {
        studentList = new ArrayList<>();
    }

    private void initUi() {
        recyclerView = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.search_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null) {
            disposable.dispose();
        }
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}