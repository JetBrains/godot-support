extends Node3D
#brief Represents a single manually placed probe for dynamic object lighting with [LightmapGI].
#desc [LightmapProbe] represents the position of a single manually placed probe for dynamic object lighting with [LightmapGI].
#desc Typically, [LightmapGI] probes are placed automatically by setting [member LightmapGI.generate_probes_subdiv] to a value other than [constant LightmapGI.GENERATE_PROBES_DISABLED]. By creating [LightmapProbe] nodes before baking lightmaps, you can add more probes in specific areas for greater detail, or disable automatic generation and rely only on manually placed probes instead.
class_name LightmapProbe





