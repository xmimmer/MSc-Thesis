import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Pay-per-use
x = np.array([20, 40, 120, 200, 300, 400])
y = np.array([5.02, 10.46, 28.14 , 56.94, 114.17, 200.91])

# Subscription
x1 = np.array([20, 40, 120, 200, 300, 400])
y1 = np.array([140, 140, 140, 140, 140, 140])

sns.set_style("dark")
sns.lineplot(x=x, y=y, label='Pay-per-use')
sns.lineplot(x=x1, y=y1, label='Subscription')

plt.xlabel("Number of Cloudlets")
plt.ylabel("Total Price (Dollars)")
plt.title('Private Deployment - Pricing Models without load-balancer')
plt.legend(loc='lower right')

plt.show()