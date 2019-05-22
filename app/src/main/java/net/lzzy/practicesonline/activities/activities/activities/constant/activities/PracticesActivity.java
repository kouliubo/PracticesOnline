package net.lzzy.practicesonline.activities.activities.activities.constant.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.activities.activities.constant.fragments.PracticesFragment;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.Practice;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.PracticeFactory;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.Question;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.QuestionFactory;
import net.lzzy.practicesonline.activities.activities.activities.constant.network.DetectWebService;
import net.lzzy.practicesonline.activities.activities.activities.constant.utils.AppUtils;
import net.lzzy.practicesonline.activities.activities.activities.constant.utils.ViewUtils;
import net.lzzy.sqllib.SqlRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class PracticesActivity extends BaseActivity implements PracticesFragment.PracticeSelectedListener{
    public static final String EXTRA_PRACTICE_ID="extraPracticeId";
    public static final String EXTRA_API_ID="extraApiId";
    public static final String EXTRA_LOCAL_COUNT = "extraLocalCount";
    private ServiceConnection connection;
    private boolean refresh=false;
    private SqlRepository<Practice> repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        //region⑤Activity中启动Service(bindService/startService)
        //service 绑定
        if (getIntent()!=null){
            refresh=getIntent().getBooleanExtra(DetectWebService.EXTRA_REFRESH,false);
        }
        connection =new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DetectWebService.DetectWebBinder binder= (DetectWebService.DetectWebBinder) service;
                binder.detect();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        //读取本地数据库数据
        int localCount = PracticeFactory.getInstance().get().size();
        //启动后台服务
        Intent intent=new Intent(this,DetectWebService.class);
        intent.putExtra(EXTRA_LOCAL_COUNT,localCount);
        bindService(intent,connection,BIND_AUTO_CREATE);
        //endregion
    }

    private void initViews() {
        SearchView search=findViewById(R.id.bar_title_search);
        search.setQueryHint("请输入关键词搜索");
        search.setOnQueryTextListener(new ViewUtils.AbstractQueryListener() {
            @Override
            public void handleQuery(String kw) {
                ((PracticesFragment)getFragment()).sarch(kw);
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
        SearchView.SearchAutoComplete auto=search.findViewById(R.id.search_src_text);
        auto.setHighlightColor(Color.WHITE);
        auto.setTextColor(Color.WHITE);
        ImageView icon=search.findViewById(R.id.search_button);
        ImageView icX=search.findViewById(R.id.search_close_btn);
        ImageView icG=search.findViewById(R.id.search_go_btn);
        icon.setColorFilter(Color.WHITE);
        icG.setColorFilter(Color.WHITE);
        icX.setColorFilter(Color.WHITE);
    }

    private void setPracticeDown(String id) {
        Practice practice = getById(id);
        if (practice != null) {
            practice.setDownloaded(true);
            repository.update(practice);
        }
    }

    public Practice getById(String id) {
        return repository.getById(id);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("退出应用")
                .setPositiveButton("确定",(dialog, which) -> AppUtils.exit())
                .show();
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_practices;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_practices_container;
    }

    @Override
    protected Fragment createFragment() {
        return new PracticesFragment();
    }

    @Override
    public void OnPractice(String practiceId, int apiId) {
        Intent intent=new Intent(this,QuestionActivity.class);
        intent.putExtra(EXTRA_PRACTICE_ID,practiceId);
        intent.putExtra(EXTRA_API_ID,apiId);
        startActivity(intent);
    }
}
