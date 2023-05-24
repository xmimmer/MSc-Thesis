import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Pay-per-use
x = np.array([8, 20, 40, 80, 200, 300, 400, 600, 800, 1200])
y = np.array([1.45, 3.98, 7.97, 17.68, 30.46, 46.33, 68.01, 124.25, 203.73, 426.11])

# Subscription
x1 = np.array([8, 20, 40, 80, 200, 300, 400, 600, 800, 1200])
y1 = np.array([170, 170, 170, 170, 170, 170, 170, 170, 170, 170])

sns.set_style("dark")
sns.set_palette("tab10")
plt.grid(color='white', linestyle='-.', linewidth=0.5)  # Add grid pattern

sns.barplot(x=x, y=y, color='blue', label='Pay-as-you-go')
sns.barplot(x=x1, y=y1, color='lightgreen', label='Subscription', alpha=0.7)

plt.xticks(fontsize=10)
plt.yticks(fontsize=10)

plt.ylim(0, 450)
plt.xlabel("Number of Cloudlets", fontsize=12)
plt.ylabel("Total Price (Dollars)", fontsize=12)
plt.legend(loc='upper left', fontsize=12)

plt.show()