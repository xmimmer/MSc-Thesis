import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Pay-per-use
x = np.array([8, 20, 40, 80, 200, 300, 400, 600, 800])
y = np.array([3.62, 3.98, 6.86 , 13.34, 40.69, 81.05, 141.89, 301.35, 515.57])

# Subscription
x1 = np.array([8, 20, 40, 80, 200, 300, 400, 600, 800])
y1 = np.array([140, 140, 140, 140, 140, 140, 140, 140, 140])

sns.set_style("dark")
sns.lineplot(x=x, y=y, label='Pay-per-use')
sns.lineplot(x=x1, y=y1, label='Subscription')

plt.xlabel("Number of Cloudlets")
plt.ylabel("Total Price (Dollars)")
plt.title('Private Deployment - Pricing Models without load-balancer')
plt.legend(loc='lower right')

plt.show()