import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Pay-per-use
x = np.array([8, 20, 40, 80, 200, 300, 400, 600, 800, 1600])
y = np.array([1.45, 3.98, 7.97 , 17.68, 30.46, 46.33,68.01, 124.25, 203.73, 426.11])

# Subscription
x1 = np.array([8, 20, 40, 80, 200, 300, 400, 600, 800, 1600])
y1 = np.array([170, 170, 170, 170, 170, 170, 170, 170, 170, 170])

sns.set_style("dark")
sns.lineplot(x=x, y=y, label='Pay-per-use')
sns.lineplot(x=x1, y=y1, label='Subscription')

plt.xlabel("Number of Cloudlets")
plt.ylabel("Total Price (Dollars)")
plt.title('Private Deployment - Pricing Models with load-balancer')
plt.legend(loc='lower right')

plt.show()