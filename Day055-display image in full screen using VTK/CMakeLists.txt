cmake_minimum_required(VERSION 2.8)

PROJECT(FullScreenImageVTK)


SET(VTK_DIR $ENV{PROGRAMFILES}/VTK/bin)
find_package(VTK REQUIRED)
include(${VTK_USE_FILE})


file(GLOB SRC "*.cpp" "*.h")

add_executable(FullScreenImageVTK ${SRC})

target_link_libraries(FullScreenImageVTK ${VTK_LIBRARIES})
