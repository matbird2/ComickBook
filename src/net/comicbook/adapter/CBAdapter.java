package net.comicbook.adapter;

import java.util.ArrayList;
import java.util.List;

import net.comicbook.bean.bmob.Album;
import net.comicbook.view.CBItemView;
import net.comicbook.view.CBItemView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class CBAdapter extends BaseAdapter{
	public List<Album> lists = new ArrayList<Album>();

    private String currentItem;

    public void appendList(List<Album> list) {
        if (!lists.containsAll(list) && list != null && list.size() > 0) {
            lists.addAll(list);
        }
        notifyDataSetChanged();
    }

    @RootContext
    Context context;

    public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }

    public void currentItem(String item) {
        this.currentItem = item;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CBItemView newItemView;

        if (convertView == null) {
            newItemView = CBItemView_.build(context);
        } else {
            newItemView = (CBItemView) convertView;
        }

        Album album = lists.get(position);
        newItemView.setCBItemView(album);
        return newItemView;
    }
}
