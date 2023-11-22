package com.phuoc.crudstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class UpdateActivity extends AppCompatActivity {
    Student student;
    EditText edtFirstName, edtLastName, edtEmail;
    Button btnXacNhan, btnClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtEmail = findViewById(R.id.edt_email);
        btnClose = findViewById(R.id.btnClosePopup);
        btnXacNhan = findViewById(R.id.btnXacNhanPopup);

        student = (Student) getIntent().getSerializableExtra("student");
        edtFirstName.setText(student.getFirst_name());
        edtLastName.setText(student.getLast_name());
        edtEmail.setText(student.getEmail());

        onClickButton();
    }

    private void onClickButton() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtFirstName.getText().toString().trim().isEmpty()) {
                    edtFirstName.setError("Invalid first name");
                    return;
                }

                if (edtLastName.getText().toString().trim().isEmpty()) {
                    edtLastName.setError("Invalid last name");
                    return;
                }

                if (!AndroidUtils.isValidEmail(edtEmail.getText().toString().trim())) {
                    edtEmail.setError("Invalid email");
                    return;
                }

                CallApi();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void CallApi() {
        ApiUtil.apiutil.updateStudent(student.getId(), edtFirstName.getText().toString(), edtLastName.getText().toString(), edtEmail.getText().toString())
                .enqueue(new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        if (response.isSuccessful()) {
                            AndroidUtils.hideKeyboard(UpdateActivity.this);
                            Toast.makeText(UpdateActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(UpdateActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        Toast.makeText(UpdateActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}