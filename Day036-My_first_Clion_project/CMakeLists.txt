cmake_minimum_required(VERSION 3.8)
project(Day036_My_first_Clion_project)

set(CMAKE_CXX_STANDARD 11)

# Add module path
list(APPEND CMAKE_MODULE_PATH "${CMAKE_CURRENT_SOURCE_DIR}/cmake")

# GLM
find_package(GLM REQUIRED)
include_directories(${GLM_INCLUDE_DIRS})

set(SOURCE_FILES main.cpp)
add_executable(Day036_My_first_Clion_project ${SOURCE_FILES})