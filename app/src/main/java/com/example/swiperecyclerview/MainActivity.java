package com.example.swiperecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {
RecyclerView recyclerView;
ArrayList<String > arrayList=new ArrayList<>();
MainAdapter adapter; String deleted=null;
List<String> archived=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.rec);
        arrayList.addAll(Arrays.asList("Java1","Java2","Java3","Java4","Java5","Java6","Java7","Java8","Java9","Java10","Java11","Java12","Java13","Java14","Java15",
                "Java16","Java17","Java18","Java19","Java20"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MainAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimaryDark))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
         final int position=viewHolder.getAdapterPosition();
         switch (direction){
             case ItemTouchHelper.LEFT :
                 deleted=arrayList.get(position);
                 arrayList.remove(position);
                 adapter.notifyItemRemoved(position);
             Snackbar.make(recyclerView,deleted, Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     arrayList.add(position,deleted);
                     adapter.notifyItemInserted(position);
                 }
             }).show();break;
             case ItemTouchHelper.RIGHT:
                 final String name=arrayList.get(position);
                 archived.add(name);
                 arrayList.remove(position);
                 adapter.notifyItemRemoved(position);
                 Snackbar.make(recyclerView,name+" Archived...", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                        archived.remove(archived.lastIndexOf(name));
                        arrayList.add(position,name);
                        adapter.notifyItemInserted(position);
                     }
                 }).show();break;

         }
            }
        }).attachToRecyclerView(recyclerView); }}