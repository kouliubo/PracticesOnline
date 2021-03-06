package net.lzzy.practicesonline.activities.activities.activities.constant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.Objects;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public abstract class BaseFragment extends Fragment {
    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Populate();
    }

    protected abstract void Populate();

    protected abstract int getLayoutRes();

    protected <T extends View> T findViewById(@IdRes int id){
        return Objects.requireNonNull(getView()).findViewById(id);
    }
    public abstract void sarch(String kw);

}
