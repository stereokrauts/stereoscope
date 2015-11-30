- Tycho does not need a target platform definition, it will decide itself which
  bundles to include according to the product file.
- Tycho uses the first *.product file it finds in this folder. So it should better
  be the one suitable for Tycho, that means it MUST NOT contain any platform
  dependencies (*win32*, *macos* etc.)
