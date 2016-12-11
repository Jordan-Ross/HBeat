print("Filename (no ext or dir):")
filename = raw_input()
path = "..\\android\\assets\\songs\\"
inf = open(path + filename + ".map", 'r')
outf = open(path + filename + ".rbm", 'w')

for line in inf:
	splitln = line.split(',')
	#time = int(splitln[2])
	#outf.write(str(time) + ',' + splitln[0] + ',' + splitln[1] + '\n')
	outf.write(splitln[2] + ',' + splitln[0] + ',' + splitln[1] + '\n')

outf.close()
