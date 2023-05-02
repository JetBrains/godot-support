package gdscript.utils

object GdSdkUtil {

    val PREFIX = "GdSdk "
    val ZIP = ".7z"

    val BASE_URL = "https://gitlab.com/api/v4/projects/28295487/repository"
    val SDKs_URL = "${BASE_URL}/tree?path=sdk"

    fun String.versionToSdkName(): String {
        return "$PREFIX$this"
    }

    fun String.versionToSdkZip(): String {
        return "$PREFIX$this$ZIP"
    }

    fun String.versionToSdkUrl(): String {
        return "$BASE_URL/files/sdk%2F${PREFIX.replace(" ", "%20")}$this$ZIP/raw"
    }

    fun String.sdkZipToVersion(): String {
        return this.removePrefix(PREFIX).removeSuffix(ZIP)
    }

}
