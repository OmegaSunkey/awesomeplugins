package com.aliucord.plugins;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.view.View;

import com.aliucord.PluginManager;
import com.aliucord.Utils;
import com.aliucord.api.SettingsAPI;
import com.aliucord.fragments.SettingsPage;
import com.aliucord.views.Button;
import com.aliucord.views.TextInput;
import com.discord.utilities.view.text.TextWatcher;
import com.discord.views.CheckedSetting;

public class PluginSettings extends SettingsPage {
    private final SettingsAPI settings;
    public PluginSettings(SettingsAPI settings) { this.settings = settings; }

    public void onViewBound(View view) {
        super.onViewBound(view);
        setActionBarTitle("UserBG");

        TextInput textInput = new TextInput(view.getContext());
        textInput.setHint("Refresh UserBG database time (minutes)");
        textInput.getEditText().setText(String.valueOf(settings.getLong("cacheTime", UserBG.REFRESH_CACHE_TIME)));
        textInput.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        textInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Long.valueOf(editable.toString()) != 0)
                        settings.setLong("cacheTime", Long.valueOf(editable.toString()));
                } catch (Exception e) {
                    settings.setLong("cacheTime", UserBG.REFRESH_CACHE_TIME);
                }
            }
        });

        Button refreshCache = new Button(view.getContext());
        refreshCache.setText("Redownload USRBG database");
        refreshCache.setOnClickListener((button) -> {
            new Thread(() -> {
                Utils.showToast(view.getContext(), "Downloading USRBG database...");
                UserBG.downloadDB(UserBG.getCacheFile(view.getContext()));
                Utils.showToast(view.getContext(), "Downloaded USRBG database.");
            }).start();
        });

        addView(textInput);
        addView(createCheckedSetting(view.getContext(), "Prioritize Nitro banner over USRBG banner", "nitroBanner", true));
        addView(createCheckedSetting(view.getContext(), "Use downscaleToFrame and partial cache (experimental)", "downscaleToFrame", false));
        addView(refreshCache);
    }

    private CheckedSetting createCheckedSetting(Context ctx, String title, String setting, boolean checked) {
        CheckedSetting checkedSetting = Utils.createCheckedSetting(ctx, CheckedSetting.ViewType.SWITCH, title, null);

        checkedSetting.setChecked(settings.getBool(setting, checked));
        checkedSetting.setOnCheckedListener( check -> {
            settings.setBool(setting, check);
        });

        return checkedSetting;
    }
}