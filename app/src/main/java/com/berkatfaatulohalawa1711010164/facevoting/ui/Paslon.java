package com.berkatfaatulohalawa1711010164.facevoting.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.berkatfaatulohalawa1711010164.facevoting.API.APIService;
import com.berkatfaatulohalawa1711010164.facevoting.API.NoConnectivityException;
import com.berkatfaatulohalawa1711010164.facevoting.R;
import com.berkatfaatulohalawa1711010164.facevoting.adapter.PaslonAdapter;
import com.berkatfaatulohalawa1711010164.facevoting.model.MenuModel;
import com.berkatfaatulohalawa1711010164.facevoting.model.PaslonModel;
import com.berkatfaatulohalawa1711010164.facevoting.response.GetPaslon;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Paslon extends AppCompatActivity {
    private RecyclerView gridView;
    private PaslonAdapter paslonAdapter;
    private List<PaslonModel> paslonModels;
    private ProgressDialog progressDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paslon);

        init();
        setupRecyclerView();
        getPaslon(getApplicationContext());
    }

    private void init(){
        progressDialog = ProgressDialog.show(this, "", "Loading.....", true, false);
        gridView = findViewById(R.id.rcPaslon);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        paslonAdapter = new PaslonAdapter(getApplicationContext(), new ArrayList<PaslonModel>());
        gridView.setLayoutManager(linearLayoutManager);
        gridView.setAdapter(paslonAdapter);
    }

    private void getPaslon(Context mContext){
        try{
            String idKategori = "1";
            Call<GetPaslon> call= APIService.Factory.create(mContext).postPaslon("1");
            call.enqueue(new Callback<GetPaslon>() {
                @Override
                public void onResponse(Call<GetPaslon> call, Response<GetPaslon> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        if(response.body() != null){
                            paslonModels = response.body().getListPaslon();
                            paslonAdapter.replaceData(paslonModels);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetPaslon> call, Throwable t) {
                    progressDialog.dismiss();
                    if(t instanceof NoConnectivityException) {
                        displayExceptionMessage("Offline, cek koneksi internet anda!");
                    }
                }
            });
        }catch (Exception e){
            progressDialog.dismiss();
            e.printStackTrace();
            displayExceptionMessage(e.getMessage());
        }
    }
    public void displayExceptionMessage(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}