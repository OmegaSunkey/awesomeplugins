package om.ega.sunkey.userpfp

import android.content.Context
import com.aliucord.api.SettingsAPI
import com.aliucord.Utils.createCheckedSetting
import com.aliucord.views.TextInput
import android.text.Editable
import android.text.InputType
import android.view.View
import com.aliucord.PluginManager
import com.aliucord.Utils
import com.aliucord.fragments.SettingsPage
import om.ega.sunkey.userpfp.model.APFP
import com.aliucord.views.Button
import com.discord.views.CheckedSetting
import com.discord.utilities.view.text.TextWatcher
import java.lang.Exception

class PluginSettings(private val settings: SettingsAPI) : SettingsPage() {
    override fun onViewBound(view: View) {
        super.onViewBound(view)
        setActionBarTitle("UserPFP")
        val textInput = TextInput(view.context, "Refresh UserPFP db time (minutes)")
        //textInput.hint = "Refresh UserPFP database time (minutes)"
        textInput.editText!!.setText(
            settings.getLong("cacheTime", UserPFP.REFRESH_CACHE_TIME).toString()
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
                    settings.setLong("cacheTime", UserPFP.REFRESH_CACHE_TIME)
                }
            }
        })
        val refreshCache = Button(view.context)
        refreshCache.text = "Redownload databases"
        refreshCache.setOnClickListener { button: View? ->
            Utils.threadPool.execute {
                Utils.showToast("Downloading databases...")
                context?.let { UserPFP.APFP.getCacheFile() }?.let { UserPFP.APFP.downloadDB(it) }
                Utils.showToast("Downloaded databases.")

                with(UserPFP.APFP) {
                    Utils.showToast("Downloading databases...")

                    Utils.showToast("Downloaded databases.")
                }
            }
        }
        addView(textInput)
        addView(refreshCache)
        addView(createCheckedSetting(
        	view.context,
        	"Enable debug logs (experimental)",
        	"debugEnabled",
        	false
         )
        )
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
        }
        return checkedSetting
    }
}
