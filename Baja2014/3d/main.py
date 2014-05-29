import random
from numpy import * 
from plot import *
from sphere import * 
from readEndurance import run

def fold_tuple_helper(acc,el):
    acc[0] = acc[0]+[el[0]] 
    acc[1] = acc[1]+[el[1]]
    acc[2] = acc[2]+[el[2]]
    return acc

def mapping(el):
    return el[0]
    
def mapping2(el):
    return el[1]

def enduranceMap():
    result = run()
    """result = sphere(500,10)
    result = list(result)
    result.sort()
    xyz = reduce(fold_tuple_helper,result, [[],[],[]])"""

    x = map(mapping, result)
    y = map(mapping2, result)
    z = zip(x,y) 
    filt = filter(lambda x: x[1]<3650, z)
    filt = filter(lambda x: x[1]>3610,filt)
    filt = map(list,zip(*filt))
    x = filt[0]
    y = filt[1]
    #plot_cool(result[0],result[1],[0]*len(x))
    plot(x,y,[0]*len(x))

def enduranceMapMesh():
    result = run()
    """result = sphere(500,10)
    result = list(result)
    result.sort()
    xyz = reduce(fold_tuple_helper,result, [[],[],[]])"""

    x = map(mapping, result)
    y = map(mapping2, result)
    z = zip(x,y) 
    filt = filter(lambda x: x[1]<3650, z)
    filt = filter(lambda x: x[1]>3610,filt)
    filt = map(list,zip(*filt))
    x = filt[0]
    y = filt[1]
    #plot_cool(result[0],result[1],[0]*len(x))
    plot(x,y,[0]*len(x))


def main():
    """result = sphere(50,10)
    result = list(result)
    result.sort()
    xyz = reduce(fold_tuple_helper,result, [[],[],[]])
    result = meshgrid2(xyz[0],xyz[1],xyz[2])
    print "X:%d, Y:%d, Z:%d" % (len(result[0]), len(result[1]), len(result[2])) 
    plot_cool(result[0],result[1],np.convolve(result[2],np.ones(10)/10)[:len(result[0])])"""
    enduranceMap()

main()
