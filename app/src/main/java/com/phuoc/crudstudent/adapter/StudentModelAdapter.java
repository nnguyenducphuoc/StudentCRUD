package com.phuoc.crudstudent.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuoc.crudstudent.AndroidUtils.AndroidUtils;
import com.phuoc.crudstudent.ApiUtil.ApiUtil;
import com.phuoc.crudstudent.MainActivity;
import com.phuoc.crudstudent.R;
import com.phuoc.crudstudent.UpdateActivity;
import com.phuoc.crudstudent.custom.CustomPopupDialogEdit;
import com.phuoc.crudstudent.interfacee.IOnClickItem;
import com.phuoc.crudstudent.model.Student;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentModelAdapter extends RecyclerView.Adapter<StudentModelAdapter.StudentModelViewHolder> implements Filterable {
    List<Student> list;
    List<Student> listOld;
    Context context;
    IOnClickItem iOnClickItem;
    Intent intent;

    public StudentModelAdapter(List<Student> list, Context context, IOnClickItem iOnClickItem) {
        this.iOnClickItem = iOnClickItem;
        this.context = context;
        this.list = list;
        this.listOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.students_row, parent, false);
        return new StudentModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentModelViewHolder holder, int position) {
        final Student student = list.get(position);
        holder.email.setText(list.get(position).getEmail());
        holder.firstNametv.setText(list.get(position).getFirst_name());
        holder.lastNametv.setText(list.get(position).getLast_name());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showDialog(holder, student);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("student", student);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void showDialog(StudentModelViewHolder holder, Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle("Thông Báo");
        builder.setMessage("Bạn có muốn xóa không?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ApiUtil.apiutil.deleteStudent(student.getId()).enqueue(new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        dialog.dismiss();
                        Toast.makeText(context, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                        iOnClickItem.callApi();
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        Toast.makeText(context, "Lỗi xóa", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    list = listOld;
                } else {
                    List<Student> list1 = new ArrayList<>();
                    for (Student student : listOld) {
                        if (student.getFirst_name().toLowerCase().contains(strSearch.toLowerCase())) {
                            list1.add(student);
                        }
                    }
                    list = list1;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<Student>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class StudentModelViewHolder extends RecyclerView.ViewHolder {
        TextView firstNametv, lastNametv, email;
        ImageButton btnEdit, btnDelete;
        public StudentModelViewHolder(@NonNull View itemView) {
            super(itemView);

            firstNametv = itemView.findViewById(R.id.tv_first_name);
            lastNametv = itemView.findViewById(R.id.tv_last_name);
            email = itemView.findViewById(R.id.tv_email);
            btnEdit = itemView.findViewById(R.id.img_btn_edit);
            btnDelete = itemView.findViewById(R.id.img_btn_close);
        }
    }


}
