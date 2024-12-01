left = []
right = []

with open("input01", "r") as filo:
    for line in filo:
        [f,s] = line.split("   ")
        left.append(int(f))
        right.append(int(s))

left.sort()
right.sort()

x = 0

for i in range(len(left)):
    x = x + abs(left[i] - right[i])

print("Part 1:", x)

summy = 0

for num in left:
    try:
        idx = right.index(num)
    except:
        continue

    count = 0
    while (idx < len(right)):
        if right[idx] == num:
            count = count + 1
            idx = idx + 1
        else:
            break
    
    summy = summy + num * count

print("Part 2:", summy)
