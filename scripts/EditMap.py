print("Filename (no ext or dir):")
filename = raw_input()
inf = open("E:\\Documents\\Android Projects\\reflectbeat\\android\\assets\\" + filename + ".map", 'r')
outf = open("E:\\Documents\\Android Projects\\reflectbeat\\android\\assets\\" + filename + ".rbm", 'w')




# Because the original maps have the time the circles _appear_, 
# we need to calculate the time_to_get_to_line, because the 
# circles will actually spawn this amount of time before.

# Calculations will be done here for now

#time_to_get_to_line = distance_between_spawn_and_hit_spot / y_speed
#map_height = 960
##line height - 100, line width/2 = 20
#hit_spot_height = 120
#y_speed = 300
#time_to_get_to_line = float(map_height - hit_spot_height)/float(y_speed)




for line in inf:
	splitln = line.split(',')
	
	#Adjust timing
	time = int(splitln[2])
	#time = time - time_to_get_to_line
	outf.write(str(time) + ',' + splitln[0] + ',' + splitln[1] + '\n')

outf.close()
