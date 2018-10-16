from scipy.spatial import distance

# dummy population
population = [[0.1, 0.5, 4.5, 0.4, 2.3, -3.0, -4.1, 0.9, -0.1, 4.9],[0.8, 4.5, -0.3, 4.3, -4.2, 0.2, 0.3, 0.4, 0.6, 0.7],
				[0.5, -0.5, 3.5, 0.4, 2.3,0.8, 3.9, -0.5, 4.2, -4.2 ],[1.8, -4.5, -0.4, 4.4, 4.2, 0.9, 1.1, 0.7, -0.6, 0.7],
				[3.5, 0.5, 3.3, -0.4, -2.3, 0.8, 3.8, -2.1, 2.8, -4.2 ],[-4.1, 0.7, 1.5, -0.4, 2.1, -3.3, 1.1, 0.6, -0.1, 4.8]]

# compute euclidian distance between every individual in population

all_distance = []
length = 0
total_distance = 0
for child1 in population:
	for child2 in population:
		if child1==child2:
			continue

		d = distance.euclidean(child1, child2)
		all_distance.append(d)
		length+=1
		total_distance +=d

print(total_distance/length)
print(all_distance)


import numpy as np
import matplotlib.pyplot as plt

fig1, ax1 = plt.subplots()
ax1.set_title('Euclidian distance between individuals in the population')
ax1.boxplot(all_distance)

plt.show()
