package om.ega.sunkey.userbg

import android.content.Context
import com.aliucord.Utils.createCheckedSetting
import com.aliucord.api.SettingsAPI
import com.aliucord.views.TextInput
import android.text.Editable
import android.text.InputType
import android.view.View
import android.text.util.Linkify
import android.widget.TextView
import com.discord.app.AppFragment
import com.discord.views.CheckedSetting
import com.aliucord.PluginManager
import com.aliucord.Utils
import com.aliucord.utils.DimenUtils
import com.aliucord.fragments.SettingsPage
import com.aliucord.views.Button
import com.aliucord.views.Divider
import com.discord.utilities.view.text.TextWatcher
import com.discord.utilities.color.ColorCompat
import com.lytefast.flexinput.R
import java.lang.Exception

class PluginSettings(private val settings: SettingsAPI) : SettingsPage() {
    override fun onViewBound(view: View) {
        super.onViewBound(view)
        setActionBarTitle("UserBG")
	val p = DimenUtils.defaultPadding
        val textInput = TextInput(view.context, "Refresh UserBG db time (min) / Actualizar la base de datos de UserBG (min)") //texthint finally 
        textInput.editText.setText(
            settings.getLong("cacheTime", UserBG.REFRESH_CACHE_TIME).toString()
        )
        textInput.editText.inputType = InputType.TYPE_CLASS_NUMBER
        textInput.editText.addTextChangedListener(object : TextWatcher() {
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
        refreshCache.setOnClickListener { Button: View? ->
            Utils.threadPool.execute {
                Utils.showToast("Downloading databases...")
                context?.let { UserBG.USRBG.getCacheFile(it) }?.let { UserBG.USRBG.downloadDB(it) }
                Utils.showToast("Downloaded databases.")
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
        addView(refreshCache) 

	val server = TextView(view?.context).apply {
		linksClickable = true
		text = "Server de soporte en español: https://discord.gg/NfkPvxvmuz"
		setTextColor(ColorCompat.getThemedColor(view?.context, R.b.colorOnPrimary))
	}
	Linkify.addLinks(server, Linkify.WEB_URLS)

	val divider = Divider(view?.context).apply {
		setPadding(p, p, p, p)
	}

	addView(divider) //thanks scruz 
	addView(server)

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
