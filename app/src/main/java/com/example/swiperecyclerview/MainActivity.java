package com.example.swiperecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButton;
    Toolbar toolbar; TextView textView;
    public int position=-1;
    public boolean isActionmode=false;
    RecyclerView recyclerView;
ArrayList<String > arrayList=new ArrayList<>();
    ArrayList<String> userSelection=new ArrayList<>();
    int counter=0;
MainAdapter adapter; String deleted=null;
List<String> archived=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.tool);setSupportActionBar(toolbar);
        textView=findViewById(R.id.tt);textView.setVisibility(View.GONE);
        imageButton=findViewById(R.id.bt); imageButton.setVisibility(View.GONE);
        recyclerView=(RecyclerView)findViewById(R.id.rec);
        arrayList.addAll(Arrays.asList("Java1","Java2","Java3","Java4","Java5","Java6","Java7","Java8","Java9","Java10","Java11","Java12","Java13","Java14","Java15",
                "Java16","Java17","Java18","Java19","Java20"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MainAdapter(this,arrayList);
        recyclerView.setAdapter(adapter);
         imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearActionMode();
            }
        });
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
        }).attachToRecyclerView(recyclerView); }

    private void clearActionMode() {
        isActionmode=false;
        textView.setVisibility(View.GONE);
        textView.setText("0 item selected");
        counter=0;userSelection.clear();
        toolbar.getMenu().clear();
        adapter.notifyDataSetChanged();
    }


    public  void check(View view ,int index){
        try{
            if (((CheckBox) view).isChecked()) {
                userSelection.add(arrayList.get(index));
                counter++;
                updateToolBarText(counter);
            } else {
                userSelection.remove(arrayList.get(index));
                counter--;
                updateToolBarText(counter);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    public  void  startSelection(int index){
        try  {
            if (!isActionmode) {
                isActionmode = true;
                userSelection.add(arrayList.get(index));
                counter++;
                updateToolBarText(counter);
                textView.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.VISIBLE);
                toolbar.inflateMenu(R.menu.rec_delete);
                position = index;
                adapter.notifyDataSetChanged();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    private  void updateToolBarText(int counter) {
        if (counter==0)
            textView.setText("0 items selected");
        else if (counter==1)
            textView.setText("1 items selected");
        else
            textView.setText(counter+" Items selected");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.delete && userSelection.size()>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete "+userSelection.size()+" items ?");
            builder.setTitle("Confirm");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
            for (String s : userSelection){
                arrayList.remove(s);
            }
            updateToolBarText(0);
            clearActionMode();

        }
        return super.onOptionsItemSelected(item);
    }
}