import requests
import json

class lib:
    def __init__(self):
        pass

    def getJson(self, url):
        result = None
        r = requests.get(url)
        if(r.status_code==200):
            result = json.loads(r.text)
        else:
            log = 'HTTP Request Fail : {}'.format(r.status_code)
            print(log)
            exit()
        return result