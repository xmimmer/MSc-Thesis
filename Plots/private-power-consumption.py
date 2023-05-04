import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Without load-balancer
x = np.array([20, 60, 160, 320, 440, 640])
y = np.array([15237.25, 51419.1, 186933.27 , 612980.21, 1115544.76, 2546133.48])

# With load-balancer
x1 = np.array([20, 75, 280, 600, 940, 1200])
y1 = np.array([12112.41, 20419.59, 68169.79, 256734.84, 538478.41, 829405.79])

sns.set_style("dark")
sns.lineplot(x=x, y=y, label='Without load-balancer')
sns.lineplot(x=x1, y=y1, label='With load-balancer')

plt.xlabel("Number of Cloudlets")
plt.ylabel("Total Power Consumption (Watts)")
plt.title('Private Deployment - Power Consumption')
plt.legend(loc='lower right')

plt.show()