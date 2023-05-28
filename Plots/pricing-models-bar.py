import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Pay-per-use
x = np.array([8, 20, 40, 80, 200, 300, 400, 600, 800, 1200])
y = np.array([3.62, 3.98, 6.86 , 13.34, 40.69, 81.05, 141.89, 301.35, 515.57, 1128.65])

# Subscription
x1 = np.array([8, 20, 40, 80, 200, 300, 400, 600, 800, 1200])
y1 = np.array([140, 140, 140, 140, 140, 140, 140, 140, 140, 140])

sns.set_style("dark")
sns.set_palette("tab10")

# Add grid pattern
plt.grid(color='white', linestyle='-.', linewidth=0.5)  

sns.barplot(x=x, y=y, color='blue', label='Pay-as-you-go')
sns.barplot(x=x1, y=y1, color='lightgreen', label='Subscription', alpha=0.7)

plt.xticks(fontsize=10)
plt.yticks(fontsize=10)

plt.ylim(0, 1200)
plt.xlabel("Number of Cloudlets", fontsize=12)
plt.ylabel("Total Price (Dollars)", fontsize=12)
plt.legend(loc='upper left', fontsize=12)

plt.show()