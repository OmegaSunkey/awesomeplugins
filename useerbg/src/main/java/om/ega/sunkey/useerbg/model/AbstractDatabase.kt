package om.ega.sunkey.useerbg.model

import android.content.Context
import com.aliucord.Http
import com.aliucord.Utils
import com.aliucord.api.PatcherAPI
import com.aliucord.api.SettingsAPI
import om.ega.sunkey.useerbg.usrbg
import com.aliucord.utils.IOUtils
import java.io.File
import java.io.FileInputStream

abstract class AbstractDatabase() {
    abstract val regex: String
    abstract val url: String
    abstract var data: String
    abstract val mapCache: MutableMap<Long, *>
    abstract val name: String

    open fun init(ctx: Context, settings: SettingsAPI, patcher: PatcherAPI) {
        loadDB(ctx, settings)
        runPatches(patcher, settings)
    }

    private fun loadDB(ctx: Context, settings: SettingsAPI) {
         Utils.threadPool.execute {
	    getCacheFile(ctx).let {
                it.createNewFile()

                data = loadFromCache(it)
                usrbg.log.debug("Loaded $name database.")

                if (ifRecache(it.lastModified(), settings) || data.isEmpty() || data == null) {
                    downloadDB(it)
                }
            }
        }
    }

    fun downloadDB(cachedFile: File) {
    Utils.threadPool.execute {
        usrbg.log.debug("Downloading $name database...")
        Http.simpleDownload(url, cachedFile)
        usrbg.log.debug("Downloaded $name database.")

        data = loadFromCache(cachedFile)
        usrbg.log.debug("Updated $name database.")
    }
}
    private fun loadFromCache(it: File): String {
        return String(IOUtils.readBytes(FileInputStream(it)))
    }

    fun getCacheFile(ctx: Context): File {
        return File(ctx.cacheDir, "${name}.css")
    }

    private fun ifRecache(lastModified: Long, settings: SettingsAPI): Boolean {
        return System.currentTimeMillis() - lastModified > settings.getLong(
            "cacheTime",
            usrbg.REFRESH_CACHE_TIME
        ) * 60 * 1000 // 6 hours
    }

    abstract fun runPatches(patcher: PatcherAPI, settings: SettingsAPI)

}
