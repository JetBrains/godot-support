package tscn.toolWindow.model

import tscn.psi.TscnNodeHeader

data class TscnSceneTreeNodeMissing(
    val node: TscnNodeHeader,
    val basePath: String,
    val externalType: String?,
    val index: Int,
    val paths: List<String>,
)
