package om.ega.sunkey.StartupSound2

import android.content.Context
import com.aliucord.Utils.createCheckedSetting
import com.aliucord.api.SettingsAPI
import com.aliucord.fragments.SettingsPage
import com.aliucord.views.TextInput
import android.text.Editable
import android.text.InputType
import android.view.View
import com.discord.app.AppFragment
import com.discord.utilities.view.text.TextWatcher

class PluginSettings (private val settings: SettingsAPI) : SettingsPage() {
	override fun onViewBound(view: View) {
		super.onViewBound(view)
		setActionBarTitle("StartupSound")
                val texto = TextInput(view.context)
		texto.editText.setText(
			settings.getString("cacheTime", StartupSound2.sonido)
		)
		texto.editText.inputType = InputType.TYPE_CLASS_STRING
		texto.editText.addTextChangedListener(object : TextWatcher() {
			override fun afterTextChanged(editable: Editable) {
				try {
					if (java.lang.String.valueOf(editable) != null) settings.setString("cacheTime",
					java.lang.String.valueOf(editable)
				)

				} catch (e: Exception) {
					settings.setString("cacheTime", StartupSound2.sonido)
				}
			}
		})
	}
}
