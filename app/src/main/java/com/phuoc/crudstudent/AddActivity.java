package com.phuoc.crudstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.phuoc.crudstudent.AndroidUtils.AndroidUtils;
import com.phuoc.crudstudent.ApiUtil.ApiUtil;
import com.phuoc.crudstudent.model.Student;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    EditText edtFirstname, edtLastname, edtEmail;
    Button btnThem, bthHuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initUi();

        bthHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtFirstname.getText().toString().trim().isEmpty()) {
                    edtFirstname.setError("Invalid first name");
                    return;
                }

                if (edtLastname.getText().toString().trim().isEmpty()) {
                    edtLastname.setError("Invalid last name");
                    return;
                }

                if (!AndroidUtils.isValidEmail(edtEmail.getText().toString().trim())) {
                    edtEmail.setError("Invalid email");
                    return;
                }

                AddApi();
            }
        });
    }

    private void AddApi() {

        ApiUtil.apiutil.insertStudent(
                edtFirstname.getText().toString(),
                edtLastname.getText().toString(),
                edtEmail.getText().toString()
        ).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    AndroidUtils.hideKeyboard(AddActivity.this);
                    Toast.makeText(AddActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(AddActivity.this, "Lỗi thêm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Toast.makeText(AddActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUi() {
        edtFirstname = findViewById(R.id.edt_first_name);
        edtLastname = findViewById(R.id.edt_last_name);
        edtEmail = findViewById(R.id.edt_email);
        bthHuy = findViewById(R.id.btnClosePopup);
        btnThem = findViewById(R.id.btnThemPopup);
    }


}