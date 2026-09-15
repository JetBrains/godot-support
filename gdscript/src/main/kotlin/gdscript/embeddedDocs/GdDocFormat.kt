package gdscript.embeddedDocs

/**
 * Identifies the byte format of the core documentation that the plugin writes.
 *
 * Raise [VERSION] when a change alters the file set or the file bytes that [GdCoreDocPipeline] writes.
 * The [GdEmbeddedDocExtractor], the [GdDocXmlSplitter] and the [GdCoreDocPipeline] produce those bytes.
 * The XML safety code decides whether the splitter accepts a document, so it can change the file set too.
 * The manifest parser selects the macOS bundle executable, so it can change which binary the extraction reads. The merger is the one
 * part that never runs on the core write path.
 *
 * A raised number invalidates every core stamp, so the plugin writes the documentation again.
 * `GdDocFormatGoldenTest` holds the hash of the written bytes for each version, and it fails until the number moves.
 */
object GdDocFormat {
    const val VERSION: Int = 1
}
