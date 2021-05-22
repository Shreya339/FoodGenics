package com.example.foodgenicsmain;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ParallelExecution implements Callable {

    String  name,constraint;
    //CharSequence constraint;
    private List<SearchResult> exampleListFull;
    String pattern;
    public ParallelExecution(List<SearchResult> exampleListFull, String constraint, String name) {
        this.exampleListFull = exampleListFull;
        this.constraint = constraint;
        this.name = name;
    }

    @Override
    public Object call() throws Exception {
        Log.i("in thred ",exampleListFull.get(0).toString());
        List<SearchResult> filterlist = new ArrayList<>();

        if (constraint == null || constraint.length() == 0) {
            filterlist.addAll(exampleListFull);
        } else {
            pattern = constraint.toLowerCase().trim();
            for (SearchResult item : exampleListFull) {
                //where search takes place
                if (item.getIngredients().toLowerCase().contains(pattern)) {
                    filterlist.add(item);
                }
            }

        }
        return filterlist;
    }
}
