import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Without load-balancer
x = np.array([20, 40, 100, 160, 200, 240, 280, 400, 800, 1200, 1600, 2400])
y = np.array([9.51, 22.61, 44.24, 47.34, 48.27, 54.40,61.02 , 72.67, 88.24, 89.94, 90.29, 90.58])

# With load-balancer
x1 = np.array([20, 40, 100, 160, 200, 240, 280, 400, 800, 1200, 1600, 2400])
y1 = np.array([9.51, 22.61, 44.24, 47.34, 48.27, 54.40, 61.04, 73.97, 94.17, 96.71, 97.92, 98.91])

sns.set_style("dark")
sns.lineplot(x=x, y=y, label='Without load-balancer')
sns.lineplot(x=x1, y=y1, label='With load-balancer')

plt.ylim(0, 100)
plt.xlabel("Number of Cloudlets")
plt.ylabel("CPU Utilization %")
plt.title('Private Deployment - CPU Utilization')
plt.legend(loc='lower right')

plt.show()