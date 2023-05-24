import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Without load-balancer
x = np.array([8, 20, 40, 60, 80, 100, 120, 180, 240, 300, 400, 800, 1600])
y = np.array([4.46, 9.51, 25.25, 36.73, 44.84, 58.87, 66.32, 72.55, 78.62, 80.49, 80.12, 85.46, 89.07])

# With load-balancer
x1 = np.array([8, 20, 40, 60, 80, 100, 120, 180, 240, 300, 400, 800, 1600])
y1 = np.array([11.16, 9.51, 10.56, 10.37, 17.24, 23.32, 27.54, 40.69, 57.14, 67.54, 73.68, 90.61, 97.38])

sns.set_style("dark")
sns.set_palette("tab10")

# Create the figure and axes
fig, ax = plt.subplots(figsize=(12, 6))

# Add a grid pattern
ax.grid(color='white', linestyle='-.', linewidth=0.5)

# Plot the lines
sns.lineplot(x=x, y=y, label='Without Auto-scaling & Load-balancer', ax=ax, linewidth=1.5)
sns.lineplot(x=x1, y=y1, label='With Auto-scaling & Load-balancer', ax=ax, linewidth=1.5)

# Set the y-axis limit
plt.ylim(0, 100)

plt.xlabel("Number of Cloudlets", fontsize=10)
plt.ylabel("CPU Utilization %", fontsize=10)
#plt.title('Bedrock - CPU Utilization')

# Add data points on the line when CPU util > 15% 
for i in range(len(x)):
    if y[i] > 15:
        plt.text(x[i], y[i]+1, str(y[i]), ha='center', color='black', fontsize=8)
    if y1[i] > 15:
        plt.text(x1[i], y1[i]+1, str(y1[i]), ha='center', color='black', fontsize=8)

# Add dots
plt.scatter(x[y > 15], y[y > 15], marker='o', color='red', s=50)
plt.scatter(x1[y1 > 15], y1[y1 > 15], marker='o', color='blue', s=50)

plt.legend(loc='lower right', fontsize=12)  # Increase the fontsize parameter

plt.show()
