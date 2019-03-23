import numpy as np
import matplotlib.pyplot as plt


# Y = 4x –2x^4 + zx -9zx^2.
def polynomial_function(x_, z_):
    return 4 * x_ - 2 * x_ ** 4 + z_ * x_ - 9 * z_ * x_ ** 2


x = np.linspace(-10, 10, 30)
z = np.linspace(-10, 10, 30)
X, Z = np.meshgrid(x, z)
Y = polynomial_function(X, Z)
fig = plt.figure()
ax = plt.axes(projection='3d')
ax.plot_wireframe(X, Y, Z, rstride=1, cstride=1)
ax.set_xlabel('x')
ax.set_ylabel('y')
ax.set_zlabel('z')
plt.show()
