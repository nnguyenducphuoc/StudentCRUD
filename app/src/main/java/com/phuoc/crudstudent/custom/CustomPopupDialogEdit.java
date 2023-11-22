package com.phuoc.crudstudent.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.phuoc.crudstudent.R;

public class CustomPopupDialogEdit extends Dialog implements View.OnClickListener {
    Button btnClose, btnXacNhan;
    public CustomPopupDialogEdit(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Đặt layout cho dialog
        setContentView(R.layout.custom_popup_edit);

        // Xác định các điều khiển trong layout
        btnClose = findViewById(R.id.btnClosePopup);
        btnXacNhan = findViewById(R.id.btnXacNhanPopup);
        // Gắn lắng nghe sự kiện cho nút đóng popup
        btnClose.setOnClickListener(this);
        btnXacNhan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Đóng popup khi nút đóng được nhấn
        dismiss();
    }
}

