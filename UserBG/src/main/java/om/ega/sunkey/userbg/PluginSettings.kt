package om.ega.sunkey.userbg

import android.content.Context
import com.aliucord.Utils.createCheckedSetting
import com.aliucord.api.SettingsAPI
import com.aliucord.views.TextInput
import android.text.Editable
import android.text.InputType
import android.view.View
import com.discord.app.AppFragment
import com.discord.views.CheckedSetting
import com.aliucord.PluginManager
import com.aliucord.Utils
import com.aliucord.fragments.SettingsPage
import om.ega.sunkey.userbg.model.APFP
import com.aliucord.views.Button
import com.discord.utilities.view.text.TextWatcher
import java.lang.Exception

class PluginSettings(private val settings: SettingsAPI) : SettingsPage() {
    override fun onViewBound(view: View) {
        super.onViewBound(view)
        setActionBarTitle("UserBG")
        val textInput = TextInput(view.context)
        //textInput.hint = "Refresh UserBG database time (minutes)"
        textInput.editText!!.setText(
            settings.getLong("cacheTime", UserBG.REFRESH_CACHE_TIME).toString()
        )
        textInput.editText!!.inputType = InputType.TYPE_CLASS_NUMBER
        textInput.editText!!.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(editable: Editable) {
                try {
                    if (java.lang.Long.valueOf(editable.toString()) != 0L) settings.setLong(
                        "cacheTime",
                        java.lang.Long.valueOf(editable.toString())
                    )
                } catch (e: Exception) {
                    settings.setLong("cacheTime", UserBG.REFRESH_CACHE_TIME)
                }
            }
        })
        val refreshCache = Button(view.context)
        refreshCache.text = "Redownload databases"
        refreshCache.setOnClickListener { button: View? ->
            Utils.threadPool.execute {
                Utils.showToast("Downloading databases...")
                context?.let { UserBG.USRBG.getCacheFile(it) }?.let { UserBG.USRBG.downloadDB(it) }
                context?.let { UserBG.APFP.getCacheFile(it) }?.let { UserBG.APFP.downloadDB(it) }
                Utils.showToast("Downloaded databases.")

                with(UserBG.APFP) {
                    Utils.showToast("Downloading databases...")

                    Utils.showToast("Downloaded databases.")
                }
            }
        }
        addView(textInput)
        addView(
            createCheckedSetting(
                view.context,
                "Prioritize Nitro banner over USRBG banner",
                "nitroBanner",
                true
            )
        )
        addView(
            createCheckedSetting(
                view.context,
                "Use downscaleToFrame and partial cache (experimental)",
                "downscaleToFrame",
                false
            )
        )
        addView(refreshCache)
    }

    private fun createCheckedSetting(
        ctx: Context,
        title: String,
        setting: String,
        checked: Boolean
    ): CheckedSetting {
        val checkedSetting = createCheckedSetting(ctx, CheckedSetting.ViewType.SWITCH, title, null)
        checkedSetting.isChecked = settings.getBool(setting, checked)
        checkedSetting.setOnCheckedListener { check: Boolean? ->
            settings.setBool(setting, check!!)
            PluginManager.stopPlugin("RainbowCord")
            PluginManager.startPlugin("RainbowCord")
        }
        return checkedSetting
    }
}
