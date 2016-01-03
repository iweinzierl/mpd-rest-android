package com.github.iweinzierl.mpd.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class BaseListAdapter<T> extends BaseAdapter {

    public interface Filter<T> {
        List<T> performFilter(List<T> items, Object filterObj);
    }

    protected final Context context;
    protected List<T> origItems;
    protected List<T> items;

    protected Comparator<T> comparator;

    protected Filter<T> filter;

    @SuppressWarnings("unchecked")
    public BaseListAdapter(final Context context, final List<T> items) {
        super();
        this.context = context;
        this.origItems = items != null ? new ArrayList<>(items) : (List<T>) Collections.EMPTY_LIST;
        this.items = items != null ? new ArrayList<>(items) : (List<T>) Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    public BaseListAdapter(final Context context, final List<T> items, Comparator<T> comparator) {
        super();
        this.context = context;
        this.origItems = items != null ? new ArrayList<>(items) : (List<T>) Collections.EMPTY_LIST;
        this.items = items != null ? new ArrayList<>(items) : (List<T>) Collections.EMPTY_LIST;
        this.comparator = comparator;

        Collections.sort(this.items, comparator);
    }

    @SuppressWarnings("unchecked")
    public void setItems(List<T> items) {
        this.origItems = items != null ? new ArrayList<>(items) : (List<T>) Collections.EMPTY_LIST;
        this.items = items != null ? new ArrayList<>(items) : (List<T>) Collections.EMPTY_LIST;

        if (comparator != null) {
            Collections.sort(this.items, comparator);
        }

        notifyDataSetChanged();
    }

    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
        Collections.sort(this.items, comparator);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(final int i) {
        return items.get(i);
    }

    public T getTypedItem(final int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    public int getIndexOf(T item) {
        return items.indexOf(item);
    }

    public void setFilter(Filter<T> filter) {
        this.filter = filter;
    }

    public void filter(Object filterObj) {
        if (filter != null) {
            this.items = filter.performFilter(this.origItems, filterObj);
            sort();
            notifyDataSetChanged();
        }
    }

    protected void sort() {
        if (comparator != null) {
            Collections.sort(this.items, comparator);
            notifyDataSetChanged();
        }
    }
}
