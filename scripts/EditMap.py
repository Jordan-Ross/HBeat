print("Filename (no ext or dir):")
filename = raw_input()
inf = open("E:\\Documents\\Android Projects\\reflectbeat\\android\\assets\\" + filename + ".map", 'r')
outf = open("E:\\Documents\\Android Projects\\reflectbeat\\android\\assets\\" + filename + ".rbm", 'w')

for line in inf:
	splitln = line.split(',')
	outf.write(splitln[2] + ',' + splitln[0] + ',' + splitln[1] + '\n')

outf.close()
