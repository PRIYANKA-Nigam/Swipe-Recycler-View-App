package com.example.swiperecyclerview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Context context;
    ArrayList<String> arrayList;
     MainActivity mainActivity;

    public MainAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.mainActivity=(MainActivity)context;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items,parent,false);
        return new ViewHolder(view,mainActivity); }
        @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
holder.textView.setText(arrayList.get(position));
            if (mainActivity.position == position){
                holder.checkBox.setChecked(true);
         //  mainActivity.position=-1;
            }else
            {
                holder.checkBox.setChecked(false);
            }
//            if (mainActivity.isActionmode){
//                Anim anim =new Anim(10,holder.linearLayout);
//                anim.setDuration(300000000);
//                holder.linearLayout.setAnimation(anim);
//            }else {
//                Anim anim =new Anim(0,holder.linearLayout);
//                anim.setDuration(300);
//                holder.linearLayout.setAnimation(anim);
//                holder.checkBox.setChecked(false);
//            }
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                   mainActivity.startSelection(position);
                    return true;
                }
            });
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  mainActivity.check(view,position);

                }
            });
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(context,arrayList.get(holder.getAdapterPosition()),Toast.LENGTH_SHORT).show(); }}); }@Override
    public int getItemCount() {
        return arrayList.size(); }
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final MainActivity mainActivity;
            TextView textView;
            LinearLayout linearLayout;
            CardView cardView;
            CheckBox checkBox;
        public ViewHolder(@NonNull View itemView,MainActivity mainActivity) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.chh);
            linearLayout=itemView.findViewById(R.id.l);
            cardView=itemView.findViewById(R.id.cdd);
            textView=itemView.findViewById(R.id.ttt);
            this.mainActivity=mainActivity;
        }
    }
//    class Anim extends Animation {
//        private  int width,startWidth;
//        private View view;
//
//        public Anim(int width, View view) {
//            this.width = width;
//            this.startWidth = view.getWidth();
//            this.view = view;
//        }
//
//        @Override
//        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            int newWidth=startWidth+(int)((width-startWidth)*interpolatedTime);
//            view.getLayoutParams().width=newWidth;
//            view.requestLayout();
//            super.applyTransformation(interpolatedTime, t);
//        }
//
//        @Override
//        public boolean willChangeBounds() {
//            return true;
//        }
//    }
}
