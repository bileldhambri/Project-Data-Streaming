import sys
import matplotlib.pyplot as plt

data = []

# read input data from stdin
for line in sys.stdin:
    data.append(line.strip())

print(data)
