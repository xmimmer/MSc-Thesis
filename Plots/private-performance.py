import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Without load-balancer
x = np.array([8, 20, 60, 200, 400, 600, 800])
y = np.array([60.52, 65.52, 145.52 , 575.27, 1980.85, 4195.16, 7170.85])

# With load-balancer
x1 = np.array([8, 20, 60, 200, 400, 600, 800])
y1 = np.array([60.52, 65.52, 115.52, 195.52, 495.85, 925.85, 1515.85])

sns.set_style("dark")
sns.lineplot(x=x, y=y, label='Without load-balancer')
sns.lineplot(x=x1, y=y1, label='With load-balancer')

plt.xlabel("Number of Cloudlets")
plt.ylabel("Execution Time (Seconds)")
plt.title('Private Deployment - Execution Times')
plt.legend(loc='lower right')

plt.show()