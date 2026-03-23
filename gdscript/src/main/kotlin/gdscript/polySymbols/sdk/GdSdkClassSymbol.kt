package gdscript.polySymbols.sdk

import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolModifier
import com.intellij.polySymbols.PolySymbolQualifiedName
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItem
import com.intellij.polySymbols.query.PolySymbolCodeCompletionQueryParams
import com.intellij.polySymbols.query.PolySymbolQueryStack
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.utils.PolySymbolScopeWithCache
import gdscript.GdIcon
import gdscript.library.GdDocClassesFoldersService
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolNamespace
import gdscript.polySymbols.completion.GdPolySymbolPriorities
import gdscript.polySymbols.completion.gdCodeCompletions
import gdscript.polySymbols.resolve.GdSymbolClassHierarchyUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.sdk.xml.GdSdkData
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdNamedElement
import javax.swing.Icon

class GdSdkClassSymbol(override val project: Project, private val sourceFile: VirtualFile) :
    GdSdkPolySymbol(), GdClassSymbol {

    override val kind: PolySymbolKind get() = GdPolySymbolKind.CLASS
    override val name: String get() = declaringClassName
    override val declaringClassName: String get() = data?.name ?: sourceFile.nameWithoutExtension
    override val declaringClassId: String get() = declaringClassName
    override val classId: String get() = declaringClassName
    override val returnType: String get() = classId

    override val data: GdSdkData.ClassData?
        get() = GdSdkParseCache.getInstance(project).getOrParseClassData(sourceFile)

    private val scopeDelegate get() = object : PolySymbolScopeWithCache<Project, String>(project, project, classId) {

        override fun initialize(consumer: (PolySymbol) -> Unit, cacheDependencies: MutableSet<Any>) {
            val classData = data
            classData?.constructors?.forEach { cDoc ->
                consumer(GdSdkConstructorSymbol(project, declaringClassName, cDoc))
            }
            classData?.methods?.forEach { mDoc ->
                consumer(GdSdkMethodSymbol(project, declaringClassName, mDoc))
            }
            classData?.properties?.forEach { pDoc ->
                consumer(GdSdkPropertySymbol(project, declaringClassName, pDoc))
            }
            classData?.constants?.forEach { cDoc ->
                consumer(GdSdkConstantSymbol(project, declaringClassName, cDoc))
            }
            classData?.enums?.forEach { eDoc ->
                consumer(GdSdkEnumSymbol(project, declaringClassName, eDoc))
            }
            classData?.signals?.forEach { sDoc ->
                consumer(GdSdkSignalSymbol(project, declaringClassName, sDoc))
            }

            // Single shared tracker for all SDK class caches. We avoid using sourceFile
            // directly as a dependency because the platform hard-references items in
            // cacheDependencies for the cache holder's lifetime (Project), which would
            // pin deleted XML VirtualFiles in memory.
            // We can't use sourceFile as the data holder because platform prevents it;
            // it says it leads to memory leaks.
            cacheDependencies.add(GdDocClassesFoldersService.getInstance(project).modificationTracker)
        }

        override fun provides(kind: PolySymbolKind): Boolean =
            kind.namespace == GdPolySymbolNamespace.NAMESPACE &&
                kind in setOf(
                GdPolySymbolKind.CONSTRUCTOR,
                GdPolySymbolKind.METHOD,
                GdPolySymbolKind.PROPERTY,
                GdPolySymbolKind.CONSTANT,
                GdPolySymbolKind.SIGNAL,
                GdPolySymbolKind.ENUM,
            )

        override fun createPointer(): Pointer<out PolySymbolScopeWithCache<Project, String>> {
            val ownerPtr = this@GdSdkClassSymbol.createPointer()
            return Pointer {
                val owner = ownerPtr.dereference() ?: return@Pointer null

                owner.directMemberScope
            }

        }

        override fun getCodeCompletions(
            qualifiedName: PolySymbolQualifiedName,
            params: PolySymbolCodeCompletionQueryParams,
            stack: PolySymbolQueryStack,
        ): List<PolySymbolCodeCompletionItem> = gdCodeCompletions(qualifiedName, params, stack)
    }

    override val icon: Icon get() = GdIcon.getEditorIcon(classId)
    override val priority: PolySymbol.Priority get() = GdPolySymbolPriorities.BUILT_IN

    override val directMemberScope: PolySymbolScopeWithCache<Project, String> get() = scopeDelegate

    private val superClassName: String?
        get() = data?.inherits?.takeIf { it.isNotBlank() }

    override fun resolveSuperClassSymbol(): GdClassSymbol? =
        superClassName?.let { GdSymbolResolverUtil.resolveCanonicalClassSymbol(project, it) }

    override fun inheritedQueryScopes(): List<PolySymbolScope> =
        GdSymbolClassHierarchyUtil.collectInheritedScopes(this, linkedSetOf(declaringClassName))

    override val queryScope: List<PolySymbolScope>
        get() = buildList {
            add(directMemberScope)
            addAll(inheritedQueryScopes())
        }

    override fun createPointer(): Pointer<out GdSdkClassSymbol> {
        val projectRef = project
        val fileUrl = sourceFile.url
        return Pointer {
            if (projectRef.isDisposed) return@Pointer null
            val vf = VirtualFileManager.getInstance().findFileByUrl(fileUrl) ?: return@Pointer null
            if (!vf.isValid) return@Pointer null
            GdSdkClassSymbol(projectRef, vf)
        }
    }


    override val modifiers: Set<PolySymbolModifier>
        get() = setOf(
            PolySymbolModifier.STATIC
        )

    override fun psiElementRepresentsSdkSymbol(element: GdNamedElement): Boolean {
        return super.psiElementRepresentsSdkSymbol(element) && element is GdClassNameNmi
    }
}