import random
import math
from numpy import *

#generates sphere with some numpoints and some radius r
def sphere(numpoints,radius):
    myset = {(0,0,0)}

    for i in xrange(numpoints):
        phi = random.uniform(0,2*3.14)
        costheta = random.uniform(-1,1)
        u = random.uniform(0,1)

        theta = arccos( costheta )
        r = radius* math.pow(u,(1.0/3))
        x =(r * sin( theta) * cos( phi ))
        y = (r * sin( theta) * sin( phi ))
        z = (r * cos( theta ))
        #print "%0.2f,%0.2f,%0.2f" % (x,y,z) 
        myset.add((x,y,z))
    return myset

def circle(numpoints, radius):
    x = []
    y = []
    for i in range(numpoints/4):
        xvar1 = radius* (float(i)/numpoints/4)
        print xvar1
        xvar2 = -xvar1
        yvar1 = math.sqrt(radius**2 - xvar1**2)
        print yvar1
        yvar2 = -yvar1
        x.append(xvar1)
        y.append(yvar1)
        x.append(xvar1)
        y.append(yvar2)
        x.append(xvar2)
        y.append(yvar1)
        x.append(xvar2)
        y.append(yvar2)

    return (x,y)


