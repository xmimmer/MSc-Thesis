import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Without load-balancer
x = np.array([8, 20, 40, 60, 80, 100, 120, 180, 240, 300, 400, 800, 1600])
y = np.array([4.46, 9.51, 25.25, 36.73, 44.84, 58.87,66.32 ,72.55, 78.62, 80.49, 80.12, 85.46, 89.07])

# With load-balancerdet 
x1 = np.array([8, 20, 40, 60, 80, 100, 120, 180, 240, 300, 400, 800, 1600])
y1 = np.array([11.16, 9.51, 10.56, 10.37, 17.24, 23.32, 27.54, 40.69, 57.14, 67.54, 73.68, 90.61, 97.38])

sns.set_style("dark")
sns.lineplot(x=x, y=y, label='Without load-balancer')
sns.lineplot(x=x1, y=y1, label='With load-balancer')

plt.ylim(0, 100)
plt.xlabel("Number of Cloudlets")
plt.ylabel("CPU Utilization %")
plt.title('Private Deployment - CPU Utilization')
plt.legend(loc='lower right')

plt.show()