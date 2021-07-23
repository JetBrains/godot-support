package gdscript.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.*
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdFileType
import gdscript.GdLanguage
import gdscript.index.Indices
import gdscript.index.stub.GdClassNamingStub
import gdscript.index.stub.GdClassNamingStubImpl
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile


object GdClassNamingElementType : IStubElementType<GdClassNamingStub, GdClassNaming>("classNaming", GdLanguage.INSTANCE) {

    fun getClassname(element: GdClassNamingImpl?): String {

        val project = element?.project;
        if (project != null) {
        val virtualFiles =
        FileTypeIndex.getFiles(GdFileType.INSTANCE, GlobalSearchScope.allScope(project));
        for (it in virtualFiles) {

            val gdf: GdFile? = PsiManager.getInstance(project).findFile(it) as GdFile?;
//            val properties = PsiTreeUtil.getChildrenOfType(gdf, GdClassNamingImpl::class.java);

//            for (pp in properties)  {
//                val ss = pp.greenStub;
//                val asd=  546;
//            }
            val properties = PsiTreeUtil.findChildOfType(gdf, GdClassNamingImpl::class.java);
            val ss = properties?.greenStub;
                val asd=  546;
        }
//            virtualFiles.forEach {
//                val gdf: GdFile? = PsiManager.getInstance(project).findFile(it) as GdFile?;
//                if (gdf != null) {
//                    val properties: Array<PsiElement> = PsiTreeUtil.getChildrenOfType(gdf, GdClassNamingImpl.class);
//                }
//            };

//            val stub = element?.getGreenStub();
//            if (stub != null) {
//                return stub.name();
//            }
        }

        return element?.classNameNm?.name.toString();
    }

    @JvmStatic
    fun getInstance(debugName: String): GdClassNamingElementType {
        return GdClassNamingElementType
    }

    override fun getExternalId(): String = "GdScript.classNaming"

    override fun serialize(stub: GdClassNamingStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name());
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): GdClassNamingStub =
        GdClassNamingStubImpl(parentStub, dataStream.readName()?.string);

    override fun indexStub(stub: GdClassNamingStub, sink: IndexSink) {
        sink.occurrence(Indices.CLASS_NAMING_INDEX, stub.name())
    }

    override fun createPsi(stub: GdClassNamingStub): GdClassNaming =
            GdClassNamingImpl(stub, stub.stubType)

    override fun createStub(psi: GdClassNaming, parentStub: StubElement<*>?): GdClassNamingStub {
        return GdClassNamingStubImpl(parentStub, psi.classNameNm?.name)
    }

}
