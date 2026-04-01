package om.ega.sunkey.userpfp.model

import com.aliucord.Http
import com.aliucord.Utils
import com.aliucord.api.PatcherAPI
import com.aliucord.api.SettingsAPI
import om.ega.sunkey.userpfp.UserPFP
import com.aliucord.utils.IOUtils
import java.io.File
import java.io.FileInputStream

abstract class AbstractDatabase() {
    abstract val regex: String
    abstract val url: String
    abstract var data: String
    abstract val mapCache: MutableMap<Long, *>
    abstract val name: String

    open fun init(settings: SettingsAPI, patcher: PatcherAPI) {
        loadDB(settings)
        runPatches(patcher, settings)
    }

    private fun loadDB(settings: SettingsAPI) {
        Utils.threadPool.execute {
            getCacheFile().let {
                it.createNewFile()

                data = loadFromCache(it)
                UserPFP.log.debug("Loaded $name database.")

                if (ifRecache(it.lastModified(), settings) || data.isEmpty() || data == null) {
                    downloadDB(it)
                }
            }
        }
    }

    fun downloadDB(cachedFile: File) {
        UserPFP.log.debug("Downloading $name database...")
        Http.simpleDownload(url, cachedFile)
        UserPFP.log.debug("Downloaded $name database.")

        data = loadFromCache(cachedFile)
        UserPFP.log.debug("Updated $name database.")
    }

    private fun loadFromCache(it: File): String {
        return String(IOUtils.readBytes(FileInputStream(it)))
    }

    fun getCacheFile(): File {
        return File("/sdcard/Aliucord/${name}.txt")
    }

    private fun ifRecache(lastModified: Long, settings: SettingsAPI): Boolean {
        return System.currentTimeMillis() - lastModified > settings.getLong(
            "cacheTime",
            UserPFP.REFRESH_CACHE_TIME
        ) * 60 * 1000 // 6 hours
    }

    abstract fun runPatches(patcher: PatcherAPI, settings: SettingsAPI)

}
