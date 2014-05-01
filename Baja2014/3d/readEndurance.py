import csv

def run():
    datafile = open('datafile.csv','r')
    datareader = csv.reader(datafile)
    data = []

    for row in datareader:
        trow =[]
        trow.append(float(row[0]))
        trow.append(float(row[1]))
        data.append(trow)
    return data

def readFullEndurance():
    datafile = open('Endurance.csv','rU')
    datareader = csv.reader(datafile)
    data = []

    for row in datareader:
        data.append(row)
    return data

readFullEndurance()
