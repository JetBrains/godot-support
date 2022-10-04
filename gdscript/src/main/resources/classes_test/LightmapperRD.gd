#brief The built-in GPU-based lightmapper for use with [LightmapGI].
#desc LightmapperRD ("RD" stands for [RenderingDevice]) is the built-in GPU-based lightmapper for use with [LightmapGI]. On most dedicated GPUs, it can bake lightmaps much faster than most CPU-based lightmappers. LightmapperRD uses compute shaders to bake lightmaps, so it does not require CUDA or OpenCL libraries to be installed to be usable.
#desc [b]Note:[/b] Only usable when using the Vulkan backend (Clustered or Mobile), not OpenGL.
class_name LightmapperRD





