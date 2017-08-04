import json
import os 
from os import listdir
from os.path import isfile, join


class Create:
    def __init__(self):
        pass
    
    def CreateFolder(self, path):
        os.makedirs(path)

    def CheckDirectoryAndCreate(self, path):
        if not os.path.exists(path):
            os.makedirs(path)

class Write:

    create = Create()

    def __init__(self):
        pass

    def SaveObjectAsJson(self, FloderPath, FileName, obj):
        self.create.CheckDirectoryAndCreate(FloderPath)
        f = open(FloderPath+'/'+FileName, 'w').write(json.dumps(obj)) 

    def SaveStringAsFile(self, FloderPath, FileName, string):
        self.create.CheckDirectoryAndCreate(FloderPath)
        f = open(FloderPath+'/'+FileName, 'w').write(string)
    

class Open:
    def __init__(self):
        pass

    def GetJsonDataAsObject(self, FloderPath, FileName):
        return json.loads(open(FloderPath+'/'+FileName, 'r').read())

    def GetCsvAsString(self,  FloderPath, FileName):
        return open(FloderPath+'/'+FileName, 'r').read()
    
    def GetFileAsString(self, FloderPath, FileName):
        return open(FloderPath+'/'+FileName, 'r').read()

class Get:
    def __init__(self):
        pass

    def getAllFolderPath(self, path):
        #return as folder name list
        if not path.endswith('/'):
            path += '/'
        result = [x[0] for x in os.walk(path)]
        result.remove(path)
        return result

    def getAllFileName(self, path):
        return [f for f in listdir(path) if isfile(join(path, f))]