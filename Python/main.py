c = open('test.csv', 'r').read()
for i in c.split('\n'):
    print(i)