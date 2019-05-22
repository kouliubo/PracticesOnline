package net.lzzy.practicesonline.activities.activities.activities.constant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.view.QuestionResult;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/5/14.
 * Description:
 */
public class GridFragment extends BaseFragment {
    public static final String ARG_QUESTION_RESULT = "argQuestionResult";
    private GridView gv;
    private TextView tv;
    private onResultSwitchListener listener;
    private BaseAdapter adapter;
    List<QuestionResult> results;

    public static GridFragment newInstance(List<QuestionResult> results) {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_QUESTION_RESULT, (ArrayList<? extends Parcelable>) results);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            results = getArguments().getParcelableArrayList(ARG_QUESTION_RESULT);
        }
    }

    @Override
    protected void Populate() {
        gv = findViewById(R.id.fragment_grid_gv);
        tv = findViewById(R.id.fragment_grid_tv);
        tv.setOnClickListener(v -> listener.gotoChart());
        if (getArguments() != null) {
            results = getArguments().getParcelableArrayList(ARG_QUESTION_RESULT);
        }
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return results.size();
            }

            @Override
            public Object getItem(int position) {
                return results.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_layout, null);
                }
                TextView textView = convertView.findViewById(R.id.grid_layout_tv);
                QuestionResult result = results.get(position);
                if (result.isRight()) {
                    textView.setBackgroundResource(R.drawable.grid_green);
                } else {
                    textView.setBackgroundResource(R.drawable.grid_accent);
                }
                textView.setText(position + 1 + "");
                return convertView;

            }
        };
        gv.setAdapter(adapter);
        gv.setOnItemClickListener((parent, view, position, id) -> listener.goBack(position));
    }

    public interface onResultSwitchListener {
        void goBack(int position);
        void gotoChart();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_grid;
    }

    @Override
    public void sarch(String kw) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onResultSwitchListener) {
            listener = (onResultSwitchListener) context;
        } else {
            throw new RuntimeException(context.toString() + "必须实现bbb");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
