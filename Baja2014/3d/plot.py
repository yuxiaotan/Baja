from numpy import *
import numpy as np
import pylab as p
#import matplotlib.axes3d as p3
import mpl_toolkits.mplot3d.axes3d as p3

def plot(x,y,z):
    fig=p.figure()
    ax = p3.Axes3D(fig)
    ax.scatter(x,y,z)
    ax.set_xlabel('X')
    ax.set_ylabel('Y')
    ax.set_zlabel('Z')
    p.show()

def plot_cool(x,y,z):
    fig=p.figure()
    ax = p3.Axes3D(fig)
    ax.plot_surface(x,y,z)
    ax.set_xlabel('X')
    ax.set_ylabel('Y')
    ax.set_zlabel('Z')
    p.show()

def meshgrid(xs,ys,zs):
    cols = np.unique(xs).shape[0]
    x = np.reshape(xs,cols)
    y = np.reshape(ys,cols) 
    z = np.reshape(zs,cols) 
    a = np.vstack((x,y,z)).T
    a = a[np.lexsort((a[:,0], a[:,1]))]
    x,y,z = a.T
    return (x,y,z)

def meshgrid2(x,y,z):
    xs,ys = np.meshgrid(x,y)
    return (xs,ys,z)

