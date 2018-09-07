import json
import os 
from os import listdir
from os.path import isfile, join
import sys  
import csv

def CreateFolder(path):
    os.makedirs(path)

def CheckDirectoryAndCreate(path):
    if not os.path.exists(path):
        os.makedirs(path)

def CheckDirectoryExists(path):
    return os.path.exists(path)

def SaveObjectAsJson(FloderPath, FileName, obj):
    CheckDirectoryAndCreate(FloderPath)
    f = open(FloderPath+'/'+FileName, 'w').write(json.dumps(obj)) 

def SaveStringAsFile(FloderPath, FileName, string):
    CheckDirectoryAndCreate(FloderPath)
    f = open(FloderPath+'/'+FileName, 'w').write(string)

def GetJsonDataAsObject(FloderPath, FileName):
    return json.loads(open(FloderPath+'/'+FileName, 'r').read())

def GetCsvAsString(FloderPath, FileName):
    return open(FloderPath+'/'+FileName, 'r').read()

def GetCsvAsList(FolderPath, FileName):
    with open('{}/{}'.format(FolderPath, FileName), 'rb') as f:
        reader = csv.reader(f)
        data = list(reader)
        return data
def GetCsvAsSet(FolderPath, FileName):
    db = set()
    with open('{}/{}'.format(FolderPath, FileName), 'rb') as f:
        reader = csv.reader(f)
        data = list(reader)
        for item in data:
            db.add(item[1].lower())
    return db
def GetFileAsString( FloderPath, FileName):
    return open(FloderPath+'/'+FileName, 'r').read()

def GetFolderSize(FolderPath):
    size = 0
    # Get All Folder Path 
    folders = {FolderPath:False}
    while True:
        tempSave = []
        for folder in folders:
            if folders[folder] is False:
                for path in GetAllFolderPath(folder):
                    tempSave.append(path)
                folders[folder] = True
                break
        for folder in tempSave:
            if folder not in folders:
                folders[folder] = False
        getFolderStop = False
        for isCheck in folders.values():
            if isCheck is False:
                getFolderStop = False
                break
            else:
                getFolderStop = True
        if getFolderStop:
            break
    # Get All File in folders
    for folder in folders:
        for filename in GetAllFileName(folder):
            filePath = '{}/{}'.format(folder,filename)
            size += os.path.getsize(filePath)
    size = size
    return '{} Byte'.format(size)

def GetAllFolderPath(path):
    if not path.endswith('/'):
        path += '/'
    result = [x[0] for x in os.walk(path)]
    result.remove(path)
    return result

def GetAllFileName(path):
    return [f for f in listdir(path) if isfile(join(path, f))]
