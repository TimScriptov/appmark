package com.vipapp.appmark2.menu;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.vipapp.appmark2.adapter.DefaultAdapter;
import com.vipapp.appmark2.callbacks.PushCallback;
import com.vipapp.appmark2.items.CallbackObject;
import com.vipapp.appmark2.items.Item;

import java.util.ArrayList;

public abstract class DefaultMenu<T, M extends RecyclerView.ViewHolder> {
    private ArrayList<PushCallback<CallbackObject>> callbacks = new ArrayList<>();
    ArrayList<T> array = new ArrayList<>();
    private DefaultAdapter adapter = null;

    public void onAdapterReceived(DefaultAdapter adapter){
        this.adapter = adapter;
    }
    public void onValueReceived(Item item){}
    public void notifyAdapter(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    public int getItemViewType(int position){ return 0; }
    public M getViewHolder(ViewGroup parent, int itemType){ return null; }

    @SuppressWarnings("unchecked")
    void onCallback(int id, Object object){
        for(PushCallback<CallbackObject> callback: callbacks) {
            callback.onComplete(new CallbackObject(id, object));
        }
    }
    public void addCallbacks(PushCallback<CallbackObject> callback){
        callbacks.add(callback);
    }
    void pushArray(ArrayList<T> arrayList){
        pushArray(arrayList, true);
    }
    void pushArray(ArrayList<T> arrayList, boolean need_to_notify){
        this.array = arrayList;
        adapter.pushArray(array, need_to_notify);
    }
    void pushItem(Item item){
        adapter.onPush(item);
    }
    public int size(){
        return array.size();
    }

    void notifyRemoved(int pos){
        adapter.notifyItemRemoved(pos);
    }
    void notifyChanged(int pos){
        adapter.notifyItemChanged(pos);
    }
    void notifyInserted(int pos){
        adapter.notifyItemInserted(pos);
    }

    @Nullable
    RecyclerView getRecyclerView(){
        return adapter != null? adapter.getRecyclerView(): null;
    }

    public abstract ArrayList<T> list(Context context);
    public abstract void bind(M vh, T item, int i);
}