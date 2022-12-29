package gdscript.sdk

import com.intellij.openapi.projectRoots.*
import org.jdom.Element

@Deprecated("lib")
object GdSdkType : SdkType("GdScript Sdk") {

    override fun saveAdditionalData(additionalData: SdkAdditionalData, additional: Element) {
    }

    override fun suggestHomePath(): String? {
        return null;
    }

    override fun isValidSdkHome(path: String): Boolean {
        return true;
    }

    override fun suggestSdkName(currentSdkName: String?, sdkHome: String): String {
        return "GdScript Sdk"
    }

    /**
     * @return Configurable object for the SDKs additional data or null if not applicable
     */
    override fun createAdditionalDataConfigurable(
        sdkModel: SdkModel,
        sdkModificator: SdkModificator,
    ): AdditionalDataConfigurable? {
        return null;
    }

    override fun getPresentableName(): String {
        return "GdScript Sdk";
    }

}
