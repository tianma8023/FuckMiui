package com.tianma.fuckmiui.app;

import android.os.Bundle;

import com.tianma.fuckmiui.R;
import com.tianma.fuckmiui.app.base.BaseActivity;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();

        mToolbar.post(() -> getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_content, new MainSettingsFragment())
                .commit());
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

}
