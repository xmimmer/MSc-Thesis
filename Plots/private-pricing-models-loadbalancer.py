import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Pay-per-use
x = np.array([20, 300, 640, 1000, 1300, 1500])
y = np.array([5.02, 12.56, 35.83 , 78.55, 128.39, 168.45])

# Subscription
x1 = np.array([20, 300, 640, 1000, 1300, 1500])
y1 = np.array([150, 150, 150, 150, 150, 150])

sns.set_style("dark")
sns.lineplot(x=x, y=y, label='Pay-per-use')
sns.lineplot(x=x1, y=y1, label='Subscription')

plt.xlabel("Number of Cloudlets")
plt.ylabel("Total Price (Dollars)")
plt.title('Private Deployment - Pricing Models with load-balancer')
plt.legend(loc='lower right')

plt.show()