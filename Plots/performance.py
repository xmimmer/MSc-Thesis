import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Without load-balancer
x = np.array([8, 20, 60, 200, 400, 600, 800])
y = np.array([60.52, 65.52, 145.52, 575.27, 1980.85, 4195.16, 7170.85])

# With load-balancer
x1 = np.array([8, 20, 60, 200, 400, 600, 800])
y1 = np.array([60.52, 65.52, 115.52, 195.52, 495.85, 925.85, 1515.85])

sns.set_style("dark")
sns.set_palette("tab10")

# Add grid pattern
plt.grid(color='white', linestyle='-.', linewidth=0.5)  

sns.lineplot(x=x, y=y, label='Without Auto-scaling & Load-balancer', linewidth=2.5)
sns.lineplot(x=x1, y=y1, label='With Auto-scaling & Load-balancer', linewidth=2.5)

# Fill the area between the two lines with grey color
plt.fill_between(x, y, y1, color='gray', alpha=0.3)

plt.xlabel("Number of Cloudlets", fontsize=12)
plt.ylabel("Execution Time (Seconds)", fontsize=12)
plt.legend(loc='upper left', fontsize=12)

plt.xticks(fontsize=10)
plt.yticks(fontsize=10)

plt.tight_layout()
plt.show()
