import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

# Without load-balancer
#x = np.array([8, 20, 60, 200, 400, 600, 800])
#y = np.array([47124, 64251, 301132 , 2101757 , 7537060,16491575, 28817240])

# With load-balancer
#x1 = np.array([8, 20, 60, 200, 400, 600, 800])
#y1 = np.array([25337, 64251, 293164, 1182974, 4397304, 9263742,16008984])

# Without load-balancer
x = np.array([8, 20, 60, 200, 400, 600, 800])
y = np.array([0.7922, 1.1693, 12.1724 ,335.8550, 4147.1628, 19219.3738, 57401.1852])

# With load-balancer
x1 = np.array([8, 20, 60, 200, 400, 600, 800])
y1 = np.array([0.4259, 1.1693, 9.4073, 64.2486, 605.6700, 2382.4596 ,6740.8942 ])

sns.set_style("dark")
sns.lineplot(x=x, y=y, label='Without load-balancer')
sns.lineplot(x=x1, y=y1, label='With load-balancer')

plt.xlabel("Number of Cloudlets")
plt.ylabel("Power Consumption (kWh)")
plt.title('Private Deployment - Power Consumption')
plt.legend(loc='lower right')

plt.show()