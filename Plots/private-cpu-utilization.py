import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Without load-balancer
x = np.array([20, 60, 160, 320, 440, 640])
y = np.array([86.95, 205.95, 585.71 , 1815.56, 3370.16, 7515])

# With load-balancer
x1 = np.array([20, 75, 280, 600, 940, 1200])
y1 = np.array([65.52, 80.85, 175.50, 545.59, 1155.36, 1775.81])

sns.set_style("dark")
sns.lineplot(x=x, y=y, label='With load-balancer')
sns.lineplot(x=x1, y=y1, label='Without load-balancer')

plt.xlabel("Number of Cloudlets")
plt.ylabel("Execution Time (Seconds)")
plt.title('Private Deployment - Execution Times')
plt.legend(loc='lower right')

plt.show()