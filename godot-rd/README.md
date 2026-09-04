# Godot-RD
This folder contains a GDExtension that serves as a communication bridge between Godot and Rider over RD protocol.

## Build
Building is done with `bazel`, where the target `//dotnet/Plugins/godot-support/godot-rd:compile_all` produces _unsigned_ libraries, for the layout see Artifact section. Signing and placing them into the Rider installation belongs in the Rider installer build.

The build itself cross-compiles for every OS x CPU arch with `clang`. The dependencies (`libc`, `MSVC CRT`, `Apple SDK`...) being pulled in by Bazel instead of using the ones on host. This means that the host OS doesn't matter. For example, it can be just a plain Linux box.

### Artifact
`compile_all` produces the following structure inside the Bazel output tree. It is laid out in a way where after the libraries are signed, the whole folder is dropped as-is into `<RiderInstall>/plugins/rider-godot/`. 
- `godot-rd/`
    - `godot-rd.gdextension` 
    - `linux/`
        - `godot-rd.linux.arm64.so`
        - `godot-rd.linux.x86_64.so`
    - `windows/`
        - `godot-rd.windows.arm64.dll`
        - `godot-rd.windows.x86_64.dll`
    - `macos/`
        - `godot-rd.macos.arm64.dylib`
        - `godot-rd.macos.x86_64.dylib`

### Build Targets
There exist more targets apart from `compile_all`. All targets live in the `//dotnet/Plugins/godot-support/godot-rd` package. Here are the relevant ones:

| Target        | Description                                                                                                                                                                                              |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `compile_all` | Cross-compile every CPU x OS combination. Artifacts are in bazel folder in `godot-rd/`, where it mirrors the final artifact as explained above.                                                          |
| `compile_host` | Same as `compile_all` but only for current OS + CPU. Artifacts are in bazel folder in `host/godot-rd/`. Useful for copying to your rider installation for testing with `godot-support/godot-editor-addon`. |
| `dev`          | Needs to be ran as `./bazel.cmd run` instead of `build`. Builds for current OS + CPU and places the artifact into project folder for testing while development.                                          |


## Compatibility
> GDExtension is a Godot-specific technology that lets the engine interact with native shared libraries at runtime. You can use it to run native code without compiling it with the engine.
 
From: https://docs.godotengine.org/en/latest/engine_details/engine_api/gdextension/what_is_gdextension.html 

The GDExtension system makes sure it works regardless of the compiler used for the engine. For example, if the engine is compiled with `MinGW` and extension compiled with `MSVC`.


The following compile time checks have been added to validate against Rider minimum requirements.

| Platform | Contract                                                             | Enforced by                                                                                   |
|---|---|---|
| `linux-x86_64`, `linux-arm64` | Uses no glibc API newer than 2.29                                    | Post-2.29 API fails the compile and every versioned symbol resolves to at most `GLIBC_2.29`   |
| `macos-x86_64`, `macos-arm64` | deployment target macOS 13.0                                         | `--macos_minimum_os=13.0`, so a post-13 API fails the compile                                 |
| `windows-x86_64`, `windows-arm64` | Do not rely on C++ redistributable from the OS.                      | Compile MSVC CRT (`/MT`) statically.                                                          |
| `windows-x86_64`, `windows-arm64` | Fail compilation, if symbols released past Windows 10 1809 are used. | Set `_WIN32_WINNT`/`WINVER`/`NTDDI_VERSION` to align with Windows 10 1809.|

For the exact flags you can check `godot_rd_platforms.bzl`.

## Generated build metadata
`godot_cpp_generated_files.bzl` and `project/addons/godot-rd/bin/godot-rd.gdextension` are generated from
`godot_version.bzl` and the pinned godot-cpp, but they are committed because Bazel needs them at loading time.

After changing `godot_version.bzl` or the `godot_cpp` archive pin, **regenerate them with**
`python3 godot_cpp_gen_main.py --regenerate-metadata` (requires going through the [Local setup](#local-setup)).

`./bazel.cmd test //dotnet/Plugins/godot-support/godot-rd:metadata_test` verifies that the committed files
match the generator output and prints a diff when they do not.

## Local setup
CMake only serves as a way to get IDE support for development. It shouldn't be used for building. To get the local setup working:
1. Open the project as Cmake with the root `CMakeLists.txt` file.
2. Run the target `generate-bindings`.
3. The symbols inside the project should now be resolved.
### Testing
For local testing this setup is currently recommended:
1. Run `./bazel.cmd run //dotnet/Plugins/godot-support/godot-rd:dev`
2. Run `Rider` configuration from IDEA.
3. Open the `godot-rd/project` folder with Rider.
4. Open the same folder inside Godot.

Step 1 builds the library for the machine you are on, and writes it into `project/addons/godot-rd/bin/<os>/` next to the committed
`godot-rd.gdextension`.

If you open the `godot-rd/project/` folder with both Rider and Godot, you should see `Open in Godot`, `Open in Rider` in Rider when right-clicking on files inside the Rider Project tab, respectively Godot FileSystem dock.

### Cross-compilation on Windows
If you want to crosscompile (you want to run `compile_all`) on Windows you must create a file `.bazelrc-user.bazelrc` in the root, with the contents of `startup --windows_enable_symlinks`. After adding this make sure to run `./bazel.cmd shutdown`.