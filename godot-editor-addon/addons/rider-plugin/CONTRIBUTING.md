# Contribution

Hello, thanks for taking time and helping out with the addon!

Most contributions require you to agree to a Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us the rights to use your contribution. Please sign the CLA before sending the PR: https://www.jetbrains.com/agreements/cla/.

#### Local setup

Open the `godot-editor-addon/CMakeLists.txt` in Rider 2026.1+, select `rider-gdextension` target in the run configuration selector.
There are some more fixes coming in 2026.2, which will allow working on gdscript and cpp files within the same workspace.

#### Signing the binaries

Plugin binaries need to be signed to comply with modern operating system security requirements. Unsigned dynamic libraries may fail to load or trigger security warnings:

- **macOS**: Requires code signing (and often notarization) for `.dylib` files. Unsigned libraries may simply fail to load.
- **Windows**: SmartScreen may block unsigned binaries, and corporate environments often enforce signed code only.
- **Linux**: Generally allows unsigned `.so` files, but some sandboxed environments (Flatpak, Snap) impose restrictions.

**How signing is implemented:**

1. **TeamCity Configuration**: [ijplatform_master_Net_Deploy_Plugins_Public_Godot](https://buildserver.labs.intellij.net/buildConfiguration/ijplatform_master_Net_Deploy_Plugins_Public_Godot) (internal JetBrains link)
   - Note: The automatic trigger on new tags is currently not working, so manual triggering is required.

2. **Release Process**:
   - First, use your GitHub Action to prepare a **pre-release** with a tag
   - Manually trigger the TeamCity configuration on the necessary branch/tag
   - The configuration will download assets, sign them, and upload them back (removing the pre-release flag)
   - The pre-release flag is required at the start; otherwise, the signing configuration will skip the release (protection against double signing)

3. **Signing existing releases**:
   - You can run signing on already existing old releases/tags
   - Mark them as pre-release first, then run the configuration on the necessary tag
