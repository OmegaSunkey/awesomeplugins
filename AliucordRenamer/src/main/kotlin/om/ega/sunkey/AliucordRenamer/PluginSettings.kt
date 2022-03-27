package om.ega.sunkey.AliucordRenamer

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
		setActionBarTitle("AliucordRenamer")
                val texto = TextInput(view.context)
		texto.editText.setText(
			settings.getString("name", "set a name in settings")
		)
		texto.editText.inputType = InputType.TYPE_CLASS_TEXT
		texto.editText.addTextChangedListener(object : TextWatcher() {
			override fun afterTextChanged(editable: Editable) {
				try {
				        settings.setString("name",
					java.lang.String.valueOf(editable)
				)

				} catch (e: Exception) {
					settings.setString("name", sonido)
				}
			}
		})
		addView(texto)
	}
}
