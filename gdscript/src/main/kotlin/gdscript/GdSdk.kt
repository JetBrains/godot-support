package gdscript

import com.intellij.openapi.projectRoots.*
import gdscript.competion.staticLoader.StaticClassLoader
import org.jdom.Element

class GdSdk : SdkType("GdScript Sdk") {

    override fun saveAdditionalData(additionalData: SdkAdditionalData, additional: Element) {
    }

    override fun suggestHomePath(): String? {
        val folder = "_classes_test"
        val loader = StaticClassLoader::class.java.classLoader
        val directory = loader.getResource(folder).path

        return directory.substring(6, directory.length - (folder.length + 2))
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
