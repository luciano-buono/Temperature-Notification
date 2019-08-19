package com.firebase.temperaturenotification;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;


//
//public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder> {
//
//    interface OnItemCheckListener {
//        void onItemCheck(Item item);
//        void onItemUncheck(Item item);
//    }
//
//
//
//    @NonNull
//    private OnItemCheckListener onItemCheckListener;
//
//    public OptionsAdapter (List<Item> items, @NonNull OnItemCheckListener onItemCheckListener) {
//        this.items = items;
//        this.onItemClick = onItemCheckListener;
//    }
//
//
//    @NonNull
//    @Override
//    public OptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_options, parent, false );
//        return new OptionsViewHolder(view);
//
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof OptionsViewHolder) {
//            final Item currentItem = items.get(position);
//
//
//
//            ((OptionsViewHolder) holder).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((OptionsViewHolder) holder).checkbox.setChecked(
//                            !((OptionsViewHolder) holder).checkbox.isChecked());
//                    if (((OptionsViewHolder) holder).checkbox.isChecked()) {
//                        onItemClick.onItemCheck(currentItem);
//                    } else {
//                        onItemClick.onItemUncheck(currentItem);
//                    }
//                }
//            });
//        }
//    }
//
//    static class OptionsViewHolder extends RecyclerView.ViewHolder {
//        CheckBox checkbox;
//        View itemView;
//
//
//
//        public OptionsViewHolder(View itemView) {
//            super(itemView);
//            this.itemView = itemView;
//            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
//            checkbox.setClickable(false);
//
//        }
//
//        public void setOnClickListener(View.OnClickListener onClickListener) {
//            itemView.setOnClickListener(onClickListener);
//        }
//    }
//}