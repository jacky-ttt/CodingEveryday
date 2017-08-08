#include <iostream>

#include <glm/vec3.hpp> // glm::vec3
#include <glm/vec4.hpp> // glm::vec4
#include <glm/mat4x4.hpp> // glm::mat4
#include <glm/gtc/matrix_transform.hpp> // glm::translate, glm::rotate, glm::scale, glm::perspective
#include <glm/ext.hpp>

glm::mat4 camera(float Translate, glm::vec2 const &Rotate) {
    glm::mat4 Projection = glm::perspective(glm::radians(45.0f), 4.0f / 3.0f, 0.1f, 100.f);
    glm::mat4 View = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, 0.0f, -Translate));
    View = glm::rotate(View, Rotate.y, glm::vec3(-1.0f, 0.0f, 0.0f));
    View = glm::rotate(View, Rotate.x, glm::vec3(0.0f, 1.0f, 0.0f));
    glm::mat4 Model = glm::scale(glm::mat4(1.0f), glm::vec3(0.5f));
    return Projection * View * Model;
}


int main() {
    std::cout << "Hello, World!" << std::endl;
    glm::tmat4x4<float> tesetMatrix
            (-0.0046, -0.9997, 0.0227, -2.0922,
             -0.9998, 0.0041, -0.0197, -6.3851,
             0.0196, -0.0228, -0.9995, -5.9063,
             0, 0, 0, 1);
    std::cout << glm::to_string(tesetMatrix) << std::endl;
    return 0;
}