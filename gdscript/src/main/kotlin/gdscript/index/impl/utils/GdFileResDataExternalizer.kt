package gdscript.index.impl.utils

import com.intellij.util.io.DataExternalizer
import java.io.DataInput
import java.io.DataOutput

object GdFileResDataExternalizer : DataExternalizer<String> {

    override fun save(out: DataOutput, value: String) {
        out.writeBytes(value)
    }

    override fun read(`in`: DataInput): String {
        return `in`.readLine()
    }

}
