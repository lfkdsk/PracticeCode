cmake_minimum_required(VERSION 3.5)
project(ReflectFrame)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES main.cpp ReflectFrame/ReflectFactory.cpp ReflectFrame/ReflectFactory.h)
add_executable(ReflectFrame ${SOURCE_FILES})