package com.aliucord.plugins;

import android.content.Context;

import com.aliucord.Http;
import com.aliucord.Logger;
import com.aliucord.PluginManager;
import com.aliucord.annotations.AliucordPlugin;
import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.aliucord.patcher.PinePrePatchFn;
import com.aliucord.utils.IOUtils;
import com.discord.utilities.icon.IconUtils;
import com.discord.widgets.user.profile.UserProfileHeaderViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings({"unused"})
@AliucordPlugin
public class UserBG extends Plugin {
    public final static long REFRESH_CACHE_TIME = 6*60;

    private static final Pattern bannerMatch = Pattern.compile("^https://cdn.discordapp.com/banners/\\d+/[a-z0-9_]+\\.\\w{3,5}\\?size=\\d+$");
    private static final String url = "https://discord-custom-covers.github.io/usrbg/dist/usrbg.css";
    private final String regex = ".*?\\(\"(.*?)\"";
    private static String css;
    private static final Logger log = new Logger();
    private Map<Long, String> urlCache = new HashMap<Long, String>();

    public UserBG() {
        settingsTab = new SettingsTab(PluginSettings.class).withArgs(settings);
    }

    @Override
    public void start(Context ctx) throws NoSuchMethodException {
        getDB(ctx);

        patcher.patch(IconUtils.class.getDeclaredMethod("getForUserBanner", long.class, String.class, Integer.class, boolean.class), new PinePatchFn(callFrame -> {
            if (css == null || (callFrame.getResult() != null && settings.getBool("nitroBanner", true) && bannerMatch.matcher(callFrame.getResult().toString()).find())) return; // could not get USRBG database in time or wasn't available

            var id = (long) callFrame.args[0];

            if (urlCache.containsKey(id))
                callFrame.setResult(withSize(urlCache.get(id), (Integer) callFrame.args[2]));
            else {
                var matcher = Pattern.compile(id + regex, Pattern.DOTALL).matcher(css);
                if (matcher.find()) {
                    String url = matcher.group(1);
                    urlCache.put(id, url);
                    callFrame.setResult(withSize(url, (Integer) callFrame.args[2]));
                }
            }
        }));

        if (settings.getBool("downscaleToFrame", false)) {
            // shitty "optimization" features
            patcher.patch(b.f.j.a.c.a.class.getConstructors()[0], new PinePrePatchFn((callFrame -> {
                callFrame.args[3] = true;
            })));

            patcher.patch(b.f.j.e.p.class.getConstructors()[0], new PinePrePatchFn((callFrame -> {
                callFrame.args[8] = true;
                callFrame.args[9] = true;
                // i, e
                // z7, z6
            })));
        }

        if (PluginManager.isPluginEnabled("ViewProfileImages")) { // inb4 ven asks what the hell im doing
            patcher.patch(UserProfileHeaderViewModel.ViewState.Loaded.class.getDeclaredMethod("getBanner"), new PinePatchFn(callFrame -> {
                if (css == null) return;
                var user = ((UserProfileHeaderViewModel.ViewState.Loaded) callFrame.thisObject).getUser();
                if (callFrame.getResult() == null && (urlCache.containsKey(user.getId()) && settings.getBool("nitroBanner", true))) callFrame.setResult("https://usrbg.cumcord.com/");
            }));
        }
    }

    private void getDB(Context ctx) {
        new Thread(() -> {
            try {
                File cachedFile = getCacheFile(ctx);
                cachedFile.createNewFile();

                css = loadFromCache(cachedFile);
                log.debug("Loaded USRBG database.");

                if (ifRecache(cachedFile.lastModified()) || css.isEmpty() || css == null) {
                    downloadDB(cachedFile);
                }
            } catch (Throwable e) { log.error(e); }
        }).start();
    }

    public static void downloadDB(File cachedFile) {
        try {
            log.debug("Downloading USRBG database...");
            Http.simpleDownload(url, cachedFile);
            log.debug("Downloaded USRBG database.");

            css = loadFromCache(cachedFile);
            log.debug("Updated USRBG database.");
        } catch (Exception e) {
            log.error(e);
        }
    }

    private static String loadFromCache(File cache) {
        try {
            return new String(IOUtils.readBytes(new FileInputStream(cache)));
        } catch (Throwable e) {
            log.error(e);
        }
        return null;
    }

    private String withSize(String background, Integer size) {
        return background + "?size=" + IconUtils.getMediaProxySize(size);
    }

    private boolean ifRecache(long lastModified) {
        return (System.currentTimeMillis() - lastModified) > (settings.getLong("cacheTime", UserBG.REFRESH_CACHE_TIME)*60*1000); // 6 hours
    }

    public static File getCacheFile(Context ctx) {
        return new File(ctx.getCacheDir(), "db.css");
    }

    @Override
    public void stop(Context context) {
        patcher.unpatchAll();
        css = null;
    }
}