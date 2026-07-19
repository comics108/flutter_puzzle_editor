# The Flutter tooling requires that developers have CMake 3.10 or later
# installed. You should not increase this version, as doing so will cause
# the plugin to fail to compile for some customers of the plugin.
cmake_minimum_required(VERSION 3.10)

project(editor_library VERSION 0.0.1 LANGUAGES C)

add_library(editor SHARED
  "editor.c"
)

set_target_properties(editor PROPERTIES
  PUBLIC_HEADER editor.h
  OUTPUT_NAME "editor"
)

target_compile_definitions(editor PUBLIC DART_SHARED_LIB)

if (ANDROID)
  # Support Android 15 16k page size
  target_link_options(editor PRIVATE "-Wl,-z,max-page-size=16384")
endif()
