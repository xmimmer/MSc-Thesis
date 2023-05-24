import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Data for the bar plots
x = np.array([8, 20, 60, 200, 400, 600, 800])
y = np.array([0.0130, 0.0178, 0.0836, 0.5838, 2.0936, 4.5809, 8.0047])

x1 = np.array([8, 20, 60, 200, 400, 600, 800])
y1 = np.array([0.0070, 0.0178, 0.0814, 0.3286, 1.2214, 2.5732, 4.4469])

sns.set_style("dark")
sns.set_palette("tab10")
plt.grid(color='white', linestyle='-.', linewidth=0.5)  # Add grid pattern
sns.barplot(x=x, y=y, color='blue')
sns.barplot(x=x1, y=y1, color='green')

# Add the horizontal line at value 3.25
plt.axhline(y=3.25, color='red', linestyle='--', label='High-end gaming PC running for 5 hours')

plt.xlabel("Number of Cloudlets", fontsize=12)
plt.ylabel("Energy Consumption (kWh)", fontsize=12)
#plt.title('Bedrock - Energy Consumption in kWh')

# Create a custom legend
legend_labels = ['Without Auto-scaling & Load-balancer', 'With Auto-scaling & Load-balancer']
legend_colors = ['blue', 'green']
legend_patches = [plt.Rectangle((0, 0), 1, 1, color=color) for color in legend_colors]
legend_patches.append(plt.Line2D([], [], color='red', linestyle='--'))
legend_labels.append('High-end gaming PC (650 Watts) running for 5 hours')

plt.xticks(fontsize=10)
plt.yticks(fontsize=10)

plt.legend(legend_patches, legend_labels, loc='upper left', fontsize=10)

plt.show()
