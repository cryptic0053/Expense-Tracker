package com.example.expensetracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expensetracker.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


public class ExpenseFragment extends Fragment {


    private FirebaseAuth mAuth;

    private DatabaseReference mExpenseDatabase;

    private RecyclerView recyclerView;

    private TextView expenseSumResult;

    private EditText edtAmmount,edtType,edtNote;

    private Button btnUpdate,btnDelete;

    private String  type,note,post_Key;

    private int ammount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View myView=inflater.inflate(R.layout.fragment_expense,container,false);

       mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        expenseSumResult=myView.findViewById(R.id.expense_txt_result);



        recyclerView=myView.findViewById(R.id.recycler_id_expense);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int expenseSum=0;
                for(DataSnapshot mySnapshot:dataSnapshot.getChildren()){

                    Data data=mySnapshot.getValue(Data.class);
                    expenseSum+=data.getAmount();

                    String strExpenseSum=String.valueOf(expenseSum);

                    expenseSumResult.setText(strExpenseSum+".00");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       return myView;
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mExpenseDatabase, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder viewHolder, @SuppressLint("RecyclerView") int position, @NonNull Data model) {

                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmmount(model.getAmount());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_Key=getRef(position).getKey();
                        type=model.getType();
                        note=model.getNote();
                        ammount=model.getAmount();

                        updateDataItem();

                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    private static class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public MyViewHolder(View itemView){
            super(itemView);
            mView=itemView;
        }
        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }
        private void setType(String type)
        {
            TextView mType=mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }
        private void setNote(String note)
        {
            TextView mNote=mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }
        private void setAmmount(int ammount){
            TextView mAmmount=mView.findViewById(R.id.ammount_txt_expense);

            String strammount=String.valueOf(ammount);

            mAmmount.setText(strammount);
        }
    }

    private void updateDataItem(){
        AlertDialog.Builder myDialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myView=inflater.inflate(R.layout.update_data_item,null);
        myDialog.setView(myView);

        edtAmmount=myView.findViewById(R.id.ammount_edt);
        edtNote=myView.findViewById(R.id.note_edt);
        edtType=myView.findViewById(R.id.type_edt);

        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmmount.setText(String.valueOf(ammount));
        edtAmmount.setSelection(String.valueOf(ammount).length());

        btnUpdate=myView.findViewById(R.id.btn_Update);
        btnDelete=myView.findViewById(R.id.btn_Delete);

        final AlertDialog dialog=myDialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type=edtType.getText().toString().trim();
                note=edtType.getText().toString().trim();
                String stAmmount=String.valueOf(ammount);
                stAmmount=edtAmmount.getText().toString().trim();
                int intAmmount=Integer.parseInt(stAmmount);
                String mDate= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(intAmmount,type,note,post_Key,mDate);
                mExpenseDatabase.child(post_Key).setValue(data);

                dialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mExpenseDatabase.child(post_Key).removeValue();

                dialog.dismiss();

            }
        });
        dialog.show();
    }
}