package gdscript.polySymbols.scope

import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.polySymbols.query.PolySymbolQueryScopeContributor
import com.intellij.polySymbols.query.PolySymbolQueryScopeProviderRegistrar
import com.intellij.polySymbols.query.polySymbolScope
import com.intellij.psi.util.siblings
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.psi.GdAnnotationType
import gdscript.psi.GdFile
import gdscript.psi.GdGetMethodIdRef
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdSetMethodIdRef
import gdscript.psi.GdTypeHintRef
import gdscript.psi.utils.GdClassMemberUtil

class GdPolySymbolQueryScopeContributor : PolySymbolQueryScopeContributor {
    override fun registerProviders(registrar: PolySymbolQueryScopeProviderRegistrar) {
        registrar
            .inFile(GdFile::class.java)
            .apply {
                // get/set resolve
                forPsiLocations(
                    psiElement(GdGetMethodIdRef::class.java),
                    psiElement(GdSetMethodIdRef::class.java),
                )
                    .contributeScopeProvider { ref ->
                        listOf(
                            GdPsiOwnClassScope(ref),
                        )
                    }

                // inheritance resolve
                forPsiLocation(
                    psiElement(GdInheritanceIdRef::class.java)
                )
                    .contributeScopeProvider { ref ->
                        listOf(
                            gdSdkClassesPolySymbolScope(ref.project),
                            GdPsiClassesPolySymbolScope(ref.project, ref),
                            GdPsiResourceClassesPolySymbolScope(ref.project, ref),
                            //gdPsiAutoloadScope(ref.project),
                            polySymbolScope {
                                provides(GdPolySymbolKind.INHERITANCE_SYMBOLS)
                                initialize {
                                    addSymbol(GdPolySymbolKind.INHERITANCE_SYMBOLS, "GDScript Inheritance Symbols") {
                                        pattern {
                                            group {
                                                symbols {
                                                    from(GdPolySymbolKind.CLASS)
                                                    from(GdPolySymbolKind.RESOURCE_CLASS)
                                                }
                                                symbolReference()
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    }

                // type hint resolve
                forPsiLocation(
                    psiElement(GdTypeHintRef::class.java),
                )
                    .contributeScopeProvider { ref ->
                        // The GdTypeHintRef sit in a flat hierarchy where all qualifiers are siblings.
                        val qualifier = ref.siblings(false, false)
                            .filterIsInstance<GdTypeHintRef>()
                            .firstOrNull()
                        if (qualifier == null) {
                            listOf(
                                GdPsiOwnClassScope(ref),
                                gdSdkClassesPolySymbolScope(ref.project),
                                gdSdkGlobalPolySymbolScope(ref.project),
                                GdPsiClassesPolySymbolScope(ref.project, ref),
                                gdPsiAutoloadScope(ref.project),
                                polySymbolScope {
                                    provides(GdPolySymbolKind.TYPE_HINTS)
                                    initialize {
                                        addSymbol(GdPolySymbolKind.TYPE_HINTS, "GDScript Type Hints") {
                                            pattern {
                                                group {
                                                    symbols {
                                                        from(GdPolySymbolKind.CLASS)
                                                        from(GdPolySymbolKind.LOADED_CLASS_ALIAS)
                                                        from(GdPolySymbolKind.ENUM)
                                                        from(GdPolySymbolKind.AUTOLOAD)
                                                    }
                                                    symbolReference()
                                                }
                                            }
                                        }
                                    }
                                })
                        } else {
                            listOf(
                                GdQualifiedTypeHintResolveScope(qualifier),
                            )
                        }
                    }

                // class member / qualified symbol resolve
                forPsiLocation(
                    psiElement(GdRefIdRef::class.java),
                )
                    .contributeScopeProvider { ref ->
                        val qualifier = GdClassMemberUtil.calledUpon(ref)
                        if (qualifier == null) {
                            listOf(
                                GdPsiOwnClassScope(ref),
                                gdSdkClassesPolySymbolScope(ref.project),
                                gdSdkGlobalPolySymbolScope(ref.project),
                                GdPsiClassesPolySymbolScope(ref.project, ref),
                                gdPsiAutoloadScope(ref.project),
                                GdLocalSymbolsStructuredScope(ref),
                                polySymbolScope {
                                    provides(GdPolySymbolKind.QUALIFIABLE_SYMBOLS)
                                    initialize {
                                        addSymbol(GdPolySymbolKind.QUALIFIABLE_SYMBOLS, "GDScript Qualified Symbols") {
                                            pattern {
                                                group {
                                                    symbols {
                                                        from(GdPolySymbolKind.CLASS)
                                                        from(GdPolySymbolKind.METHOD)
                                                        from(GdPolySymbolKind.PROPERTY)
                                                        from(GdPolySymbolKind.CONSTANT)
                                                        from(GdPolySymbolKind.ENUM)
                                                        from(GdPolySymbolKind.SIGNAL)

                                                        // PSI-only
                                                        from(GdPolySymbolKind.LOADED_CLASS_ALIAS)
                                                        from(GdPolySymbolKind.AUTOLOAD)

                                                        // local, PSI-only (unqualified access only)
                                                        from(GdPolySymbolKind.LOCAL_VARIABLE)
                                                        from(GdPolySymbolKind.LOCAL_CONSTANT)
                                                        from(GdPolySymbolKind.PARAMETER)
                                                        from(GdPolySymbolKind.FOR_VARIABLE)
                                                        from(GdPolySymbolKind.BINDING_PATTERN)
                                                    }
                                                    symbolReference()
                                                }
                                            }
                                        }
                                    }
                                },
                            )

                        } else {
                            listOf(
                                GdQualifiedRefIdResolveScope(qualifier),
                            )
                        }
                    }

                forPsiLocation(psiElement(GdAnnotationType::class.java))
                    .contributeScopeProvider { ref ->
                        listOf(gdSdkAnnotationsPolySymbolScope(ref.project))
                    }
            }
    }
}
