import json

class ListObjecrProcess:
    def __init(self):
        pass

    def removeListRepeatItem(self, data):
        startcount = len(data)
        for i, item in enumerate(data):
            print('{currentIndex}/{total}'.format(currentIndex=i+1, total=len(data)))
            for checkIndex, checkItem in enumerate(data):
                if checkItem == item and i != checkIndex:
                    del data[checkIndex]
                    print('delete {currentIndex} -X- {checkIndex}'.format(currentIndex=i, checkIndex=checkIndex) )
        endcount = len(data)
        if startcount == endcount:
            print ('No Repeat Item')
        else :
            print ('Repeat Item Been Remove')
        return data
                