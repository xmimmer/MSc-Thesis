import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Without load-balancer
x = np.array([8, 20, 60, 200, 400, 600, 800])
y = np.array([47124, 64251, 301132, 2101757, 7537060, 16491575, 28817240])

# With load-balancer
x1 = np.array([8, 20, 60, 200, 400, 600, 800])
y1 = np.array([25337, 64251, 293164, 1182974, 4397304, 9263742, 16008984])

sns.set_style("dark")
sns.set_palette("tab10")
plt.grid(color='white', linestyle='-.', linewidth=0.5)  # Add grid pattern

plt.plot(x, y, label='Without Auto-scaling & Load-balancer', color='tab:blue', linewidth=2.5)
plt.plot(x1, y1, label='With Auto-scaling & Load-balancer', color='tab:orange', linewidth=2.5)

# Fill the area between the two lines with grey color
plt.fill_between(x, y, y1, color='gray', alpha=0.3)

plt.xlabel("Number of Cloudlets", fontsize=12)
plt.ylabel("Energy Consumption (Joules)", fontsize=12)
plt.legend(loc='upper left', fontsize=12)
plt.xticks(fontsize=10)
plt.yticks(fontsize=10)

plt.show()


